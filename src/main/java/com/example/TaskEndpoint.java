package com.example;

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

    // param1=value1&param2=value2&param3=value3

    // <form>
    //   <textbox name=param1>
    // </form>
    @RequestMapping(path = "/create-task", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void doCreateTask(
            final @RequestParam(value = "message") String contents,
            HttpServletResponse response) throws Exception {

        Task task = new Task();
        task.setMessage(contents);
        taskService.create(task);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @RequestMapping(path = "/list-tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> listTasks() {
        return taskService.list();
    }
}
