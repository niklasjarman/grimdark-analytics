package com.grimdark_analytics.grimdark_analytics.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMatchupRequest(
        @NotNull Long tournamentId,
        @Min(1)  Integer round,
        @NotNull Long player1Id,
        @NotNull Long player2Id,
        Long winnerId,
        @NotBlank String player1Faction,
        @NotBlank String player2Faction
) {}
