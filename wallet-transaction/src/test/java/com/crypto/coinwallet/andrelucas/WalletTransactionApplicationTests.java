package com.crypto.coinwallet.andrelucas;

import com.crypto.coinwallet.andrelucas.business.portfolio.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;
import java.net.URISyntaxException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public abstract class WalletTransactionApplicationTests {

	@Autowired
	protected PortfolioRepository portfolioRepository;

	@BeforeEach
	void setUp() {
		portfolioRepository.deleteAll();
	}

	protected SqsClient sqsClient() throws URISyntaxException {
		return SqsClient.builder()
				.region(Region.US_EAST_1)
				.endpointOverride(new URI("http://localhost:4566"))
				.credentialsProvider(getAwsCredentialsProvider())
				.build();
	}

	protected AwsCredentialsProvider getAwsCredentialsProvider() {
		return AwsCredentialsProviderChain.builder()
				.addCredentialsProvider(DefaultCredentialsProvider.create())
				.build();
	}

}
