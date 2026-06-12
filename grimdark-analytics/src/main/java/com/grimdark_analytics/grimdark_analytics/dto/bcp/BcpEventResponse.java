package com.grimdark_analytics.grimdark_analytics.dto.bcp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BcpEventResponse(
        String id,
        String name,
        @JsonProperty("datePlayed") String datePlayed,
        String city,
        String state,
        String country,
        @JsonProperty("numberOfRounds") int numberOfRounds
) {}
