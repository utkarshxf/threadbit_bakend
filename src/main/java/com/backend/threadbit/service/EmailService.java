package com.backend.threadbit.service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Service for sending emails
 * Uses Spring Mail to send emails via SMTP
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String appPassword;

    /**
     * Sends a simple text email
     * 
     * @param to recipient email address
     * @param subject email subject
     * @param body email body (plain text)
     */
    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            log.info("Sending email to: {}", to);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);

        }
    }

    /**
     * Sends an HTML email
     * 
     * @param to recipient email address
     * @param subject email subject
     * @param htmlBody email body (HTML)
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            log.info("Sending HTML email to: {}", to);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indicates HTML

            mailSender.send(message);

            log.info("HTML email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to: {}", to, e);
            // In a production environment, you might want to handle this exception differently
            // For now, we'll just log it and continue execution
        }
    }

    /**
     * Creates a mail session with Gmail SMTP using app password authentication
     * 
     * @return Session configured for Gmail SMTP with app password authentication
     */
    private Session createMailSession() {
        // Set mail properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create session with authenticator that uses app password
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });
    }

    /**
     * Sends an email using direct Jakarta Mail API with app password
     * This method demonstrates how to use Jakarta Mail API directly instead of through Spring's JavaMailSender
     * 
     * @param to recipient email address
     * @param subject email subject
     * @param body email body (plain text)
     */
    public void sendEmailWithDirectApi(String to, String subject, String body) {
        try {
            log.info("Sending email using direct Jakarta Mail API to: {}", to);

            // Get mail session
            Session session = createMailSession();

            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Send message
            Transport.send(message);

            log.info("Email sent successfully using direct Jakarta Mail API to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email using direct Jakarta Mail API to: {}", to, e);
            // In a production environment, you might want to handle this exception differently
            // For now, we'll just log it and continue execution
        }
    }

    /**
     * Sends an HTML email using direct Jakarta Mail API with app password
     * This method demonstrates how to use Jakarta Mail API directly instead of through Spring's JavaMailSender
     * 
     * @param to recipient email address
     * @param subject email subject
     * @param htmlBody email body (HTML)
     */
    public void sendHtmlEmailWithDirectApi(String to, String subject, String htmlBody) {
        try {
            log.info("Sending HTML email using direct Jakarta Mail API to: {}", to);

            // Get mail session
            Session session = createMailSession();

            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);

            log.info("HTML email sent successfully using direct Jakarta Mail API to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send HTML email using direct Jakarta Mail API to: {}", to, e);
            // In a production environment, you might want to handle this exception differently
            // For now, we'll just log it and continue execution
        }
    }
}
