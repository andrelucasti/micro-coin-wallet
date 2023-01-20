package com.walletmanager;


import com.walletmanager.ecs.EcsStack;
import com.walletmanager.sns.SnsStack;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

public class WalletManagerAppStack extends Stack {
    private static final String VPC_NAME = "aws-resources-lab";
    public WalletManagerAppStack(final String stackName,
                                 final Construct scope,
                                 final StackProps props) {
        super(scope, stackName, props);

        var environment = Environment.SANDBOX;

        new EcsStack(this, "wallet-manager-ecs-stack", props, environment, VPC_NAME)
                .create();
        new SnsStack(this, "wallet-manager-sns-stack", props)
                .create();
    }
}
