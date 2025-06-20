package com.sgs.tasktracker.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;

    public AuthResponse() {}

    public AuthResponse(String token) { this.token = token; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
