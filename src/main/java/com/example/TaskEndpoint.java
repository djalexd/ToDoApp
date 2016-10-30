package com.example;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class TaskEndpoint {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    // param1=value1&param2=value2&param3=value3

    // <form>
    //   <textbox name=param1>
    // </form>
    @RequestMapping(path = "/create-task", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void doCreateTask(
            final @RequestParam(value = "message") String contents,
            HttpServletResponse response) throws Exception {

        User currentUser = UserService.getCurrentUser();
        if(currentUser == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        Task task = new Task();
        task.setMessage(contents);
        task.setAuthor(currentUser);
        // default assignment
        task.setAssignee(currentUser);
        taskService.create(task);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @RequestMapping(path = "/assign-task", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void doAssignTask(
            final @RequestParam(value = "toUserName") String toUser,
            final @RequestParam(value = "taskID") Long taskId,
            HttpServletResponse response){

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
        User currentUser = UserService.getCurrentUser();
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
        User currentUser = UserService.getCurrentUser();
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
        User currentUser = UserService.getCurrentUser();
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
