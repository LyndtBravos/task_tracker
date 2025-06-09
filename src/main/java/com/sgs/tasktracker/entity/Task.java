package com.sgs.tasktracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name ="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate dueDate;
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    public Task(String title, String description, Status status, LocalDate dueDate, LocalDate createdDate, User assignedUser) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.createdDate = createdDate;
        this.assignedUser = assignedUser;
    }

    public Task() {}

    public enum Status {
        NEW, IN_PROGRESS, DELAYED, OVERDUE, COMPLETED;
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }
}