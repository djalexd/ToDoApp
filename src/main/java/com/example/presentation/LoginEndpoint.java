package com.example.presentation;

import com.example.Constants;
import com.example.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by alexpeptan on 31/10/16.
 */
public class LoginEndpoint {

    private UserService loginService;

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void login(@RequestParam String name, @RequestParam String passwd,
                      HttpServletRequest req, HttpServletResponse response) throws Exception {
        // Try to authenticate
        try{
            loginService.login(name, passwd);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    public void logout(HttpServletRequest req) {
        req.getSession(false).removeAttribute(Constants.CURRENT_USER);
    }
}
