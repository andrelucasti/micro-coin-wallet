package com.crypto.coinwallet.andrelucas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.net.URI;
import java.net.URISyntaxException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class WalletManagerApplicationTests {

	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
	}

	protected AwsCredentialsProvider getAwsCredentialsProvider() {
		return AwsCredentialsProviderChain.builder()
				.addCredentialsProvider(DefaultCredentialsProvider.create())
				.build();
	}

	protected SqsClient sqsClient() throws URISyntaxException {
		return SqsClient.builder()
				.region(Region.US_EAST_1)
				.endpointOverride(new URI("http://localhost:4566"))
				.credentialsProvider(getAwsCredentialsProvider())
				.build();
	}

	protected SnsClient snsClient() throws URISyntaxException {
		return SnsClient.builder()
				.region(Region.US_EAST_1)
				.endpointOverride(new URI("http://localhost:4566"))
				.credentialsProvider(getAwsCredentialsProvider())
				.build();
	}

	protected SubscribeRequest createSubscribe(String topicName, String queueName) throws URISyntaxException {
		var topicArn = createTopic(topicName);
		var queueUrl = createQueue(queueName);

		var subscribeRequest = SubscribeRequest.builder()
				.protocol("sqs")
				.topicArn(topicArn)
				.endpoint(queueUrl)
				.build();

		snsClient().subscribe(subscribeRequest);

		return subscribeRequest;
	}

	protected String createQueue(String queueName) throws URISyntaxException {
		var createQueueRequest = CreateQueueRequest.builder()
				.queueName(queueName)
				.build();
		return sqsClient().createQueue(createQueueRequest).queueUrl();
	}

	protected String createTopic(String topicName) throws URISyntaxException {
		var createTopicRequest = CreateTopicRequest.builder()
				.name(topicName)
				.build();
		return snsClient().createTopic(createTopicRequest).topicArn();
	}

	protected ReceiveMessageResponse receiveMessage(String queueUrl) throws URISyntaxException {
		var sqsClient = sqsClient();
		var receiveMessageRequest = ReceiveMessageRequest.builder().queueUrl(queueUrl).build();
		return sqsClient.receiveMessage(receiveMessageRequest);
	}


	protected <T> T convertFromSNSPayloadToObject(String payload, Class<T> clazz) {

		try {
			JsonNode jsonNode = objectMapper.readTree(payload);
			String message = jsonNode.get("Message").textValue();
			return objectMapper.readValue(message, clazz);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
