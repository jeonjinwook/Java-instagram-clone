package com.Java_instagram_clone.kafka;

import com.Java_instagram_clone.component.WebSocketHandler;
import com.Java_instagram_clone.domain.member.entity.ResponseMember;
import com.Java_instagram_clone.domain.member.repository.MemberRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedConsumer {

  private final WebSocketHandler webSocketHandler;
  private final MemberRepository memberRepository;

  @KafkaListener(topics = "feed_notifications", groupId = "feed_notification_group")
  public void listen(ConsumerRecord<String, String> record) {
    String notificationMessage = record.value();
    String userId = "1";

    List<ResponseMember> follows = memberRepository.findFollowerAllByUserId(Long.valueOf(userId));
    if (follows != null) {

      for (ResponseMember follow : follows) {

        try {
          webSocketHandler.sendMessageToUser(String.valueOf(follow.getId()), notificationMessage);
        } catch (IOException e) {
          log.error("WebSocket ERROR{} ", e.getMessage());
        }

      }
    }
  }
}
