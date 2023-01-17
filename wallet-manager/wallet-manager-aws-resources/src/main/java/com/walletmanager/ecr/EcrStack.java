package com.walletmanager.ecr;

import com.walletmanager.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecr.TagMutability;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.constructs.Construct;

public class EcrStack extends Stack {

    private final Environment environment;


    public EcrStack(final Construct scope,
                    final String id,
                    final StackProps props,
                    final Environment environment) {
        super(scope, id, props);
        this.environment = environment;
    }

    public void create(){
        Repository repository = Repository.Builder.create(this, "wallet-manager-registry")
                .repositoryName("wallet-manager")
                .imageTagMutability(TagMutability.IMMUTABLE)
                .build();

        IRole ecsTaskExecutionRole =
                Role.fromRoleName(this, "ecsTaskExecutionRole", environment.value().concat("-").concat("aws-resource-ecsTaskExecutionRole"));

        repository.grantPull(ecsTaskExecutionRole);
    }
}
