package com.sgs.tasktracker.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String email;
    private String role;
    private List<TaskResponseDTO> tasks;

    public UserTaskDTO() {}

    public UserTaskDTO(Integer id, String username, String email, String role, List<TaskResponseDTO> tasks) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.tasks = tasks;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTasks(List<TaskResponseDTO> tasks) {
        this.tasks = tasks;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public List<TaskResponseDTO> getTasks() {
        return tasks;
    }
}
