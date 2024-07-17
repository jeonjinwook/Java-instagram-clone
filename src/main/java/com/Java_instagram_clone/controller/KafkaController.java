package com.Java_instagram_clone.controller;

import com.Java_instagram_clone.config.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "kafka-logger")
@RestController
public class KafkaController {
    private final MessageSender messageSender;

    @Autowired
    public KafkaController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        messageSender.send("test-kafka", message);
        log.info("Send message : " + message);
        return "success";
    }
}
