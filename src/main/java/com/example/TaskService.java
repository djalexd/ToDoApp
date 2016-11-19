package com.example;

import com.example.domain.Task;
import com.example.domain.User;
import com.example.domain.vo.TaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Manages the lifecycle of tasks!
 */
@Component
@Transactional
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserRepository userRepository;
	
	private static Supplier<RuntimeException> noTaskWithId(Long id) {
		return () -> new IllegalArgumentException(String.format("There is no task with given id=%s", id));
	}

	/**
	 * Creates a new task with current authenticated user.
	 */
	@PreAuthorize("isAuthenticated()")
	public Task create(TaskVO taskData) {
		final User owner = requireAuthUser();

		final Task task = new Task();
		task.setMessage(taskData.getMessage());
		task.setAuthor(owner);
		task.setAssignee(maybeGetAssignee(taskData.getAssigneeId()));
		return taskRepository.save(task);
	}

	private final User requireAuthUser() {
		final Long ownerId = Long.valueOf(((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
		final User owner = userRepository.findOne(ownerId);
		if (owner == null) {
			throw new IllegalArgumentException(String.format("Invalid User with id=%s", ownerId));
		}
		return owner;
	}

	private User maybeGetAssignee(Long assigneeId) {
		return Optional.ofNullable(assigneeId)
				.map(id -> userRepository.findOne(id))
				.orElse(null);
	}

	public List<Task> list() {
		return taskRepository.findAll();
	}

	public Task getTaskById(Long taskId) {
		return taskRepository.findById(taskId);
	}

	@PreAuthorize("isAuthenticated() and (@mySecurity.isTaskCreator(#id) || @mySecurity.isTaskAssignee(#id))")
	public Task update(final long id, final TaskVO updateTask) {
		return Optional.of(id)
				.map(this::getTaskById)
				.map(task -> {
					task.setMessage(updateTask.getMessage());
					task.setAssignee(maybeGetAssignee(updateTask.getAssigneeId()));
					return task;
				})
				.orElseThrow(noTaskWithId(id));
	}
	
	@PreAuthorize("isAuthenticated() and @mySecurity.isTaskCreator(#id)")
	public Task delete(final long id) {
		return Optional.of(id)
				.map(this::getTaskById)
				.map(task -> {
					taskRepository.delete(task);
					return task;
				}).orElseThrow(noTaskWithId(id));
	}
}
