package com.Java_instagram_clone.kafka;

import com.Java_instagram_clone.component.WebSocketHandler;
import com.Java_instagram_clone.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
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
  private final MemberRepository memberRepository;

  @KafkaListener(topics = "feed_notifications", groupId = "feed_notification_group")
  @Transactional
  public void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    try {
      String notificationMessage = record.value();

      String userId = record.key();

      try {
        webSocketHandler.sendMessageToUser(String.valueOf(userId), notificationMessage);
      } catch (IOException e) {
        log.error("WebSocket ERROR{} ", e.getMessage());
        return;
      }

      acknowledgment.acknowledge();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    }
  }
}
