package com.example;

import com.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by alexpeptan on 23/10/16.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    // not extra functionality added for the moment. Do I need any? Do I need this interface?
    User findByUserName(String userName);
}
