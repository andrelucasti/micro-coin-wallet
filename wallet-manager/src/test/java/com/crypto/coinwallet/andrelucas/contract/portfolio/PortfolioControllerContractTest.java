package com.crypto.coinwallet.andrelucas.contract.portfolio;

import com.crypto.coinwallet.andrelucas.WalletManagerApplicationTests;
import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import com.crypto.coinwallet.andrelucas.dataprovider.portfolio.PortfolioRepositoryImpl;
import com.google.common.io.Resources;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

class PortfolioControllerContractTest extends WalletManagerApplicationTests {

    @Autowired
    private PortfolioRepositoryImpl portfolioRepository;

    @Test
    void shouldReturnPortfolioWhenIsSaved() throws IOException {
        var userId = UUID.randomUUID();
        var payload = Resources.toString(Resources.getResource("contracts/portfolio-request.json"), StandardCharsets.UTF_8)
                .replace("{name}", "Token Wallet");

        RestAssuredMockMvc.given()
                .header("userId", userId)
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/portfolio")
                .then()
                .status(HttpStatus.CREATED);

        var portfolioList = portfolioRepository.findAll();
        
        Assertions.assertThat(portfolioList).isNotEmpty();
        Assertions.assertThat(portfolioList).hasSize(1);
        Assertions.assertThat(portfolioList.stream().findAny().get().userId()).isEqualTo(userId);
    }

    @Test
    void shouldReturnPortfolioByWalletId() throws IOException {
        var userId = UUID.randomUUID();
        var portFolioName = "My Token Game";
        portfolioRepository.save(new Portfolio(portFolioName, userId));

        MockHttpServletResponse mockHttpServletResponse = RestAssuredMockMvc.given()
            .header("userId", userId)
            .contentType(ContentType.JSON)
            .get("/portfolio")
            .getMockHttpServletResponse();

        var responseExpected = Resources.toString(Resources.getResource("contracts/portfolio-response.json"), StandardCharsets.UTF_8)
        .replace("{name}", portFolioName)
        .replace("{userId}", userId.toString());

        Assertions.assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(responseExpected).containsAnyOf(userId.toString(), portFolioName);

    }
}