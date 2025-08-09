package com.springboot.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailHelper {

    private JavaMailSenderImpl mailSender;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        this.mailSender = new JavaMailSenderImpl();
        this.mailSender.setHost("smtp.hostinger.com");
        this.mailSender.setPort(587);
        this.mailSender.setUsername("support@nimbusengineering.co.bw");
        this.mailSender.setPassword("12Support09*#&$");
        Properties props = this.mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        this.mailSender.send(email);
    }
}
