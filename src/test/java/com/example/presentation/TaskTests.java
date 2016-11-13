package com.example.presentation;

import com.example.domain.Task;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Ignore
public class TaskTests extends RESTTests {

    // 1st test - create new task over HTTP
    @Test
    public void createTask() throws IOException {
        // Simulate we are coming from outside (HTTP request)
        // Get result
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("message", "first task ever!");
        ResponseEntity<String> response = ops.postForEntity("http://localhost:{port}/create-task", map, String.class, serverPort);

        // HTTP 201 CREATED
        Assert.assertEquals(201, response.getStatusCodeValue());
    }

    // 2nd test - create new task & list
    @Test
    public void listTasks() throws IOException {
        final String content = "One of the tasks!";

        // Simulate we are coming from outside (HTTP request)
        LinkedMultiValueMap<String, Object> map0 = new LinkedMultiValueMap<>();
        map0.add("message", content);
        ops.postForEntity("http://localhost:8080/create-task", map0, String.class);

        // Get result
        ResponseEntity<ListOfTasks> response = ops.getForEntity("http://localhost:{port}/list-tasks", ListOfTasks.class, serverPort);

        // - Response is JSON
        MediaType contentType = response.getHeaders().getContentType();
        Assert.assertNotNull(contentType);
        Assert.assertTrue(contentType.isCompatibleWith(MediaType.APPLICATION_JSON));
        // - Expect at least 1 task
        List<Task> tasks = response.getBody();
        Assert.assertTrue("expect to find at least 1 task", tasks.size() >= 1);
        // - All tasks need to have 'id'
        tasks.forEach(t -> Assert.assertTrue("Task contains id", t.getId() > 0));
        // - At least one task has the contents above
        long numTasksThatMatch = tasks
                .stream()
                .map(Task::getMessage)
                .filter(m -> m.equals(content))
                .count();
        Assert.assertTrue("expect to find at least 1 task with created content", numTasksThatMatch >= 1);
    }

    // List<Task> -> String -> .... wire .... -> String -> List<Task>
    final static class ListOfTasks extends ArrayList<Task> {}

}
