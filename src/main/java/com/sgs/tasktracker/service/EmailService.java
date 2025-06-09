package com.sgs.tasktracker.service;

import com.sgs.tasktracker.repository.TaskRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TaskRepository taskRepo;

    public void sendEmail(String to, String subject, String body, String... cc) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);

            if(cc != null && cc.length > 0)
                helper.setCc(cc);

            mailSender.send(message);

        }catch(MessagingException e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }
}
