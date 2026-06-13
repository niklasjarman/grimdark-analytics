package com.grimdark_analytics.grimdark_analytics.dto;

public record TournamentResultEntryDto(
        Long playerId,
        String playerName,
        String faction,
        int placement,
        int wins,
        int losses,
        int draws
) {}
