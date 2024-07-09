package com.Java_instagram_clone;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {
    @KafkaListener(
            topics = "test-kafka",
            groupId = "kafka-group",
            clientIdPrefix = "kafka-client"
    )
    public void kafkaTestItemIncoming(String message) {
        System.out.println("Received Message : " + message);
    }
}
