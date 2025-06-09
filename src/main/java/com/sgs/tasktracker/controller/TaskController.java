package com.sgs.tasktracker.controller;

import com.sgs.tasktracker.dto.TaskRequestDTO;
import com.sgs.tasktracker.dto.TaskResponseDTO;
import com.sgs.tasktracker.entity.Task.Status;
import com.sgs.tasktracker.scheduler.OverdueTaskScheduler;
import com.sgs.tasktracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task Management API endpoints")
public class TaskController {

    @Autowired
    private TaskService service;

    @Autowired
    private OverdueTaskScheduler scheduler;

    @PostMapping
    @Operation(summary = "Creates a new tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid TaskRequestDTO task) {
        return new ResponseEntity<>(service.createTask(task), HttpStatus.CREATED);
    }

    @GetMapping("/hello")
    @Operation(summary = "Get a task by using a task id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> checkHealth() {
        return new ResponseEntity<>("Hello, World!", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by using a task id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getTaskById(id), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Gets all tasks logged")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@RequestParam(required = false) String status,
                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
                                                  @RequestParam(required = false) Integer assignedUserId) {

        if(status != null)
            return new ResponseEntity<>(service.getTasksByStatus(Status.valueOf(status.toUpperCase())), HttpStatus.OK);
        else if(dueDate != null)
            return new ResponseEntity<>(service.getTasksByDueDateBefore(dueDate), HttpStatus.OK);
        else if(assignedUserId != null)
            return new ResponseEntity<>(service.getTasksByAssignedUserId(assignedUserId), HttpStatus.OK);
        else return new ResponseEntity<>(service.getAllTasks(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Integer id, @RequestBody @Valid TaskRequestDTO task) {
        return new ResponseEntity<>(service.updateTask(id, task), HttpStatus.OK);
    }

    @PutMapping("/{id}/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponseDTO> updateStatus(
            @PathVariable Integer id,
            @PathVariable String status
    ) {
        TaskResponseDTO updated = service.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task, using task id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/trigger-overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> triggerOverdueNow() {
        scheduler.markOverdueTasks();
        return new ResponseEntity<>("Manual overdue check complete.", HttpStatus.OK);
    }
}