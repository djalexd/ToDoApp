package com.example;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.naming.AuthenticationException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.jar.Pack200;

/**
 * Manages the lifecycle of users
 */
//. Don't know if it is useful here
@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;

    void create(User newUser){
        userRepository.save(newUser);
    }

    List<User> list(){
        return userRepository.findAll();
    }

    User findByUserName(String userName) { return userRepository.findByUserName(userName); }

    void delete(final long userId){

    }

    public User findByUserNameEM(String userName) {
        TypedQuery<User> q = em.createQuery("select u from User u where u.userName = :userName", User.class);
        q.setParameter("userName", userName);
        return q.getSingleResult();
    }

    // example usage
    public static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    public static User getCurrentUser(){
        return (User)getSession().getAttribute(Constants.CURRENT_USER);
    }

    public void login(User userToAuth) throws AuthenticationException {

        User userCredentials = findByUserNameEM(userToAuth.getUserName());
        if(userCredentials.getPassword().equals(userToAuth.getPassword())){
            HttpSession session = getSession();
            session.setAttribute(Constants.CURRENT_USER, userToAuth);
        } else {
            throw new AuthenticationException("Invalid username of password");
        }
    }

    public void logout(){
        getSession().setAttribute(Constants.CURRENT_USER, null);
    }
}
