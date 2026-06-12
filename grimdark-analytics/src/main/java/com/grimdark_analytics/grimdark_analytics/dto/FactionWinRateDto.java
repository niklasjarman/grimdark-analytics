package com.grimdark_analytics.grimdark_analytics.dto;

public record FactionWinRateDto(
        String faction,
        long wins,
        long losses,
        long draws,
        long totalGames,
        double winRate
) {
    public static FactionWinRateDto of(String faction, long wins, long losses, long draws) {
        long total = wins + losses + draws;
        double rate = total > 0 ? Math.round((double) wins / total * 10000.0) / 10000.0 : 0.0;
        return new FactionWinRateDto(faction, wins, losses, draws, total, rate);
    }
}
