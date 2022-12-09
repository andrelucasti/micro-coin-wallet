package com.crypto.coinwallet.andrelucas.app.portfolio;

import com.crypto.coinwallet.andrelucas.business.portfolio.CreatePortfolio;
import com.crypto.coinwallet.andrelucas.business.portfolio.FetchPortfolioByUserId;
import com.crypto.coinwallet.andrelucas.business.portfolio.Portfolio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
    private final CreatePortfolio createPortfolio;
    private final FetchPortfolioByUserId fetchPortfolioByUserId;

    public PortfolioController(CreatePortfolio createPortfolio, FetchPortfolioByUserId fetchPortfolioByUserId) {
        this.createPortfolio = createPortfolio;
        this.fetchPortfolioByUserId = fetchPortfolioByUserId;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<PortfolioResponse> newPortfolio(@RequestHeader UUID userId,
                                                          @RequestBody PortfolioRequest portfolioRequest){

        var portfolio = new Portfolio(portfolioRequest.name(), userId);
        createPortfolio.execute(portfolio);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PortfolioResponse>> listAllPortfolioByUserId(@RequestHeader UUID userId){
        var portfolioResponses = fetchPortfolioByUserId.execute(userId)
                .stream()
                .map(portfolio -> new PortfolioResponse(portfolio.id(), portfolio.name(), portfolio.userId()))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(portfolioResponses);
    }
}
