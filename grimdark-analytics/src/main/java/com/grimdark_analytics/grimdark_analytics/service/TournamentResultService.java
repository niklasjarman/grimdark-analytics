package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.TournamentResultEntryDto;
import com.grimdark_analytics.grimdark_analytics.dto.request.CreateTournamentResultRequest;
import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import com.grimdark_analytics.grimdark_analytics.model.TournamentResult;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentResultService {

    private final TournamentResultRepository tournamentResultRepository;
    private final TournamentService tournamentService;
    private final PlayerService playerService;

    public List<TournamentResultEntryDto> getResultsForTournament(Long tournamentId) {
        Tournament tournament = tournamentService.getOrThrow(tournamentId);
        return tournamentResultRepository.findByTournament(tournament).stream()
                .sorted(Comparator.comparingInt(TournamentResult::getPlacement))
                .map(r -> new TournamentResultEntryDto(
                        r.getPlayer().getId(),
                        r.getPlayer().getName(),
                        r.getFaction(),
                        r.getPlacement(),
                        r.getWins(),
                        r.getLosses(),
                        r.getDraws()
                ))
                .toList();
    }

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
