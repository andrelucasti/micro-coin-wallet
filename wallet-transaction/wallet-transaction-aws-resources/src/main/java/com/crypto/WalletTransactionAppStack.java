package com.crypto;

import com.crypto.wallettransaction.sqs.SqsStack;
import com.crypto.wallettransaction.sqs.queues_subscribed.QueuesSubscribedStack;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

public class WalletTransactionAppStack extends Stack {
    public WalletTransactionAppStack(final Construct scope,
                                     final String id,
                                     final StackProps props) {

        super(scope, id, props);

        SqsStack sqsStack = new SqsStack(
                new QueuesSubscribedStack(this, props)
        );

        sqsStack.execute();
    }
}
