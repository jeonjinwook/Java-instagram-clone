package com.Java_instagram_clone.domain.feed.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FeedConsumer {

    @KafkaListener(topics = "feed_notifications", groupId = "feed_notification_group")
    public void listen(ConsumerRecord<String, String> record) {
        String notification = record.value();
    }

}
