package com.walletmanager.sns;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;
public class SnsStack extends Stack {
    private final PortfolioTopic portfolioTopic;
    public SnsStack(String stackName,
                    Construct construct,
                    StackProps stackProps) {
        super(construct, stackName, stackProps);

        portfolioTopic = new PortfolioTopic(this);
    }

    public void create(){
        portfolioTopic.execute();
    }
}
