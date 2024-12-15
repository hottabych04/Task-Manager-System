package com.hottabych04.app.service.jwt.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Token(UUID id, String login,
                    List<String> authorities,
                    Instant createdAt,
                    Instant expiredAt) {
}
