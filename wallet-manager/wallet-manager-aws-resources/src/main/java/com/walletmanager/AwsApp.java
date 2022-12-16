package com.walletmanager;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class AwsApp {
    public static void main(final String[] args) {
        App app = new App();
        Account account = new Account(app);

        String accountId = account.getAccountId();
        String region = account.getRegion();

        executeStacks(app, accountId, region);

        app.synth();
    }
    private static void executeStacks(App app, String accountId, String region) {

        new WalletManagerStack(app, "wallet-manager-aws-resource-stack",
                    StackProps.builder().env(getEnv(accountId, region))
                            .build());

        app.synth();
    }

    public static Environment getEnv(final String accountId,
                                     final String region){

        return Environment.builder()
                .account(accountId)
                .region(region)
                .build();
    }
}

