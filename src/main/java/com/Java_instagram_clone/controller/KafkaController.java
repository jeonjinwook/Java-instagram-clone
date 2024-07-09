package com.Java_instagram_clone.controller;

import com.Java_instagram_clone.config.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    private final MessageSender messageSender;

    @Autowired
    public KafkaController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        //"test-kafka" 이 부분은 저번 실습에서 정한 Topic명으로
        messageSender.send("test-kafka", message);
        return "success";
    }
}
