package com.grimdark_analytics.grimdark_analytics.dto.bcp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BcpPagedResponse<T>(List<T> data) {}
