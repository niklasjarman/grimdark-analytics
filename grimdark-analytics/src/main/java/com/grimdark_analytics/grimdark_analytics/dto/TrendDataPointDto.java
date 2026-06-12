package com.grimdark_analytics.grimdark_analytics.dto;

import java.time.LocalDate;

public record TrendDataPointDto(LocalDate date, double winRate, long gamesPlayed) {}
