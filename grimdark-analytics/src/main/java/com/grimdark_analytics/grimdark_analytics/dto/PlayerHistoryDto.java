package com.grimdark_analytics.grimdark_analytics.dto;

import java.util.List;

public record PlayerHistoryDto(
        Long id,
        String bcpPlayerId,
        String name,
        String region,
        int totalWins,
        int totalLosses,
        int totalDraws,
        double overallWinRate,
        List<TournamentResultSummaryDto> results
) {}
