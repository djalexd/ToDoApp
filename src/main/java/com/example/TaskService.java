package com.example;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the lifecycle of tasks!
 */
@Data
@Component
public class TaskService {

    @Data
    static class Task {
        private long id;
        private String message;
    }

    private Map<Long, Task> tasks = new HashMap<>();
    private int counter = 0;

    void create(Task newTask) {
        newTask.setId(counter++);
        tasks.put(newTask.getId(), newTask);
    }

    List<Task> list() {
        return new ArrayList<>(tasks.values());
    }

    void update(final long taskToUpdateId, final Task updateTask) {}

    void delete(final long taskToDeleteId) {}
}
