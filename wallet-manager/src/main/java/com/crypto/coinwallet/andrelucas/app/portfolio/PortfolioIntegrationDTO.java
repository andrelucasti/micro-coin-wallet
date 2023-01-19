package com.crypto.coinwallet.andrelucas.app.portfolio;

import java.util.Objects;
import java.util.UUID;

public record PortfolioIntegrationDTO(UUID id, String name) {
    public PortfolioIntegrationDTO {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
    }
}
