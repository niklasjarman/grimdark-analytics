package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.request.CreateTournamentRequest;
import com.grimdark_analytics.grimdark_analytics.exception.ResourceNotFoundException;
import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentRepository.findById(id);
    }

    public Tournament getOrThrow(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament", id));
    }

    public List<Tournament> getTournamentsAfter(LocalDate date) {
        return tournamentRepository.findByDateAfter(date);
    }

    public List<Tournament> getTournamentsByMinPlayers(Integer minPlayers) {
        return tournamentRepository.findByPlayerCountGreaterThanEqual(minPlayers);
    }

    @Transactional
    public Tournament createTournament(CreateTournamentRequest req) {
        Tournament t = new Tournament();
        t.setName(req.name());
        t.setDate(req.date());
        t.setLocation(req.location());
        t.setFormat(req.format());
        t.setPlayerCount(req.playerCount());
        return tournamentRepository.save(t);
    }
}
