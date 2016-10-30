package com.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles({ "in_memory" })
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Before
    public void createUserBeforeTest() {
        userService.create(new User("demo", "password"));
    }

    @Test
    public void testFindUserBySpringRepository() {
        Assert.assertNotNull("expect to find user by name 'demo'", userService.findByUserName("demo"));
    }

    @Test
    public void testFindUserByEntityManager() {
        Assert.assertNotNull("expect to find user by name 'demo'", userService.findByUserNameEM("demo"));
    }

    @Test
    public void testLoginLogout(){
        User userToAuthenticate = new User("demo", "password");
        Assert.assertEquals("Expected no user to be logged in!", null, UserService.getSession().getAttribute(Constants.CURRENT_USER));

        try {
            userService.login(userToAuthenticate);
            Assert.assertEquals("Logged in user is not the desired one!", userToAuthenticate, UserService.getSession().getAttribute("CURRENT_USER"));
        } catch (AuthenticationException e) {
            Assert.fail("Login failed!");
            e.printStackTrace();
        }

        userService.logout();
        Assert.assertEquals("Logout performed unsuccessfully!", null, UserService.getSession().getAttribute(Constants.CURRENT_USER));
    }
}