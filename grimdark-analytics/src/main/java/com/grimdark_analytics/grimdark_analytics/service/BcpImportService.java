package com.grimdark_analytics.grimdark_analytics.service;

import com.grimdark_analytics.grimdark_analytics.dto.bcp.BcpEventResponse;
import com.grimdark_analytics.grimdark_analytics.dto.bcp.BcpPagedResponse;
import com.grimdark_analytics.grimdark_analytics.dto.bcp.BcpPairingResponse;
import com.grimdark_analytics.grimdark_analytics.dto.bcp.BcpPlayerResponse;
import com.grimdark_analytics.grimdark_analytics.model.Matchup;
import com.grimdark_analytics.grimdark_analytics.model.Player;
import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import com.grimdark_analytics.grimdark_analytics.model.TournamentResult;
import com.grimdark_analytics.grimdark_analytics.repository.MatchupRepository;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentRepository;
import com.grimdark_analytics.grimdark_analytics.repository.TournamentResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BcpImportService {

    private static final String BCP_BASE = "https://api.bestcoastpairings.com";

    private final TournamentRepository tournamentRepository;
    private final TournamentResultRepository tournamentResultRepository;
    private final MatchupRepository matchupRepository;
    private final PlayerService playerService;

    private final RestClient restClient = RestClient.create();

    @Transactional
    public Tournament importEvent(String bcpEventId) {
        log.info("Importing BCP event: {}", bcpEventId);

        BcpEventResponse event = fetchEvent(bcpEventId);
        Tournament tournament = upsertTournament(bcpEventId, event);

        List<BcpPlayerResponse> bcpPlayers = fetchPlayers(bcpEventId);
        log.info("Found {} players for event {}", bcpPlayers.size(), bcpEventId);

        // bcpId → our Player entity
        Map<String, Player> playerMap = new HashMap<>();
        for (BcpPlayerResponse bp : bcpPlayers) {
            Player p = playerService.findOrCreateByBcpId(bp.id(), bp.name(), null);
            playerMap.put(bp.id(), p);
            upsertResult(tournament, p, bp);
        }

        // Import each round's pairings
        for (int round = 1; round <= event.numberOfRounds(); round++) {
            List<BcpPairingResponse> pairings = fetchPairings(bcpEventId, round);
            log.info("Round {}: {} pairings", round, pairings.size());
            for (BcpPairingResponse pairing : pairings) {
                importPairing(tournament, pairing, playerMap, round);
            }
        }

        log.info("Import complete for event {} (tournament id {})", bcpEventId, tournament.getId());
        return tournament;
    }

    private Tournament upsertTournament(String bcpEventId, BcpEventResponse event) {
        return tournamentRepository.findByBcpEventId(bcpEventId).orElseGet(() -> {
            Tournament t = new Tournament();
            t.setBcpEventId(bcpEventId);
            t.setName(event.name());
            t.setDate(parseDate(event.datePlayed()));
            t.setLocation(buildLocation(event));
            t.setFormat("GT");
            return tournamentRepository.save(t);
        });
    }

    private void upsertResult(Tournament tournament, Player player, BcpPlayerResponse bp) {
        boolean exists = tournamentResultRepository
                .findByTournamentAndPlayer(tournament, player).isPresent();
        if (exists) return;

        TournamentResult r = new TournamentResult();
        r.setTournament(tournament);
        r.setPlayer(player);
        r.setFaction(normaliseFaction(bp.armyName()));
        r.setPlacement(bp.placing());
        r.setWins(bp.wins()   != null ? bp.wins()   : 0);
        r.setLosses(bp.losses() != null ? bp.losses() : 0);
        r.setDraws(bp.draws()  != null ? bp.draws()  : 0);
        tournamentResultRepository.save(r);
    }

    private void importPairing(Tournament tournament, BcpPairingResponse pairing,
                               Map<String, Player> playerMap, int round) {
        Player p1 = playerMap.get(pairing.player1Id());
        Player p2 = playerMap.get(pairing.player2Id());
        if (p1 == null || p2 == null) return;

        Player winner = pairing.winnerId() != null ? playerMap.get(pairing.winnerId()) : null;

        String f1 = factionFor(tournament, p1);
        String f2 = factionFor(tournament, p2);

        Matchup m = new Matchup();
        m.setTournament(tournament);
        m.setRound(round);
        m.setPlayer1(p1);
        m.setPlayer2(p2);
        m.setWinner(winner);
        m.setPlayer1Faction(f1 != null ? f1 : "Unknown");
        m.setPlayer2Faction(f2 != null ? f2 : "Unknown");
        matchupRepository.save(m);
    }

    // ── HTTP helpers ───────────────────────────────────────────────────────────

    private BcpEventResponse fetchEvent(String eventId) {
        return restClient.get()
                .uri(BCP_BASE + "/events/{id}", eventId)
                .retrieve()
                .body(BcpEventResponse.class);
    }

    private List<BcpPlayerResponse> fetchPlayers(String eventId) {
        BcpPagedResponse<BcpPlayerResponse> resp = restClient.get()
                .uri(BCP_BASE + "/players?eventId={id}&limit=500", eventId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return resp != null && resp.data() != null ? resp.data() : List.of();
    }

    private List<BcpPairingResponse> fetchPairings(String eventId, int round) {
        BcpPagedResponse<BcpPairingResponse> resp = restClient.get()
                .uri(BCP_BASE + "/pairings?eventId={id}&roundNumber={r}&limit=500", eventId, round)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return resp != null && resp.data() != null ? resp.data() : List.of();
    }

    // ── Utility ────────────────────────────────────────────────────────────────

    private String factionFor(Tournament tournament, Player player) {
        return tournamentResultRepository.findByTournamentAndPlayer(tournament, player)
                .map(TournamentResult::getFaction)
                .orElse(null);
    }

    private LocalDate parseDate(String raw) {
        if (raw == null) return LocalDate.now();
        try {
            return LocalDate.parse(raw.substring(0, 10));
        } catch (DateTimeParseException e) {
            log.warn("Could not parse date '{}', defaulting to today", raw);
            return LocalDate.now();
        }
    }

    private String buildLocation(BcpEventResponse event) {
        StringBuilder sb = new StringBuilder();
        if (event.city()    != null) sb.append(event.city());
        if (event.state()   != null) sb.append(", ").append(event.state());
        if (event.country() != null) sb.append(", ").append(event.country());
        return sb.isEmpty() ? null : sb.toString();
    }

    private String normaliseFaction(String armyName) {
        if (armyName == null) return "Unknown";
        String lower = armyName.toLowerCase();
        if (lower.contains("space marine") || lower.contains("ultramar"))  return "Space Marines";
        if (lower.contains("blood angel"))                                  return "Blood Angels";
        if (lower.contains("dark angel"))                                   return "Dark Angels";
        if (lower.contains("tyranid") || lower.contains("hive fleet"))     return "Tyranids";
        if (lower.contains("necron"))                                       return "Necrons";
        if (lower.contains("aeldari") || lower.contains("eldar"))          return "Aeldari";
        if (lower.contains("tau") || lower.contains("t'au"))               return "T'au Empire";
        if (lower.contains("chaos space marine"))                           return "Chaos Space Marines";
        if (lower.contains("death guard"))                                  return "Death Guard";
        if (lower.contains("world eater"))                                  return "World Eaters";
        if (lower.contains("ork"))                                          return "Orks";
        if (lower.contains("mechanicus") || lower.contains("adeptus mech")) return "Adeptus Mechanicus";
        if (lower.contains("astra militarum") || lower.contains("guard"))  return "Astra Militarum";
        if (lower.contains("thousand son"))                                 return "Thousand Sons";
        if (lower.contains("emperor's children"))                           return "Emperor's Children";
        return armyName; // keep as-is if no match
    }
}
