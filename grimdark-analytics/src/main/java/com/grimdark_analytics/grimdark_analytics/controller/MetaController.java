package com.grimdark_analytics.grimdark_analytics.controller;

import com.grimdark_analytics.grimdark_analytics.dto.MetaSnapshotDto;
import com.grimdark_analytics.grimdark_analytics.service.MetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meta")
@RequiredArgsConstructor
@Tag(name = "Meta", description = "Overall meta snapshot")
public class MetaController {

    private final MetaService metaService;

    @GetMapping("/snapshot")
    @Operation(summary = "Get current meta snapshot with top factions and aggregate counts")
    public MetaSnapshotDto getSnapshot() {
        return metaService.getSnapshot();
    }
}
