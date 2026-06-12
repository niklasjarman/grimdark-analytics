package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.MatchupStatsDto;
import com.grimdark_analytics.grimdark_analytics.model.Matchup;
import com.grimdark_analytics.grimdark_analytics.model.Player;
import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import com.grimdark_analytics.grimdark_analytics.repository.MatchupRepository;
import org.junit.jupiter.api.BeforeEach;
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
class MatchupServiceTest {

    @Mock
    private MatchupRepository matchupRepository;

    @InjectMocks
    private MatchupService matchupService;

    private Player p1;
    private Player p2;
    private Tournament tournament;

    @BeforeEach
    void setUp() {
        p1 = new Player(1L, null, "Alice", "UK");
        p2 = new Player(2L, null, "Bob", "UK");
        tournament = new Tournament(10L, null, "London GT", LocalDate.of(2026, 3, 15), "London", "GT", 64);
    }

    private Matchup matchup(String f1, String f2, Player winner) {
        return new Matchup(null, tournament, 1, p1, p2, winner, f1, f2);
    }

    @Test
    void getMatchupStats_noMatchups() {
        when(matchupRepository.findByFactions("SM", "CSM")).thenReturn(List.of());

        MatchupStatsDto stats = matchupService.getMatchupStats("SM", "CSM");

        assertThat(stats.totalGames()).isZero();
        assertThat(stats.faction1WinRate()).isZero();
    }

    @Test
    void getMatchupStats_faction1WinsAll() {
        when(matchupRepository.findByFactions("SM", "CSM")).thenReturn(List.of(
                matchup("SM", "CSM", p1),
                matchup("SM", "CSM", p1)
        ));

        MatchupStatsDto stats = matchupService.getMatchupStats("SM", "CSM");

        assertThat(stats.faction1Wins()).isEqualTo(2);
        assertThat(stats.faction2Wins()).isZero();
        assertThat(stats.faction1WinRate()).isEqualTo(1.0);
    }

    @Test
    void getMatchupStats_draws() {
        when(matchupRepository.findByFactions("SM", "CSM")).thenReturn(List.of(
                matchup("SM", "CSM", null),
                matchup("SM", "CSM", null)
        ));

        MatchupStatsDto stats = matchupService.getMatchupStats("SM", "CSM");

        assertThat(stats.draws()).isEqualTo(2);
        assertThat(stats.faction1Wins()).isZero();
    }

    @Test
    void getMatchupStats_mixedResults() {
        when(matchupRepository.findByFactions("SM", "CSM")).thenReturn(List.of(
                matchup("SM",  "CSM", p1),  // SM wins (p1 is SM)
                matchup("CSM", "SM",  p1),  // p1 is CSM here, so CSM wins → faction2 wins
                matchup("SM",  "CSM", null) // draw
        ));

        MatchupStatsDto stats = matchupService.getMatchupStats("SM", "CSM");

        assertThat(stats.faction1Wins()).isEqualTo(1);
        assertThat(stats.faction2Wins()).isEqualTo(1);
        assertThat(stats.draws()).isEqualTo(1);
        assertThat(stats.totalGames()).isEqualTo(3);
    }

    @Test
    void getMatchupStats_faction1WinRateCalculation() {
        when(matchupRepository.findByFactions("SM", "CSM")).thenReturn(List.of(
                matchup("SM", "CSM", p1),
                matchup("SM", "CSM", p1),
                matchup("SM", "CSM", p2)
        ));

        MatchupStatsDto stats = matchupService.getMatchupStats("SM", "CSM");

        assertThat(stats.faction1WinRate()).isEqualTo(0.6667);
    }
}
