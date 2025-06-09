package com.sgs.tasktracker.repository;

import com.sgs.tasktracker.entity.Task;
import com.sgs.tasktracker.entity.User;
import com.sgs.tasktracker.entity.Task.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;
import java.util.List;

@RedisHash
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByStatus(Status status);
    List<Task> findByDueDateBefore(LocalDate date);
    List<Task> findByAssignedUser(User user);
    List<Task>findByStatusAndAssignedUser(Status status, User user);
    List<Task> findByStatusAndDueDateBefore(Status status, LocalDate date);

}