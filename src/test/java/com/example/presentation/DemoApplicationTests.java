package com.example.presentation;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class DemoApplicationTests extends RESTTests {

    @Test
	public void indexShowsHelloWorld() throws IOException {
        // Simulate we are coming from outside (HTTP request)
        // Get result
        ResponseEntity<String> response = ops.getForEntity("http://localhost:{port}/index.html", String.class, serverPort);
        String result = response.getBody();

        // Compare with 'Hello, world.'
        Assert.assertEquals("Hello, world.", result);
    }
}
