package com.example;

import com.google.gson.Gson;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskTests {

    private static HttpClient client = HttpClients.createDefault();

    // 1st test - create new task over HTTP
    @Test
    public void createTask() throws IOException {
        // Simulate we are coming from outside (HTTP request)
        HttpPost post = new HttpPost("http://localhost:8080/create-task");
        post.setEntity(new StringEntity("The first task ever!"));
        // Get result
        HttpResponse response = client.execute(post);

        // HTTP 201 CREATED
        Assert.assertEquals(201, response.getStatusLine().getStatusCode());
    }

    // 2nd test - create new task & list
    @Test
    public void listTasks() throws IOException {
        // Simulate we are coming from outside (HTTP request)
        HttpPost post = new HttpPost("http://localhost:8080/create-task");
        final String content = "One of the tasks!";
        post.setEntity(new StringEntity(content));
        client.execute(post);
        // Get result
        HttpResponse response = client.execute(new HttpGet("http://localhost:8080/list-tasks"));

        // - Response is JSON
        Header contentType = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
        Assert.assertNotNull(contentType);
        Assert.assertEquals("application/json;charset=UTF-8", contentType.getValue());
        // - Expect at least 1 task
        String result = EntityUtils.toString(response.getEntity());
        Gson gson = new Gson();
        List<Map> fromJson = gson.fromJson(result, List.class);
        Assert.assertTrue("expect to find at least 1 task", fromJson.size() >= 1);
        // - All tasks need to have 'id'
        fromJson.forEach(map -> {
            Assert.assertTrue("Task contains id", map.containsKey("id"));
        });
        // - At least one task has the contents above
        long numTasksThatMatch = fromJson
                .stream()
                .filter(map -> {
                    return content.equals(map.get("message"));
                })
                .count();
        Assert.assertTrue("expect to find at least 1 task with created content", numTasksThatMatch >= 1);
    }
}
