package com.example;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Manages the lifecycle of tasks!
 */
@Data
@Component
public class TaskService {

    @Autowired
    private PlatformTransactionManager ptm;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;
    //@Autowired
    //TransactionManager tm; // not possible to inject this here..

    /**
     * Creates a new task with current authenticated user.
     */
    public Task create(Long ownerId, String message) throws Exception {
        User owner = userRepository.findOne(ownerId);

        TransactionStatus status = ptm.getTransaction(new DefaultTransactionDefinition());
        // begin java transaction api (jta)
        //EntityTransaction tx = em.getTransaction();
        //tx.begin();
        try{
            Task newTask = new Task();
            newTask.setAuthor(owner);
            newTask.setMessage(message);
            newTask.setAssignee(owner);
            // persist JPA entity
            em.persist(newTask);
            // commit tx
            //tx.commit();
            ptm.commit(status);
            return newTask;
        } catch (RuntimeException ex) {
            ptm.rollback(status);
            //tx.rollback();
            throw ex;
        }
    }

    public List<Task> list() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long taskId){
        return taskRepository.findById(taskId);
    }

    public void update(final long taskToUpdateId, final Task updateTask) {
        TypedQuery<Task> q = em.createQuery("update t from Task t set t.message=:message, t.assignee=:assignee " +
                "where t.id = ?3", Task.class);
        q.setParameter("message", updateTask.getMessage());
        q.setParameter("assignee", updateTask.getAssignee());
        q.setParameter(3, taskToUpdateId);
        q.executeUpdate();
    }

    @Transactional
    @PreAuthorize("isAuthenticated() and mySecurity.isTaskCreator(#id)")
    public void delete(final long id) {
        final Task existing = taskRepository.findOne(id);
        // TODO what happens if 'existing' does not exist??
        taskRepository.delete(id);
    }
}
