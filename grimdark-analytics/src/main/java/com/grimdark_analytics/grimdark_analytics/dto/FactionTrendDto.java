package com.grimdark_analytics.grimdark_analytics.dto;

import java.util.List;

public record FactionTrendDto(String faction, List<TrendDataPointDto> dataPoints) {}
