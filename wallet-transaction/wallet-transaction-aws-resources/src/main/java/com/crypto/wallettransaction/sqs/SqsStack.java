package com.crypto.wallettransaction.sqs;

import com.crypto.wallettransaction.sqs.queues_subscribed.QueuesSubscribedStack;


public class SqsStack {
    private final QueuesSubscribedStack queuesSubscribedStack;


    public SqsStack(QueuesSubscribedStack queuesSubscribedStack) {
        this.queuesSubscribedStack = queuesSubscribedStack;

    }

    public void execute() {
        queuesSubscribedStack.execute();
    }
}