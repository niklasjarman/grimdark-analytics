package com.grimdark_analytics.grimdark_analytics.controller;

import com.grimdark_analytics.grimdark_analytics.dto.request.CreateMatchupRequest;
import com.grimdark_analytics.grimdark_analytics.dto.request.CreateTournamentRequest;
import com.grimdark_analytics.grimdark_analytics.dto.request.CreateTournamentResultRequest;
import com.grimdark_analytics.grimdark_analytics.model.Matchup;
import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import com.grimdark_analytics.grimdark_analytics.model.TournamentResult;
import com.grimdark_analytics.grimdark_analytics.service.MatchupService;
import com.grimdark_analytics.grimdark_analytics.service.TournamentResultService;
import com.grimdark_analytics.grimdark_analytics.service.TournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournaments", description = "Tournament management endpoints")
public class TournamentController {

    private final TournamentService tournamentService;
    private final TournamentResultService tournamentResultService;
    private final MatchupService matchupService;

    @GetMapping
    @Operation(summary = "List all tournaments", description = "Filter by ?after=YYYY-MM-DD or ?minPlayers=N")
    public List<Tournament> getAllTournaments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate after,
            @RequestParam(required = false) Integer minPlayers) {

        if (after != null)       return tournamentService.getTournamentsAfter(after);
        if (minPlayers != null)  return tournamentService.getTournamentsByMinPlayers(minPlayers);
        return tournamentService.getAllTournaments();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tournament by ID")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        return tournamentService.getTournamentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new tournament")
    public Tournament createTournament(@Valid @RequestBody CreateTournamentRequest req) {
        return tournamentService.createTournament(req);
    }

    @PostMapping("/{id}/results")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a player result to a tournament")
    public TournamentResult addResult(
            @PathVariable Long id,
            @Valid @RequestBody CreateTournamentResultRequest req) {
        return tournamentResultService.createResult(id, req);
    }

    @PostMapping("/{id}/matchups")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Record a matchup within a tournament")
    public Matchup addMatchup(
            @PathVariable Long id,
            @Valid @RequestBody CreateMatchupRequest req) {
        return matchupService.createMatchup(req);
    }
}
