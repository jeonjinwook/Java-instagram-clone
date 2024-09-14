package com.Java_instagram_clone.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSender {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void send(String topic, String message) {
    System.out.println("Produce message : " + message);
    kafkaTemplate.send(topic, message);
  }
}
