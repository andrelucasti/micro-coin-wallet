package com.walletmanager;


import com.walletmanager.ecr.EcrStack;
import com.walletmanager.ecs.EcsStack;
import com.walletmanager.sns.SnsStack;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

public class WalletManagerStack extends Stack {
    private static final String VPC_NAME = "aws-resources-lab";
    public WalletManagerStack(final String stackName,
                              final Construct scope,
                              final StackProps props) {
        super(scope, stackName, props);

        var environment = Environment.SANDBOX;

        new EcrStack(this, "wallet-manager-registry-stack", props, environment)
                .create();

        new EcsStack(this, "wallet-manager-ecs-stack", props, environment, VPC_NAME)
                .create();
    }
}
