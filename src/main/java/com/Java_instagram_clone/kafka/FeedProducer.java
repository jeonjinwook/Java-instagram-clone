package com.Java_instagram_clone.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class FeedProducer {

  private static final String TOPIC = "feed_notifications";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void sendNotification(String message, String feedId) {
    kafkaTemplate.send(TOPIC, feedId, message);
  }
}
