package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.PlayerHistoryDto;
import com.grimdark_analytics.grimdark_analytics.model.Player;
import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import com.grimdark_analytics.grimdark_analytics.model.TournamentResult;
import com.grimdark_analytics.grimdark_analytics.repository.PlayerRepository;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TournamentResultRepository tournamentResultRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player player;
    private Tournament tournament;

    @BeforeEach
    void setUp() {
        player = new Player(1L, "BCP-001", "Niklas Jarman", "UK");
        tournament = new Tournament(10L, null, "London GT", LocalDate.of(2026, 3, 15), "London", "GT", 128);
    }

    @Test
    void getPlayerHistory_notFound() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<PlayerHistoryDto> result = playerService.getPlayerHistory(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void getPlayerHistory_returnsPlayerWithResults() {
        TournamentResult result = new TournamentResult(1L, tournament, player, "Space Marines", 3, 4, 1, 0);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(tournamentResultRepository.findByPlayer(player)).thenReturn(List.of(result));

        Optional<PlayerHistoryDto> dto = playerService.getPlayerHistory(1L);

        assertThat(dto).isPresent();
        assertThat(dto.get().name()).isEqualTo("Niklas Jarman");
        assertThat(dto.get().results()).hasSize(1);
        assertThat(dto.get().results().get(0).faction()).isEqualTo("Space Marines");
    }

    @Test
    void getPlayerHistory_calculatesWinRate() {
        TournamentResult r1 = new TournamentResult(1L, tournament, player, "Space Marines", 1, 5, 0, 0);
        TournamentResult r2 = new TournamentResult(2L, tournament, player, "Space Marines", 2, 3, 2, 0);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(tournamentResultRepository.findByPlayer(player)).thenReturn(List.of(r1, r2));

        PlayerHistoryDto dto = playerService.getPlayerHistory(1L).orElseThrow();

        assertThat(dto.totalWins()).isEqualTo(8);
        assertThat(dto.totalLosses()).isEqualTo(2);
        assertThat(dto.overallWinRate()).isEqualTo(0.8);
    }

    @Test
    void getPlayerHistory_nullFieldsDefaultToZero() {
        TournamentResult result = new TournamentResult(1L, tournament, player, "Tyranids", null, null, null, null);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(tournamentResultRepository.findByPlayer(player)).thenReturn(List.of(result));

        PlayerHistoryDto dto = playerService.getPlayerHistory(1L).orElseThrow();

        assertThat(dto.totalWins()).isZero();
        assertThat(dto.overallWinRate()).isZero();
    }
}
