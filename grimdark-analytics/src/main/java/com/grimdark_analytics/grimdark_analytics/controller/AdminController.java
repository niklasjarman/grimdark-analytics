package com.grimdark_analytics.grimdark_analytics.controller;

import com.grimdark_analytics.grimdark_analytics.model.Tournament;
import com.grimdark_analytics.grimdark_analytics.service.BcpImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Data import and administration endpoints")
public class AdminController {

    private final BcpImportService bcpImportService;

    @PostMapping("/import/bcp/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Import a BCP event",
        description = "Fetches tournament data from bestcoastpairings.com by event ID. " +
                      "Find the event ID in the BCP URL: bestcoastpairings.com/event/{eventId}"
    )
    public Tournament importBcpEvent(@PathVariable String eventId) {
        return bcpImportService.importEvent(eventId);
    }
}
