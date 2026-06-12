package com.grimdark_analytics.grimdark_analytics.dto;

import java.time.LocalDate;

public record TournamentResultSummaryDto(
        Long tournamentId,
        String tournamentName,
        LocalDate tournamentDate,
        String tournamentFormat,
        String faction,
        int placement,
        int wins,
        int losses,
        int draws
) {}
