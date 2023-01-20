package com.walletmanager.sns;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;
public class SnsStack extends Stack {
    private final PortfolioTopic portfolioTopic;
    public SnsStack(Construct construct, String stackName,
                    StackProps stackProps) {
        super(construct, stackName, stackProps);

        portfolioTopic = new PortfolioTopic(this);
    }

    public void create(){
        portfolioTopic.execute();
    }
}
