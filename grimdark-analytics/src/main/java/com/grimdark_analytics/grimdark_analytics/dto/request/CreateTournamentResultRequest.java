package com.grimdark_analytics.grimdark_analytics.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTournamentResultRequest(
        @NotNull Long playerId,
        @NotBlank String faction,
        @Min(1)  Integer placement,
        @Min(0)  Integer wins,
        @Min(0)  Integer losses,
        @Min(0)  Integer draws
) {}
