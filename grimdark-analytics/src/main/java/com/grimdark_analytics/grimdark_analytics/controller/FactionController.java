package com.grimdark_analytics.grimdark_analytics.controller;

import com.grimdark_analytics.grimdark_analytics.dto.FactionTrendDto;
import com.grimdark_analytics.grimdark_analytics.dto.FactionWinRateDto;
import com.grimdark_analytics.grimdark_analytics.service.FactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factions")
@RequiredArgsConstructor
@Tag(name = "Factions", description = "Faction meta analytics")
public class FactionController {

    private final FactionService factionService;

    @GetMapping("/winrates")
    @Operation(summary = "Get aggregated win rates for all factions")
    public List<FactionWinRateDto> getWinRates() {
        return factionService.getAllFactionWinRates();
    }

    @GetMapping("/{faction}/trend")
    @Operation(summary = "Get win rate trend over time for a given faction")
    public FactionTrendDto getFactionTrend(@PathVariable String faction) {
        return factionService.getFactionTrend(faction);
    }
}
