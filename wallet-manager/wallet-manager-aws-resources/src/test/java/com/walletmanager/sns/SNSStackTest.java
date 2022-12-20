package com.walletmanager.sns;

import org.junit.jupiter.api.Test;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.assertions.Match;
import software.amazon.awscdk.assertions.Template;

import java.util.Map;

class SNSStackTest {

    @Test
    void shouldCreateWalletManagerTopic() {
        StackProps stackProps = StackProps.builder().env(getEnv("000000000000", "us-east-1"))
                .build();

        SNSStack snsStack = new SNSStack("walletManagerTopic", new App(), stackProps);
        snsStack.execute();

        Template template = Template.fromStack(snsStack);

        template.hasResourceProperties("AWS::SNS::Topic", Map.of(
                "TopicName", Match.exact("wallet-manager-portfolio"))
        );

    }

    public static Environment getEnv(final String accountId,
                                     final String region){

        return Environment.builder()
                .account(accountId)
                .region(region)
                .build();
    }
}