package com.sgs.tasktracker.scheduler;

import com.sgs.tasktracker.entity.Task;
import com.sgs.tasktracker.entity.User;
import com.sgs.tasktracker.repository.TaskRepository;
import com.sgs.tasktracker.repository.UserRepository;
import com.sgs.tasktracker.service.EmailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OverdueTaskNotifier {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepo;

    @Scheduled(cron = "0 0 8 * * ?")
    public void scheduledOverdueNotification() {
        System.out.println("Running the scheduled-overdue-task notifier.");
        notifyOverdueTasks();
    }

    public void notifyOverdueTasks() {
        List<Task> overdueTasks = taskRepo.findByDueDateBefore(LocalDate.now());

        if (overdueTasks.isEmpty()) {
            System.out.println("No overdue tasks today.");
            return;
        }

        Map<String, List<Task>> tasksByUserEmail = overdueTasks
                .stream()
                .collect(Collectors.groupingBy(task -> task.getAssignedUser().getEmail()));

        for (Map.Entry<String, List<Task>> entry : tasksByUserEmail.entrySet()) {
            String email = entry.getKey();
            List<Task> userTasks = entry.getValue();
            String username = userTasks.get(0).getAssignedUser().getUsername();

            StringBuilder bodyBuilder = new StringBuilder();
            bodyBuilder.append("Hi ").append(username).append(",\n\n");
            bodyBuilder.append("The following tasks assigned to you are overdue:\n\n");

            userTasks.forEach(task -> bodyBuilder
                    .append("- ")
                    .append(task.getTitle())
                    .append("-> Description (")
                    .append(task.getDescription())
                    .append(")\n- Due Date: ")
                    .append(task.getDueDate())
                    .append("\n- Date Created: ")
                    .append(task.getCreatedDate())
                    .append("\n"));

            bodyBuilder.append("\nPlease take the necessary action to complete them.\n\n");
            bodyBuilder.append("Many Thanks,\n- Task Tracker System");

            String subject = "Overdue Task Reminder - " + LocalDate.now();

            try {
                emailService.sendEmail(email, subject, bodyBuilder.toString(), "lindtbravos@gmail.com");
                System.out.println("Sent overdue reminder to: " + email);
            } catch (Exception e) {
                System.err.println("Failed to send email to " + email + ". Reason: " + e.getMessage());
            }
        }
    }

    public void sendAllTasksToUser(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Task> tasks = taskRepo.findByAssignedUser(user);

        if (tasks.isEmpty()) {
            System.out.println("User has no tasks.");
            return;
        }

        StringBuilder body = new StringBuilder("Hi %s,\n\nHere are all your assigned tasks:\n\n"
                .formatted(user.getUsername()));

        tasks.forEach(task -> body.append("-> [%s] %s (Due: %s)\n"
                .formatted(task.getStatus(), task.getTitle(), task.getDueDate().toString())));

        body.append("\nKind Regards,\nTask Tracker");

        try {
            emailService.sendEmail(user.getEmail(), "Your Task Summary", body.toString());
            System.out.println("Task Summary shared to " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send summary: " + e.getMessage());
        }
    }
}