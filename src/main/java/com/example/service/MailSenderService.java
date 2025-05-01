package com.example.service;

import com.example.utility.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${server.domain.name}")
    private String serverUrl;
   private final String subject="Kun uz verification";

    public void sendEmail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    public void sendEmailVerification(String to, String name, String id) {
        String jwt= JwtUtil.encode(id);
        String url= serverUrl+"/api/v1/auth/verification/email/"+jwt;
        String message = String.format("<h1 style=\"color:#ff0000\"> Hello%s </h1>", name) +
                " <p>" +
                "Click the link to verify your account!" +
                " </p>" +
                url;
        sendEmailMime(to, message);
        emailHistoryService.sendEmailHistory(to,jwt);
    }
    public void sendEmailMime(String to, String text) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {      try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(fromEmail);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }});
        executorService.shutdown();
    }
}
