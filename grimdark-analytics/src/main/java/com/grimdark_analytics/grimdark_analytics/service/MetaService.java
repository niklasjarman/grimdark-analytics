package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.FactionWinRateDto;
import com.grimdark_analytics.grimdark_analytics.dto.MetaSnapshotDto;
import com.grimdark_analytics.grimdark_analytics.repository.MatchupRepository;
import com.grimdark_analytics.grimdark_analytics.repository.PlayerRepository;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;
    private final MatchupRepository matchupRepository;
    private final FactionService factionService;

    public MetaSnapshotDto getSnapshot() {
        long totalTournaments = tournamentRepository.count();
        long totalPlayers     = playerRepository.count();
        long totalMatchups    = matchupRepository.count();

        List<FactionWinRateDto> allRates = factionService.getAllFactionWinRates();

        List<FactionWinRateDto> top5 = allRates.stream()
                .filter(f -> f.totalGames() >= 5)
                .sorted(Comparator.comparingDouble(FactionWinRateDto::winRate).reversed())
                .limit(5)
                .toList();

        String mostPlayed = allRates.stream()
                .max(Comparator.comparingLong(FactionWinRateDto::totalGames))
                .map(FactionWinRateDto::faction)
                .orElse("N/A");

        return new MetaSnapshotDto(LocalDate.now(), totalTournaments, totalPlayers, totalMatchups, top5, mostPlayed);
    }
}
