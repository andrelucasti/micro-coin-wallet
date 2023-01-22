package com.crypto;

import com.crypto.wallettransaction.EcrStack;
import com.crypto.wallettransaction.ecs.loadbalancer.LoadBalancerStack;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

public class WalletTransactionInfrastructureStack extends Stack {
    private static final String VPC_NAME = "aws-resources-lab";

    public WalletTransactionInfrastructureStack(final Construct scope,
                                                final String id,
                                                final StackProps props) {
        super(scope, id, props);

        var environment = Environment.SANDBOX;
        var vpcName = environment.withResourceName(VPC_NAME);

        new EcrStack(this, "wallet-transaction-repository", props, Environment.SANDBOX)
                .create();

        new LoadBalancerStack(this, "wallet-transaction-alb-stack", props, Environment.SANDBOX, vpcName)
                .create();
    }
}
