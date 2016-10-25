package com.example;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Manages the lifecycle of users
 */
@Data//. Don't know if it is useful here
@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    void create(User newUser){
        userRepository.save(newUser);
    }

    List<User> list(){
        return userRepository.findAll();
    }

    void delete(final long userId){

    }
}
