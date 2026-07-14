package com.smartfinance.userservice.messaging;

import com.smartfinance.userservice.events.UserMetricEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserMetricsProducer {
	
    private static final Logger log = LoggerFactory.getLogger(UserMetricsProducer.class);

    private final KafkaTemplate<String, UserMetricEvent> kafkaTemplate;
    private final String userMetricsTopic;

    public UserMetricsProducer(
            KafkaTemplate<String, UserMetricEvent> kafkaTemplate,
            @Value("${app.kafka.topics.user-metrics}") String userMetricsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.userMetricsTopic = userMetricsTopic;
    }

    public void publish(UserMetricEvent event) {
    	log.info("Publishing UserMetricEvent to topic {}: {}", userMetricsTopic, event);
    	
        kafkaTemplate.send(userMetricsTopic, event.getUsername(), event)
	        .whenComplete((result, ex) -> {
	            if (ex != null) {
	                log.error("Failed to publish UserMetricEvent to Kafka", ex);
	            } else {
	                log.info(
	                        "UserMetricEvent published successfully. topic={}, partition={}, offset={}",
	                        result.getRecordMetadata().topic(),
	                        result.getRecordMetadata().partition(),
	                        result.getRecordMetadata().offset()
	                );
	            }
	        });
    }
}