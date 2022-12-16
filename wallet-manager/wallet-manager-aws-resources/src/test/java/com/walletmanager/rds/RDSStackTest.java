package com.walletmanager.rds;

import org.junit.jupiter.api.Test;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.assertions.Match;
import software.amazon.awscdk.assertions.Template;

import java.util.Map;

class RDSStackTest {
    @Test
    void shouldCreateWalletDB() {
        StackProps stackProps = StackProps.builder().env(getEnv("000000000000", "us-east-1"))
                .build();

        App app = new App();
        RDSStack rdsStack = new RDSStack(app, stackProps, "walletManagerDBSecret");
        rdsStack.execute();
        Template template = Template.fromStack(rdsStack);

        System.out.println(template.toJSON());
        template.hasResourceProperties("AWS::RDS::DBInstance", Map.of(
                    "DBName", Match.exact("walletManagerDatabase"),
                    "EngineVersion", "14")

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