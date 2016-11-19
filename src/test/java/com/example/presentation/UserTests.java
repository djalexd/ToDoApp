package com.example.presentation;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

@Slf4j
public class UserTests extends RESTTests {

    @Test
    public void createUser(){
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("userName", "adelina");
        map.add("password", "adelina");

        // simulate http post request for creating user. But before this will work I need to create the endpoint for creating users
        ResponseEntity<String> response = ops.postForEntity("http://localhost:{port}/create-user", map, String.class, serverPort);
        System.out.println("What is up??? " + response.getBody());
        // de unde am stiut sa pun ResponseEntity<String>? si nu generic de alt tip?

        Assert.assertEquals("", 201, response.getStatusCodeValue());
    }
}
