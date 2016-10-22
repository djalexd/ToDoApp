package com.example;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DemoApplicationTests {

    private static HttpClient client = HttpClients.createDefault();

    @Test
	public void indexShowsHelloWorld() throws IOException {
        // Simulate we are coming from outside (HTTP request)
        // Get result
        HttpResponse response = client.execute(new HttpGet("http://localhost:8080/index.html"));
        String result = EntityUtils.toString(response.getEntity());

        // Compare with 'Hello, world.'
        Assert.assertEquals("Hello, world.", result);
    }
}
