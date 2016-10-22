package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RestController
public class TaskEndpoint {

    @Autowired
    private TaskService taskService;

    @RequestMapping(path = "/create-task", method = RequestMethod.POST)
    public void doCreateTask(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final String contents = FileCopyUtils.copyToString(
                new InputStreamReader(request.getInputStream()));

        TaskService.Task task = new TaskService.Task();
        task.setMessage(contents);
        taskService.create(task);

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @RequestMapping(path = "/list-tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskService.Task> listTasks() {
        return taskService.list();
    }
}
