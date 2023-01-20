package com.crypto;

import com.crypto.wallettransaction.ecs.EcsStack;
import com.crypto.wallettransaction.sqs.SqsStack;
import com.crypto.wallettransaction.sqs.queues_subscribed.QueuesSubscribedStack;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

public class WalletTransactionAppStack extends Stack {
    private static final String VPC_NAME = "aws-resources-lab";
    public WalletTransactionAppStack(final Construct scope,
                                     final String id,
                                     final StackProps props) {

        super(scope, id, props);

        SqsStack sqsStack = new SqsStack(
                new QueuesSubscribedStack(this, props)
        );

        sqsStack.execute();

        var environment = Environment.SANDBOX;
        var vpcName = environment.withResourceName(VPC_NAME);
        new EcsStack(this, "wallet-transaction-ecs-stack", props, Environment.SANDBOX, vpcName)
                .create();
    }
}
