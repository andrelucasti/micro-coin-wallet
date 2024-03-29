package com.walletmanager.rds;

import org.junit.jupiter.api.Test;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.assertions.Match;
import software.amazon.awscdk.assertions.Template;

import java.util.Map;

class RdsStackTest {
    @Test
    void shouldCreateWalletDB() {
        StackProps stackProps = StackProps.builder().env(getEnv("000000000000", "us-east-1"))
                .build();

        App app = new App();
        RdsStack rdsStack = new RdsStack(app, "WalletManagerRdsStack", stackProps, com.walletmanager.Environment.SANDBOX, "walletManagerDBSecret", "vpcName");
        rdsStack.create();

        Template template = Template.fromStack(rdsStack);
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