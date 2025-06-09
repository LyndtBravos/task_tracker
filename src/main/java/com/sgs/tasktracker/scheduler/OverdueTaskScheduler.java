package com.sgs.tasktracker.scheduler;

import com.sgs.tasktracker.entity.Task;
import com.sgs.tasktracker.entity.Task.Status;
import com.sgs.tasktracker.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OverdueTaskScheduler {

    @Autowired
    private TaskRepository taskRepo;

    @Scheduled(cron = "0 50 7 * * *")
    @Transactional
    @CacheEvict(value = {"tasks", "task"}, allEntries = true)
    public void markOverdueTasks() {
        String timeNow = String.join(" ", LocalDateTime.now().toString().split("T"));
        System.out.println("Running overdue task check at: " + timeNow.split("\\.")[0]);

        List<Task> tasks = taskRepo.findAll();

        for (Task task : tasks) {
            if (task.getDueDate() != null &&
                    task.getDueDate().isBefore(LocalDate.now()) &&
                    task.getStatus() != Status.OVERDUE) {

                task.setStatus(Status.OVERDUE);
                System.out.println("Task " + task.getId() + " marked as OVERDUE");
            }
        }

        taskRepo.saveAll(tasks);
    }
}
