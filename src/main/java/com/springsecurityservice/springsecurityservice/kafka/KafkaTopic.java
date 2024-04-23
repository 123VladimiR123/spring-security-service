package com.springsecurityservice.springsecurityservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {
    @Value("${custom.kafka.topic.request}")
    private String topicRequest;

    @Value("${custom.kafka.topic.response}")
    private String topicResponse;

    @Bean
    public NewTopic topicRequest() {
        return TopicBuilder.name(topicRequest).build();
    }
    @Bean
    public NewTopic topicResponse() {
        return TopicBuilder.name(topicResponse).build();
    }

}
