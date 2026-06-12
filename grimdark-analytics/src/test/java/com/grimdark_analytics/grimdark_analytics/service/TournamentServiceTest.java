package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentRepository;
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
class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentService tournamentService;

    private Tournament sampleTournament;

    @BeforeEach
    void setUp() {
        sampleTournament = new Tournament(1L, null, "London GT", LocalDate.of(2026, 3, 15), "London", "GT", 128);
    }

    @Test
    void getAllTournaments_returnsList() {
        when(tournamentRepository.findAll()).thenReturn(List.of(sampleTournament));

        List<Tournament> result = tournamentService.getAllTournaments();

        assertThat(result).hasSize(1).contains(sampleTournament);
    }

    @Test
    void getTournamentById_found() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(sampleTournament));

        Optional<Tournament> result = tournamentService.getTournamentById(1L);

        assertThat(result).isPresent().contains(sampleTournament);
    }

    @Test
    void getTournamentById_notFound() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Tournament> result = tournamentService.getTournamentById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void getTournamentsAfter_returnsFilteredList() {
        LocalDate cutoff = LocalDate.of(2026, 1, 1);
        when(tournamentRepository.findByDateAfter(cutoff)).thenReturn(List.of(sampleTournament));

        List<Tournament> result = tournamentService.getTournamentsAfter(cutoff);

        assertThat(result).hasSize(1).contains(sampleTournament);
    }

    @Test
    void getTournamentsByMinPlayers_returnsFilteredList() {
        when(tournamentRepository.findByPlayerCountGreaterThanEqual(100)).thenReturn(List.of(sampleTournament));

        List<Tournament> result = tournamentService.getTournamentsByMinPlayers(100);

        assertThat(result).hasSize(1).contains(sampleTournament);
    }
}
