package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.PlayerHistoryDto;
import com.grimdark_analytics.grimdark_analytics.dto.TournamentResultSummaryDto;
import com.grimdark_analytics.grimdark_analytics.dto.request.CreatePlayerRequest;
import com.grimdark_analytics.grimdark_analytics.exception.ResourceNotFoundException;
import com.grimdark_analytics.grimdark_analytics.model.Player;
import com.grimdark_analytics.grimdark_analytics.model.TournamentResult;
import com.grimdark_analytics.grimdark_analytics.repository.PlayerRepository;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TournamentResultRepository tournamentResultRepository;

    @Transactional
    public Player createPlayer(CreatePlayerRequest req) {
        Player p = new Player();
        p.setBcpPlayerId(req.bcpPlayerId());
        p.setName(req.name());
        p.setRegion(req.region());
        return playerRepository.save(p);
    }

    @Transactional
    public Player findOrCreateByBcpId(String bcpId, String name, String region) {
        return playerRepository.findByBcpPlayerId(bcpId).orElseGet(() -> {
            Player p = new Player();
            p.setBcpPlayerId(bcpId);
            p.setName(name);
            p.setRegion(region);
            return playerRepository.save(p);
        });
    }

    public Player getOrThrow(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player", id));
    }

    @Transactional(readOnly = true)
    public Optional<PlayerHistoryDto> getPlayerHistory(Long id) {
        return playerRepository.findById(id).map(player -> {
            List<TournamentResult> results = tournamentResultRepository.findByPlayer(player);

            int totalWins   = results.stream().mapToInt(r -> r.getWins()   != null ? r.getWins()   : 0).sum();
            int totalLosses = results.stream().mapToInt(r -> r.getLosses() != null ? r.getLosses() : 0).sum();
            int totalDraws  = results.stream().mapToInt(r -> r.getDraws()  != null ? r.getDraws()  : 0).sum();
            int total = totalWins + totalLosses + totalDraws;
            double winRate = total > 0 ? Math.round((double) totalWins / total * 10000.0) / 10000.0 : 0.0;

            List<TournamentResultSummaryDto> summaries = results.stream()
                    .map(r -> new TournamentResultSummaryDto(
                            r.getTournament().getId(),
                            r.getTournament().getName(),
                            r.getTournament().getDate(),
                            r.getTournament().getFormat(),
                            r.getFaction(),
                            r.getPlacement() != null ? r.getPlacement() : 0,
                            r.getWins()      != null ? r.getWins()      : 0,
                            r.getLosses()    != null ? r.getLosses()    : 0,
                            r.getDraws()     != null ? r.getDraws()     : 0
                    ))
                    .toList();

            return new PlayerHistoryDto(
                    player.getId(), player.getBcpPlayerId(), player.getName(), player.getRegion(),
                    totalWins, totalLosses, totalDraws, winRate, summaries
            );
        });
    }
}
