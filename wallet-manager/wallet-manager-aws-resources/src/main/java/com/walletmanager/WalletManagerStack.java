package com.walletmanager;


import com.walletmanager.rds.RDSStack;
import com.walletmanager.sns.SNSStack;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

public class WalletManagerStack extends Stack {
    public WalletManagerStack(final String stackName,
                              final Construct scope,
                              final StackProps props) {

        super(scope, stackName, props);

        SNSStack snsStack = new SNSStack("wallet-manager-sns-stack", scope, props);
        snsStack.execute();

        RDSStack rdsStack = new RDSStack("wallet-rds-resource-stack", scope, props, "walletManagerDBSecret");
        rdsStack.execute();
    }
}
