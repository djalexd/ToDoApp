package com.example;


import com.example.domain.Task;
import com.example.domain.User;
import com.example.domain.vo.TaskVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
	private Task existingTask;

    @Before
    public void createUserBeforeEachTest() {
        existingUser = new User("alex", "adelina");
        userService.create(existingUser);

        // TODO check these suggestions, which one fits :)
        // userService.create(String username, String password): User
        // userService.create(UserDTO user): User

	    // Create task
	    TaskVO vo = TaskVO.builder().message("Sample").build();
	    existingTask = SecurityContext.executeInUserContext(existingUser, u -> service.create(vo));
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldNotBeAbleToDeleteTaskWhenAnonymous() throws Exception {
        // Delete task
        try {
            SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("(anon)", "(anon)", Collections.singletonList(new SimpleGrantedAuthority("(anon)"))));
            /* action */
            service.delete(existingTask.getId());
            /* action */
        } catch (AccessDeniedException e) {
            // Check if task still exists
            Assert.assertNotNull(service.getTaskById(existingTask.getId()));
            throw e;
        }
    }

    @Test
    public void shouldDeleteTaskWhenCalledByOwner() throws Exception {
        // Delete task
        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(existingUser.getUserName(), existingUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(token);
        /* action */
        service.delete(existingTask.getId());
        /* action */

        // Check if task still exists
        Assert.assertNull(service.getTaskById(existingTask.getId()));
    }


    @Test(expected = AccessDeniedException.class)
    public void shouldNotDeleteTaskWhenCalledBySomeOneElseThanOwner() throws Exception {
        // Delete task
        User badBoy = new User("alex badBoy", "adelina");
        userService.create(badBoy);
        try {
            final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(badBoy.getUserName(), badBoy.getPassword());
            SecurityContextHolder.getContext().setAuthentication(token);
            /* action */
            service.delete(existingTask.getId());
            /* action */
        } catch (AccessDeniedException e) {
            // Check if task still exists
            Assert.assertNotNull(service.getTaskById(existingTask.getId()));
            throw e;
        }
    }

}
