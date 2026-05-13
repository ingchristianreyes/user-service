package com.smartfinance.userservice.events;

import java.time.Instant;

public class UserMetricEvent {

    private String eventId;
    private String username;
    private String endpoint;
    private String eventType;
    private Instant occurredAt;

    public UserMetricEvent() {
    }

    public UserMetricEvent(String eventId, String username, String endpoint, String eventType, Instant occurredAt) {
        this.eventId = eventId;
        this.username = username;
        this.endpoint = endpoint;
        this.eventType = eventType;
        this.occurredAt = occurredAt;
    }

    public String getEventId() {
        return eventId;
    }

    public String getUsername() {
        return username;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}