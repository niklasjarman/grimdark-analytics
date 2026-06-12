package com.grimdark_analytics.grimdark_analytics.dto;

import java.time.LocalDate;
import java.util.List;

public record MetaSnapshotDto(
        LocalDate generatedAt,
        long totalTournaments,
        long totalPlayers,
        long totalMatchups,
        List<FactionWinRateDto> topFactionsByWinRate,
        String mostPlayedFaction
) {}
