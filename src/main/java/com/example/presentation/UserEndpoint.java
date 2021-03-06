package com.example.presentation;

import com.example.UserService;
import com.example.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserEndpoint {
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/create-user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void doCreateUser(final @RequestParam(value = "userName") String userName,
                             final @RequestParam(value = "password") String pass,
                             HttpServletResponse response) {
        User newUser = new User(userName, pass); // this constructor was not automatically generated
        userService.create(newUser);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @RequestMapping(path = "/list-users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> listUsers() {
        return userService.list();
    }

    @RequestMapping(path = "/get-user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(final @RequestParam(value = "userName") String userName) {
        return userService.findByUserName(userName);
    }
}
