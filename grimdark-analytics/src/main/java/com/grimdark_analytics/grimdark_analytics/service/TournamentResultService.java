package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.request.CreateTournamentResultRequest;
import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import com.grimdark_analytics.grimdark_analytics.model.TournamentResult;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TournamentResultService {

    private final TournamentResultRepository tournamentResultRepository;
    private final TournamentService tournamentService;
    private final PlayerService playerService;

    @Transactional
    public TournamentResult createResult(Long tournamentId, CreateTournamentResultRequest req) {
        Tournament tournament = tournamentService.getOrThrow(tournamentId);

        TournamentResult result = new TournamentResult();
        result.setTournament(tournament);
        result.setPlayer(playerService.getOrThrow(req.playerId()));
        result.setFaction(req.faction());
        result.setPlacement(req.placement());
        result.setWins(req.wins());
        result.setLosses(req.losses());
        result.setDraws(req.draws());
        return tournamentResultRepository.save(result);
    }
}
