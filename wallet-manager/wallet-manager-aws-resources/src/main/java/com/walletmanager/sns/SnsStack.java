package com.walletmanager.sns;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.sns.Topic;
import software.constructs.Construct;
public class SnsStack extends Stack {
    public SnsStack(String stackName,
                    Construct construct,
                    StackProps stackProps) {
        super(construct, stackName, stackProps);
    }

    public void execute(){
        Topic.Builder.create(this, "wallet-manager-portfolio")
                .topicName("wallet-manager-portfolio")
                .build();
    }
}
