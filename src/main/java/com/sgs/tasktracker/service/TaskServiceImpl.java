package com.sgs.tasktracker.service;

import com.sgs.tasktracker.dto.TaskRequestDTO;
import com.sgs.tasktracker.dto.TaskResponseDTO;
import com.sgs.tasktracker.entity.Task;
import com.sgs.tasktracker.entity.Task.Status;
import com.sgs.tasktracker.entity.User;
import com.sgs.tasktracker.repository.TaskRepository;
import com.sgs.tasktracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepo;
    @Autowired
    private UserRepository userRepo;

    public TaskResponseDTO toDto(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();

        dto.setTitle(task.getTitle());
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setCreatedDate(task.getCreatedDate());
        dto.setStatus(task.getStatus());
        dto.setAssignedUserId(task.getAssignedUser().getId().longValue());
        dto.setDueDate(task.getDueDate());

        return dto;
    }

    @Override
    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        System.out.println(">>> createTask() - Hitting DB");
        User user = userRepo.findById(dto.getAssignedUserId().intValue())
                .orElseThrow(() -> new EntityNotFoundException("User not found, please retry."));

        Task task = new Task(dto.getTitle(), dto.getDescription(), dto.getStatus() != null
                ? Status.valueOf(dto.getStatus()) : Status.NEW,
                dto.getDueDate(), LocalDate.now(), user);

        taskRepo.save(task);
        return toDto(task);
    }

    @Override
    @Cacheable(value = "task", key = "#id")
    public TaskResponseDTO getTaskById(Integer id) {
        System.out.println(">>> getTaskById(" + id + ") - Hitting DB");
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Please retry. Task isn't found."));
        return toDto(task);
    }

    @Override
    @Cacheable("tasks")
    public ArrayList<TaskResponseDTO> getAllTasks() {
        System.out.println(">>> getAllTasks() - Hitting DB");
        return new ArrayList<>(taskRepo.findAll()
                .stream()
                .map(this::toDto)
                .toList());
    }

    @Override
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public TaskResponseDTO updateTask(Integer id, TaskRequestDTO updatedTask) {
        System.out.println(">>> updateTask(" + id + ") - Hitting DB");
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task update failed, entity not found."));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(Task.Status.valueOf(updatedTask.getStatus()));
        task.setDueDate(updatedTask.getDueDate());

        taskRepo.save(task);
        return toDto(task);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public void deleteTask(Integer id) {
        System.out.println(">>> deleteTask(" + id + ") - Hitting DB");
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id %d not found.".formatted(id)));

        User user = task.getAssignedUser();
        if (user != null) {
            user.getTasks().remove(task);
        }

        taskRepo.deleteById(id);
    }

    @Override
    @Cacheable(value = "tasksByStatus", key = "#status")
    public ArrayList<TaskResponseDTO> getTasksByStatus(Status status) {
        System.out.println(">>> getTasksByStatus(" + status + ") - Hitting DB");
        return new ArrayList<>(taskRepo.findByStatus(status)
                .stream()
                .map(this::toDto)
                .toList());
    }

    @Override
    @Cacheable(value = "tasksByDueDate", key = "#date")
    public ArrayList<TaskResponseDTO> getTasksByDueDateBefore(LocalDate date) {
        System.out.println(">>> getTasksByDueDateBefore(" + date + ") - Hitting DB");
        return new ArrayList<>(taskRepo.findByDueDateBefore(date)
                .stream()
                .map(this::toDto)
                .toList());
    }

    @Override
    @Cacheable(value = "tasksByUser", key = "#userId")
    public ArrayList<TaskResponseDTO> getTasksByAssignedUserId(Integer userId) {
        System.out.println(">>> getTasksByAssignedUserId(" + userId + ") - Hitting DB");
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User wasn't found, please retry."));

        return new ArrayList<>(taskRepo.findByAssignedUser(user)
                .stream()
                .map(this::toDto)
                .toList());
    }

    @Override
    @CacheEvict(value = {"tasks", "task"}, key = "#id", allEntries = true)
    public TaskResponseDTO updateStatus(Integer id, String statusString) {
        System.out.println(">>> updateStatus(" + id + ") - Hitting DB");
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        Status status;
        try {
            status = Status.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + statusString);
        }

        task.setStatus(status);
        taskRepo.save(task);
        return toDto(task);
    }
}