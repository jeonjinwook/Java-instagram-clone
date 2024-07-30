package com.Java_instagram_clone.domain.auth.controller;

import com.Java_instagram_clone.config.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class authController {
    private final MessageSender messageSender;

    @Autowired
    public authController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    @GetMapping("/emailValidation")
    public String emailValidation(@RequestParam String email) {

        messageSender.send("test-kafka", email);

        return "success";
    }
}
