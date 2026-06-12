package com.grimdark_analytics.grimdark_analytics.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreatePlayerRequest(
        String bcpPlayerId,
        @NotBlank String name,
        String region
) {}
