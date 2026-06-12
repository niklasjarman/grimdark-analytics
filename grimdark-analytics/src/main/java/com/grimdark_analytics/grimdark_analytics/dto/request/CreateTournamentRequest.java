package com.grimdark_analytics.grimdark_analytics.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateTournamentRequest(
        @NotBlank String name,
        @NotNull  LocalDate date,
        String location,
        String format,
        @Min(2) Integer playerCount
) {}
