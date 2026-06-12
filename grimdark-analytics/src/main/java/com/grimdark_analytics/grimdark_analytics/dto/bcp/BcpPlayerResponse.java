package com.grimdark_analytics.grimdark_analytics.dto.bcp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BcpPlayerResponse(
        String id,
        String name,
        @JsonProperty("armyName") String armyName,
        @JsonProperty("wins")     Integer wins,
        @JsonProperty("losses")   Integer losses,
        @JsonProperty("draws")    Integer draws,
        @JsonProperty("placing")  Integer placing
) {}
