package com.crypto.wallettransaction;

import com.crypto.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecr.TagMutability;
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
        var repository = Repository.Builder.create(this, "wallet-manager-registry")
                .repositoryName("wallet-transaction")
                .imageTagMutability(TagMutability.IMMUTABLE)
                .build();

        var ecsTaskExecutionRole =
                Role.fromRoleName(this, "ecsTaskExecutionRole", environment.value().concat("-").concat("aws-resource-ecsTaskExecutionRole"));

        repository.grantPull(ecsTaskExecutionRole);
    }
}
