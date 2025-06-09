package com.sgs.tasktracker.dto;

import com.sgs.tasktracker.entity.Task;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TaskResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String description;
    private Task.Status status;
    private LocalDate dueDate;
    private LocalDate createdDate;
    private Long assignedUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task.Status getStatus() {
        return status;
    }

    public void setStatus(Task.Status status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public TaskResponseDTO() {}

    public TaskResponseDTO(Long id, String title, String description, Task.Status status, LocalDate dueDate, LocalDate createdDate, Long assignedUserId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.createdDate = createdDate;
        this.assignedUserId = assignedUserId;
    }
}