package com.grimdark_analytics.grimdark_analytics.controller;

import com.grimdark_analytics.grimdark_analytics.dto.PlayerHistoryDto;
import com.grimdark_analytics.grimdark_analytics.dto.request.CreatePlayerRequest;
import com.grimdark_analytics.grimdark_analytics.model.Player;
import com.grimdark_analytics.grimdark_analytics.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
@Tag(name = "Players", description = "Player statistics and tournament history")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/{id}")
    @Operation(summary = "Get player by ID with full tournament history and win rate")
    public ResponseEntity<PlayerHistoryDto> getPlayer(@PathVariable Long id) {
        return playerService.getPlayerHistory(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new player")
    public Player createPlayer(@Valid @RequestBody CreatePlayerRequest req) {
        return playerService.createPlayer(req);
    }
}
