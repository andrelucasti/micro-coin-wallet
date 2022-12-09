package com.crypto.coinwallet.andrelucas.business.portfolio;

public class PortfolioNotFoundException extends Exception {

    public PortfolioNotFoundException(String message) {
        super(message);
    }

    public PortfolioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
