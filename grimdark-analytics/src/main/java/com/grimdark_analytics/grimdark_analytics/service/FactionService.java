package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.FactionTrendDto;
import com.grimdark_analytics.grimdark_analytics.dto.FactionWinRateDto;
import com.grimdark_analytics.grimdark_analytics.dto.TrendDataPointDto;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FactionService {

    private final TournamentResultRepository tournamentResultRepository;

    public List<FactionWinRateDto> getAllFactionWinRates() {
        return tournamentResultRepository.aggregateByFaction().stream()
                .map(row -> FactionWinRateDto.of(
                        (String) row[0],
                        toLong(row[1]),
                        toLong(row[2]),
                        toLong(row[3])
                ))
                .toList();
    }

    public FactionTrendDto getFactionTrend(String faction) {
        List<TrendDataPointDto> dataPoints = tournamentResultRepository
                .aggregateByFactionAndDate(faction).stream()
                .map(row -> {
                    long wins   = toLong(row[1]);
                    long losses = toLong(row[2]);
                    long draws  = toLong(row[3]);
                    long total  = wins + losses + draws;
                    double rate = total > 0 ? Math.round((double) wins / total * 10000.0) / 10000.0 : 0.0;
                    return new TrendDataPointDto((LocalDate) row[0], rate, total);
                })
                .toList();

        return new FactionTrendDto(faction, dataPoints);
    }

    private long toLong(Object value) {
        return value instanceof Number n ? n.longValue() : 0L;
    }
}
