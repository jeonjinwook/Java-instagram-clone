package com.Java_instagram_clone.kafka;

import com.Java_instagram_clone.component.WebSocketHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FeedConsumer {

  private final WebSocketHandler webSocketHandler;

  public FeedConsumer(WebSocketHandler webSocketHandler) {
    this.webSocketHandler = webSocketHandler;
  }

  @KafkaListener(topics = "feed_notifications", groupId = "feed_notification_group")
  public void listen(ConsumerRecord<String, String> record) {
    String notificationMessage = record.value();
    String userId = record.key();

    try {
      webSocketHandler.sendMessageToUser(userId, notificationMessage);
    } catch (Exception e) {
    }
  }

}
