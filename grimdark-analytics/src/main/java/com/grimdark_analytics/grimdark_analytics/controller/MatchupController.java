package com.grimdark_analytics.grimdark_analytics.controller;

import com.grimdark_analytics.grimdark_analytics.dto.MatchupStatsDto;
import com.grimdark_analytics.grimdark_analytics.service.MatchupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matchups")
@RequiredArgsConstructor
@Tag(name = "Matchups", description = "Head-to-head faction matchup statistics")
public class MatchupController {

    private final MatchupService matchupService;

    @GetMapping("/{faction1}/vs/{faction2}")
    @Operation(summary = "Get head-to-head statistics between two factions")
    public MatchupStatsDto getMatchupStats(
            @PathVariable String faction1,
            @PathVariable String faction2) {
        return matchupService.getMatchupStats(faction1, faction2);
    }
}
