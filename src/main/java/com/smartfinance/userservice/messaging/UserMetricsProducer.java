package com.smartfinance.userservice.messaging;

import com.smartfinance.userservice.events.UserMetricEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserMetricsProducer {

    private final KafkaTemplate<String, UserMetricEvent> kafkaTemplate;
    private final String userMetricsTopic;

    public UserMetricsProducer(
            KafkaTemplate<String, UserMetricEvent> kafkaTemplate,
            @Value("${app.kafka.topics.user-metrics}") String userMetricsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.userMetricsTopic = userMetricsTopic;
    }

    public void publish(UserMetricEvent event) {
        kafkaTemplate.send(userMetricsTopic, event.getUsername(), event);
    }
}