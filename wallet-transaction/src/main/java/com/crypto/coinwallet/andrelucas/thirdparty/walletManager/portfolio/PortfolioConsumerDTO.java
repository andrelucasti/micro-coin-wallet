package com.crypto.coinwallet.andrelucas.thirdparty.walletManager.portfolio;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record PortfolioConsumerDTO(@JsonProperty(required = true, value = "id") UUID id,
                                   @JsonProperty(required = true, value = "name") String name) {
}
