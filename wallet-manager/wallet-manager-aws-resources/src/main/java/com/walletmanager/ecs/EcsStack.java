package com.walletmanager.ecs;

import com.walletmanager.Environment;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ecr.IRepository;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.CfnTaskDefinition;
import software.amazon.awscdk.services.logs.ILogGroup;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.constructs.Construct;

import java.util.Collections;
import java.util.Map;

public class EcsStack extends Stack {

    private static final String REPOSITORY_NAME = "wallet-manager";
    private static final String CLUSTER_NAME = "";
    private static final String APP_NAME = "wallet-manager";
    private static final String DATE_FORMAT = "%Y-%m-%dT%H:%M:%S.%f%z";
    private static final Number TASK_DEF_CPU = 256;
    private static final Number TASK_DEF_MEMORY = 1024;
    private static final Number CONTAINER_PORT = 8929;
    
    private final Environment environment;
    private final String imageTag;

    public EcsStack(final Construct scope,
                    final String id,
                    final StackProps props,
                    final Environment environment) {

        super(scope, id, props);

        this.environment = environment;
        this.imageTag = getImageTag(scope);
    }

    public void create(){
        IRepository repository = Repository.fromRepositoryName(this, "ecr-ecs-stack", REPOSITORY_NAME);

        ILogGroup logGroup = LogGroup.fromLogGroupName(this, "", environment.withResourceName(CLUSTER_NAME));
        CfnTaskDefinition.ContainerDefinitionProperty container = CfnTaskDefinition.ContainerDefinitionProperty.builder()
                .name(environment.withResourceName(APP_NAME))
                .cpu(TASK_DEF_CPU)
                .memory(TASK_DEF_MEMORY)
                .image(repository.repositoryUriForTagOrDigest(imageTag))
                .logConfiguration(CfnTaskDefinition.LogConfigurationProperty.builder()
                        .logDriver("awslogs")
                        .options(Map.of(
                                "awslogs-group", logGroup.getLogGroupName(),
                                "awslogs-region", "us-east-1",
                                "awslogs-stream-prefix", "stream-".concat(environment.withResourceName(APP_NAME)),
                                "awslogs-datetime-format", DATE_FORMAT
                        ))
                        .build())
                .portMappings(Collections.singletonList(CfnTaskDefinition.PortMappingProperty.builder()
                        .containerPort(CONTAINER_PORT)
                        .build()))
                .stopTimeout(2)
                .build();

    }

    private static String getImageTag(Construct scope) {
        return scope.getNode().tryGetContext("imageTag") == null ? "latest" : (String) scope.getNode().tryGetContext("imageTag");
    }
}
