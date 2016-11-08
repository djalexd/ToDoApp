package com.example.presentation;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by alexpeptan on 23/10/16.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserTests {

    //private static final Logger LOGGER = LoggerFactory.getLogger(UserTests.class);

    private static RestOperations ops = new RestTemplate();

    @Test
    public void createUser(){
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("userName", "adelina");
        map.add("password", "adelina");

        // simulate http post request for creating user. But before this will work I need to create the endpoint for creating users
        ResponseEntity<String> response = ops.postForEntity("http://localhost:8080/create-user", map, String.class);
        System.out.println("What is up??? " + response.getBody());
        // de unde am stiut sa pun ResponseEntity<String>? si nu generic de alt tip?

        Assert.assertEquals("", 201, response.getStatusCodeValue());
    }
}
