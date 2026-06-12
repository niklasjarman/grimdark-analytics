package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.FactionWinRateDto;
import com.grimdark_analytics.grimdark_analytics.dto.MetaSnapshotDto;
import com.grimdark_analytics.grimdark_analytics.repository.MatchupRepository;
import com.grimdark_analytics.grimdark_analytics.repository.PlayerRepository;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetaServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MatchupRepository matchupRepository;

    @Mock
    private FactionService factionService;

    @InjectMocks
    private MetaService metaService;

    @Test
    void getSnapshot_returnsCorrectCounts() {
        when(tournamentRepository.count()).thenReturn(10L);
        when(playerRepository.count()).thenReturn(200L);
        when(matchupRepository.count()).thenReturn(500L);
        when(factionService.getAllFactionWinRates()).thenReturn(List.of());

        MetaSnapshotDto snapshot = metaService.getSnapshot();

        assertThat(snapshot.totalTournaments()).isEqualTo(10L);
        assertThat(snapshot.totalPlayers()).isEqualTo(200L);
        assertThat(snapshot.totalMatchups()).isEqualTo(500L);
    }

    @Test
    void getSnapshot_topFactionsByWinRate() {
        List<FactionWinRateDto> rates = List.of(
                FactionWinRateDto.of("Space Marines", 60, 30, 10),
                FactionWinRateDto.of("Tyranids",      50, 40, 10),
                FactionWinRateDto.of("Chaos",         30, 60, 10)
        );
        when(tournamentRepository.count()).thenReturn(5L);
        when(playerRepository.count()).thenReturn(50L);
        when(matchupRepository.count()).thenReturn(100L);
        when(factionService.getAllFactionWinRates()).thenReturn(rates);

        MetaSnapshotDto snapshot = metaService.getSnapshot();

        assertThat(snapshot.topFactionsByWinRate()).hasSize(3);
        assertThat(snapshot.topFactionsByWinRate().get(0).faction()).isEqualTo("Space Marines");
        assertThat(snapshot.mostPlayedFaction()).isEqualTo("Space Marines");
    }

    @Test
    void getSnapshot_excludesFactionsWithFewGames() {
        List<FactionWinRateDto> rates = List.of(
                FactionWinRateDto.of("Space Marines", 60, 30, 10),
                FactionWinRateDto.of("Rare Faction",  3,  0,  0)  // only 3 games, filtered out of top
        );
        when(tournamentRepository.count()).thenReturn(5L);
        when(playerRepository.count()).thenReturn(50L);
        when(matchupRepository.count()).thenReturn(100L);
        when(factionService.getAllFactionWinRates()).thenReturn(rates);

        MetaSnapshotDto snapshot = metaService.getSnapshot();

        assertThat(snapshot.topFactionsByWinRate()).hasSize(1);
        assertThat(snapshot.topFactionsByWinRate().get(0).faction()).isEqualTo("Space Marines");
    }

    @Test
    void getSnapshot_mostPlayedIsNA_whenNoFactions() {
        when(tournamentRepository.count()).thenReturn(0L);
        when(playerRepository.count()).thenReturn(0L);
        when(matchupRepository.count()).thenReturn(0L);
        when(factionService.getAllFactionWinRates()).thenReturn(List.of());

        MetaSnapshotDto snapshot = metaService.getSnapshot();

        assertThat(snapshot.mostPlayedFaction()).isEqualTo("N/A");
        assertThat(snapshot.topFactionsByWinRate()).isEmpty();
    }
}
