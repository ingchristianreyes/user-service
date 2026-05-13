package com.smartfinance.userservice.controller;

import com.smartfinance.userservice.dto.MeResponse;
import com.smartfinance.userservice.events.UserMetricEvent;
import com.smartfinance.userservice.messaging.UserMetricsProducer;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserMetricsProducer userMetricsProducer;

    public UserController(UserMetricsProducer userMetricsProducer) {
        this.userMetricsProducer = userMetricsProducer;
    }

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        Set<String> authorities = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toSet());

        UserMetricEvent event = new UserMetricEvent(
                UUID.randomUUID().toString(),
                authentication.getName(),
                "/me",
                "USER_PROFILE_VIEWED",
                Instant.now()
        );

        userMetricsProducer.publish(event);

        return new MeResponse(
                authentication.getName(),
                authorities,
                authentication.isAuthenticated()
        );
    }
}