package com.chrisimoni.libraryeventsproducer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.topic}")
    public String topic;
    @Value("${spring.kafka.new-event-topic}")
    public String newLibraryEventTopic;

    @Bean
    public NewTopic libraryEvents() {
        return TopicBuilder.name(topic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic newLibraryEvents() {
        return TopicBuilder.name(newLibraryEventTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
