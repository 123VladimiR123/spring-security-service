package com.springsecurityservice.springsecurityservice.kafka;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaSender {

    @Value("${custom.kafka.topic.response}")
    private String topicResponse;

    private final KafkaTemplate<String, String> template;

    public void sendResponse(String msg) {
        template.send(topicResponse, msg);
    }
}
