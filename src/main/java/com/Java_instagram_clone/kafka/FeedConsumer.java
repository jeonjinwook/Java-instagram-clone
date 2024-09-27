package com.Java_instagram_clone.kafka;

import com.Java_instagram_clone.component.WebSocketHandler;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedConsumer {

  private final WebSocketHandler webSocketHandler;

  @KafkaListener(topics = "feed_notifications", groupId = "feed_notification_group")
  public void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    String notificationMessage = record.value();
    String userId = record.key();

    try {
      webSocketHandler.sendMessageToUser(userId, notificationMessage);
      acknowledgment.acknowledge();
    } catch (IOException e) {
      log.error("WebSocket 오류: {}", e.getMessage(), e);
    } catch (Exception e) {
      log.error("메시지 처리 중 오류 발생: {}", e.getMessage(), e);
      throw e;
    }
  }
}

