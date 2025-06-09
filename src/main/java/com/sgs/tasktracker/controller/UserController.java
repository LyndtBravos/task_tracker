package com.sgs.tasktracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgs.tasktracker.dto.UserRequestDTO;
import com.sgs.tasktracker.dto.UserResponseDTO;
import com.sgs.tasktracker.dto.UserTaskDTO;
import com.sgs.tasktracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/redis-test")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> testRedis() {
        redisTemplate.opsForValue().set("health", "ok");
        String value = redisTemplate.opsForValue().get("health");
        return ResponseEntity.ok("Redis says: " + value);
    }

    @PostMapping
    @Operation(summary = "For creating a new user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO user) {
        return new ResponseEntity<>(service.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Brings back all users")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets user via user id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable int id) {
        return new ResponseEntity<>(service.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "For updating an existing user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer id, @RequestBody @Valid UserRequestDTO user) {
        return new ResponseEntity<>(service.updateUser(id, user), HttpStatus.OK);
    }

    @GetMapping("/{id}/tasks")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserTaskDTO> getUserWithTasks(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getUserWithTasks(id), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "For deleting an old user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/test-dto")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserTaskDTO test() {
        UserTaskDTO dto = new UserTaskDTO();
        dto.setId(1);
        dto.setUsername("gippy");
        dto.setEmail("test@example.com");
        dto.setRole("ADMIN");
        dto.setTasks(new ArrayList<>());
        return dto;
    }
}