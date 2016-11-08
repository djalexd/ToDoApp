package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("mySecurity")
public class MySpecialSecurity {

    @Autowired
    private TaskRepository taskRepository;

    public boolean isTaskCreator(Long taskId) {
        Task t = taskRepository.findOne(taskId);
        if (t == null) {
            return true;
        } else {
            User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return t.getAuthor().getId().equals(u.getId());
        }
    }
}
