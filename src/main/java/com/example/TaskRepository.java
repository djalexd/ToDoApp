package com.example;

import com.example.domain.Task;
import com.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    //rng data
    // @Query("select Task t where t.assignee = :u")
    Page<Task> findByAssignee(User u, Pageable pagingInfo);
    Task findById(Long taskId);
    void delete(Long taskId);
}
