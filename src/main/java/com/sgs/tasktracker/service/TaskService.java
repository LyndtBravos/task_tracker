package com.sgs.tasktracker.service;

import com.sgs.tasktracker.dto.TaskRequestDTO;
import com.sgs.tasktracker.dto.TaskResponseDTO;
import com.sgs.tasktracker.entity.Task;
import com.sgs.tasktracker.entity.Task.Status;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskResponseDTO toDto(Task task);
    TaskResponseDTO createTask(TaskRequestDTO task);
    TaskResponseDTO getTaskById(Integer id);
    List<TaskResponseDTO> getAllTasks();
    TaskResponseDTO updateTask(Integer id, TaskRequestDTO task);
    void deleteTask(Integer id);
    TaskResponseDTO updateStatus(Integer id, String status);

    List<TaskResponseDTO> getTasksByStatus(Status status);
    List<TaskResponseDTO> getTasksByDueDateBefore(LocalDate date);
    List<TaskResponseDTO> getTasksByAssignedUserId(Integer userId);
}
