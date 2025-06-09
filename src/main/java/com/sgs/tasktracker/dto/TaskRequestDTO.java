package com.sgs.tasktracker.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String status;

    @FutureOrPresent(message = "Due date must be today or set in the future.")
    private LocalDate dueDate;
    private Long assignedUserId;

    public @NotBlank(message = "Title is required") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") String title) {
        this.title = title;
    }

    public TaskRequestDTO(String title, String description, String status, LocalDate dueDate, Long assignedUserId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.assignedUserId = assignedUserId;
    }

    public TaskRequestDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status.toUpperCase();
    }

    public void setStatus(String status) {
        this.status = status.toUpperCase();
    }

    public @FutureOrPresent(message = "Due date must be today or set in the future.") LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(@FutureOrPresent(message = "Due date must be today or set in the future.") LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }
}
