package com.grimdark_analytics.grimdark_analytics.dto.bcp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BcpPairingResponse(
        String id,
        @JsonProperty("player1Id") String player1Id,
        @JsonProperty("player2Id") String player2Id,
        @JsonProperty("winnerId")  String winnerId,
        @JsonProperty("roundNumber") int roundNumber
) {}
