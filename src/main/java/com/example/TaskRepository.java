package com.example;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    //rng data
    // @Query("select Task t where t.assignee = :u")
    Page<Task> findByAssignee(User u, Pageable pagingInfo);
    Task findById(Long taskId);
    void delete(Long taskId);
}
