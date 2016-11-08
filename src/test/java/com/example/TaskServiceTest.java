package com.example;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles({ "in_memory" })
public class TaskServiceTest {
    @Autowired
    private TaskService service;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authentication;

    private User existingUser;

    @Before
    public void createUserBeforeEachTest() {
        existingUser = new User("alex", "adelina");
        userService.create(existingUser);

        // TODO check these suggestions, which one fits :)
        // userService.create(String username, String password): User
        // userService.create(UserDTO user): User
    }

    @Test(expected = Exception.class)
    public void shouldNotBeAbleToDeleteTaskWhenAnonymous() throws Exception {
        // Create task
        Task task = service.create(existingUser.getId(), "Sample");
        // Delete task
        try {
            SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("(anon)", "(anon)", Collections.emptyList()));
            /* action */
            service.delete(task.getId());
            /* action */
        } catch (Exception e) {
            // Check if task still exists
            Assert.assertNotNull(service.getTaskById(task.getId()));
            throw e;
        }
    }


    @Test
    public void shouldDeleteTaskWhenCalledByOwner() throws Exception {
        // Create task
        Task task = service.create(existingUser.getId(), "Sample");
        // Delete task
        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(existingUser.getUserName(), existingUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(token);
        /* action */
        service.delete(task.getId());
        /* action */

        // Check if task still exists
        Assert.assertNull(service.getTaskById(task.getId()));
    }


    @Test(expected = Exception.class)
    public void shouldNotDeleteTaskWhenCalledBySomeOneElseThanOwner() throws Exception {
        // Create task
        Task task = service.create(existingUser.getId(), "Sample");
        // Delete task
        User badBoy = new User("alex badBoy", "adelina");
        userService.create(badBoy);
        try {
            final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(badBoy, "(password)");
            SecurityContextHolder.getContext().setAuthentication(token);
            /* action */
            service.delete(task.getId());
            /* action */
        } catch (Exception e) {
            // Check if task still exists
            Assert.assertNotNull(service.getTaskById(task.getId()));
            throw e;
        }
    }

}
