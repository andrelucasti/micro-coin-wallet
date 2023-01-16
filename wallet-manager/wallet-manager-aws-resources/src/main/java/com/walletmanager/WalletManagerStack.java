package com.walletmanager;


import com.walletmanager.ecr.EcrStack;
import com.walletmanager.rds.RDSStack;
import com.walletmanager.sns.SnsStack;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

public class WalletManagerStack extends Stack {

    public WalletManagerStack(final String stackName,
                              final Construct scope,
                              final StackProps props) {

        super(scope, stackName, props);

        EcrStack ecrStack = new EcrStack(this, "wallet-manager-registry-stack", props, Environment.SANDBOX);
        ecrStack.execute();

        SnsStack snsStack = new SnsStack("wallet-manager-sns-stack", scope, props);
        snsStack.execute();

        RDSStack rdsStack = new RDSStack("wallet-manager-rds-resource-stack", scope, props, "walletManagerDBSecret");
        rdsStack.execute();
    }
}
