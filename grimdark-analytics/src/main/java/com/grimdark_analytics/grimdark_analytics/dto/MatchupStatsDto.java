package com.grimdark_analytics.grimdark_analytics.dto;

public record MatchupStatsDto(
        String faction1,
        String faction2,
        long faction1Wins,
        long faction2Wins,
        long draws,
        long totalGames,
        double faction1WinRate
) {}
