package com.example.presentation;

import com.example.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskTests {

    private static RestOperations ops = new RestTemplate();

    // 1st test - create new task over HTTP
    @Test
    public void createTask() throws IOException {
        // Simulate we are coming from outside (HTTP request)
        // Get result
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("message", "first task ever!");
        ResponseEntity<String> response = ops.postForEntity("http://localhost:8080/create-task", map, String.class);

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
        ResponseEntity<ListOfTasks> response = ops.getForEntity("http://localhost:8080/list-tasks", ListOfTasks.class);

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
