package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.MatchupStatsDto;
import com.grimdark_analytics.grimdark_analytics.dto.request.CreateMatchupRequest;
import com.grimdark_analytics.grimdark_analytics.model.Matchup;
import com.grimdark_analytics.grimdark_analytics.repository.MatchupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchupService {

    private final MatchupRepository matchupRepository;
    private final TournamentService tournamentService;
    private final PlayerService playerService;

    @Transactional
    public Matchup createMatchup(CreateMatchupRequest req) {
        Matchup m = new Matchup();
        m.setTournament(tournamentService.getOrThrow(req.tournamentId()));
        m.setRound(req.round());
        m.setPlayer1(playerService.getOrThrow(req.player1Id()));
        m.setPlayer2(playerService.getOrThrow(req.player2Id()));
        m.setWinner(req.winnerId() != null ? playerService.getOrThrow(req.winnerId()) : null);
        m.setPlayer1Faction(req.player1Faction());
        m.setPlayer2Faction(req.player2Faction());
        return matchupRepository.save(m);
    }

    @Transactional(readOnly = true)
    public MatchupStatsDto getMatchupStats(String faction1, String faction2) {
        List<Matchup> matchups = matchupRepository.findByFactions(faction1, faction2);

        long faction1Wins = 0, faction2Wins = 0, draws = 0;

        for (Matchup m : matchups) {
            if (m.getWinner() == null) {
                draws++;
            } else {
                boolean faction1IsPlayer1 = m.getPlayer1Faction().equals(faction1);
                boolean winnerIsPlayer1   = m.getWinner().getId().equals(m.getPlayer1().getId());
                boolean faction1Won = (faction1IsPlayer1 && winnerIsPlayer1)
                                   || (!faction1IsPlayer1 && !winnerIsPlayer1);
                if (faction1Won) faction1Wins++;
                else             faction2Wins++;
            }
        }

        long total = matchups.size();
        double winRate = total > 0 ? Math.round((double) faction1Wins / total * 10000.0) / 10000.0 : 0.0;

        return new MatchupStatsDto(faction1, faction2, faction1Wins, faction2Wins, draws, total, winRate);
    }
}
