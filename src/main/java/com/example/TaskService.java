package com.example;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Manages the lifecycle of tasks!
 */
@Data
@Component
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    void create(Task newTask) throws Exception {
        // begin java transaction api (jta)
//        tm.
//        try {
//            // persist jpa entity
//            em.persist(newTask);
//            // commit transaction
//            tm.commit();
//        } catch (RuntimeException e) {
//            // for any exceptions, rollback (don't store data)
//            tm.rollback();
//        }
        taskRepository.save(newTask);
    }

    List<Task> list() {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Task> criteria = cb.createQuery( Task.class );
//        Root<Task> taskRoot = criteria.from( Task.class );
//        criteria.select( taskRoot );
//        return em.createQuery(criteria).getResultList();
        return taskRepository.findAll();
    }

    void update(final long taskToUpdateId, final Task updateTask) {}

    void delete(final long taskToDeleteId) {}
}
