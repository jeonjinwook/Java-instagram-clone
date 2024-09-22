package com.Java_instagram_clone.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedProducer {

  private static final String TOPIC = "feed_notifications";
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendNotification(String message, String userNo) {
    kafkaTemplate.send(TOPIC, userNo, message);
  }
}
