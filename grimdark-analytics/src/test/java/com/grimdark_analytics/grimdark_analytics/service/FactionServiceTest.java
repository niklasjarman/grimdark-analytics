package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.FactionTrendDto;
import com.grimdark_analytics.grimdark_analytics.dto.FactionWinRateDto;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FactionServiceTest {

    @Mock
    private TournamentResultRepository tournamentResultRepository;

    @InjectMocks
    private FactionService factionService;

    @Test
    void getAllFactionWinRates_returnsAggregatedData() {
        when(tournamentResultRepository.aggregateByFaction()).thenReturn(List.of(
                new Object[]{"Space Marines", 60L, 30L, 10L},
                new Object[]{"Tyranids", 40L, 50L, 10L}
        ));

        List<FactionWinRateDto> result = factionService.getAllFactionWinRates();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).faction()).isEqualTo("Space Marines");
        assertThat(result.get(0).totalGames()).isEqualTo(100L);
        assertThat(result.get(0).winRate()).isEqualTo(0.6);
    }

    @Test
    void getAllFactionWinRates_emptyWhenNoData() {
        when(tournamentResultRepository.aggregateByFaction()).thenReturn(List.of());

        assertThat(factionService.getAllFactionWinRates()).isEmpty();
    }

    @Test
    void getFactionTrend_returnsDataPoints() {
        LocalDate d1 = LocalDate.of(2026, 1, 10);
        LocalDate d2 = LocalDate.of(2026, 3, 15);
        when(tournamentResultRepository.aggregateByFactionAndDate("Space Marines")).thenReturn(List.of(
                new Object[]{d1, 10L, 5L, 0L},
                new Object[]{d2, 8L, 2L, 0L}
        ));

        FactionTrendDto trend = factionService.getFactionTrend("Space Marines");

        assertThat(trend.faction()).isEqualTo("Space Marines");
        assertThat(trend.dataPoints()).hasSize(2);
        assertThat(trend.dataPoints().get(0).date()).isEqualTo(d1);
        assertThat(trend.dataPoints().get(0).winRate()).isEqualTo(0.6667);
        assertThat(trend.dataPoints().get(1).winRate()).isEqualTo(0.8);
    }

    @Test
    void getFactionTrend_emptyWhenNoData() {
        when(tournamentResultRepository.aggregateByFactionAndDate("Unknown")).thenReturn(List.of());

        FactionTrendDto trend = factionService.getFactionTrend("Unknown");

        assertThat(trend.dataPoints()).isEmpty();
    }
}
