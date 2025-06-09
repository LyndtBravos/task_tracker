package com.sgs.tasktracker.controller;

import com.sgs.tasktracker.scheduler.OverdueTaskNotifier;
import com.sgs.tasktracker.scheduler.OverdueTaskScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private OverdueTaskNotifier notifier;

    @Autowired
    private OverdueTaskScheduler scheduler;

    @PostMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public String triggerOverdueEmails() {
        scheduler.markOverdueTasks();
        notifier.notifyOverdueTasks();
        return "Overdue notification check triggered.";
    }

    @PostMapping("/summary/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String triggerUserTaskSummary(@PathVariable Integer userId) {
        notifier.sendAllTasksToUser(userId);
        return "Summary sent (if tasks exist).";
    }
}