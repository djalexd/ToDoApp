package com.example;

import com.example.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.naming.AuthenticationException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Manages the lifecycle of users
 */
@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;

    public void create(User newUser){
        userRepository.save(newUser);
    }

    public List<User> list(){
        return userRepository.findAll();
    }

    public User findByUserName(String userName) { return userRepository.findByUserName(userName); }

    void delete(final long userId){

    }

    public User findByUserNameEM(String userName) {
        TypedQuery<User> q = em.createQuery("select u from User u where u.userName = :userName", User.class);
        q.setParameter("userName", userName);
        return q.getSingleResult();
    }

    // example usage
    public HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    public User getCurrentUser(){
        return (User)getSession().getAttribute(Constants.CURRENT_USER);
    }

    public void login(String userName, String pass) throws AuthenticationException {
        User user = findByUserNameEM(userName);

        if(user == null || !user.getPassword().equals(pass)){
            throw new AuthenticationException("Invalid username or password");
        }

        getSession().setAttribute(Constants.CURRENT_USER, user.getId());
    }

    public void logout(){
        getSession().setAttribute(Constants.CURRENT_USER, null);
    }
}
