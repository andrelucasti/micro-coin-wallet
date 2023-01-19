package com.walletmanager.sns;

import software.amazon.awscdk.services.sns.Topic;
import software.constructs.Construct;

public record PortfolioTopic(Construct scope) {
    public void execute(){
        Topic.Builder.create(scope, "wallet-manager-portfolio")
                .topicName("wallet-manager-portfolio")
                .build();
    }
}
