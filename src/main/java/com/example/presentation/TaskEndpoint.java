package com.example.presentation;

import com.example.*;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskEndpoint {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    static class AuthenticationException extends Exception {}

    // exception handler (separate class)
    // handler method

    // param1=value1&param2=value2&param3=value3

    // <form>
    //   <textbox name=param1>
    // </form>
    //@SomeSecurity("onlyRunIfAuthenticated") // throw Exception if there is no logged in user, otherwise continue
    @RequestMapping(path = "/create-task", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void doCreateTask(
             final @RequestParam(value = "message") String content,
             final @SessionAttribute("CURRENT_USER") Optional<Long> loggedInUserId,
             HttpServletResponse response) throws Exception {
        // Authentication
        if (!loggedInUserId.isPresent()) {
            // Should I really have thrown an Exception, or I could have just set the Suitable HTTP Status and return?
            throw new AuthenticationException();
        }
        User currentUser = loggedInUserId
                 .map(id -> userRepository.findOne(id))
                 .orElseThrow(() -> new RuntimeException("No such user exists anymore!")); // why not the same AuthenticationException?
        // Should I really have thrown an Exception, or I could have just set the Suitable HTTP Status and return?
        // authorization - at some point in the future, here the necessary role will be checked

        // WHAT IF CURRENT USER GETS DELETED HERE?(the create method will fail I assume) - could use a tx(@Transactional(Transactional.TxType.REQUIRES_NEW))!(any benefit here?)

        // service call
        taskService.create(currentUser.getId(), content);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @RequestMapping(path = "/assign-task", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void doAssignTask(
            final @RequestParam(value = "toUserName") String toUser,
            final @RequestParam(value = "taskID") Long taskId,
            HttpServletResponse response,
            @SessionAttribute(Constants.CURRENT_USER) Optional<Long> loggedInUserId) throws Exception {

        if(!loggedInUserId.isPresent()){
            throw new AuthenticationException();
        }
        User currentUser = loggedInUserId
                .map(id -> userRepository.findOne(id))
                .orElseThrow(() -> new RuntimeException("No such user exists anymore!"));
        // authorization - none



        Task task = taskService.getTaskById(taskId); // get task by id
        if(task == null){
            // invalid taskId - how can I communicate to the client more details about the response error?
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        User assigneeCandidate = userService.findByUserName(toUser);
        if(assigneeCandidate == null){
            // invalid user - how can I communicate to the client more details about the response error?
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        currentUser = userService.getCurrentUser();
        if(currentUser == null || !currentUser.equals(task.getAuthor()) || !currentUser.equals(task.getAssignee())){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        task.setAssignee(assigneeCandidate);
        taskService.update(taskId, task);

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping(path = "/update-task", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void doModifyTask(
            final @RequestParam(value = "newMessage") String newMessage,
            final @RequestParam(value = "taskId") Long taskId,
            HttpServletResponse response){

        Task task = taskService.getTaskById(taskId); // get task by id
        if(task == null){
            // invalid taskId - how can I communicate to the client more details about the response error?
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        User currentUser = userService.getCurrentUser();
        if(currentUser == null || !currentUser.equals(task.getAuthor()) || !currentUser.equals(task.getAssignee())){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        task.setMessage(newMessage);
        taskService.update(taskId, task);

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping(path = "/delete-task", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void doDeleteTask(
            final @RequestParam(value = "taskId") Long taskId,
            HttpServletResponse response){

        Task task = taskService.getTaskById(taskId); // get task by id
        if(task == null){
            // invalid taskId - how can I communicate to the client more details about the response error?
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        User currentUser = userService.getCurrentUser();
        if(currentUser == null || !currentUser.equals(task.getAuthor())){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        taskService.delete(taskId);

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping(path = "/list-tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> listTasks() {
        return taskService.list();
    }
}
