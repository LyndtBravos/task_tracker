package com.sgs.tasktracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Username is required.")
    private String username;

    @Email(message = "Invalid email.")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 10, message = "Password must at least be 10 characters long.")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;

    public UserRequestDTO() {}

    public UserRequestDTO(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public @NotBlank(message = "Username is required.") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required.") String username) {
        this.username = username;
    }

    public @Email(message = "Invalid email.") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Invalid email.") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password is required") @Size(min = 10, message = "Password must at least be 10 characters long.") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") @Size(min = 10, message = "Password must at least be 10 characters long.") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Role is required") String getRole() {
        return role.toUpperCase();
    }

    public void setRole(@NotBlank(message = "Role is required") String role) {
        this.role = role.toUpperCase();
    }
}