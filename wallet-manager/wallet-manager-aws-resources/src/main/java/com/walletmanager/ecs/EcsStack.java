package com.walletmanager.ecs;

import com.walletmanager.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.ISubnet;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcLookupOptions;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.CfnService;
import software.amazon.awscdk.services.ecs.CfnTaskDefinition;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListener;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListenerLookupOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.CfnListenerRule;
import software.amazon.awscdk.services.elasticloadbalancingv2.CfnTargetGroup;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.logs.LogGroup;
import software.constructs.Construct;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EcsStack extends Stack {
    private static final String REPOSITORY_NAME = "wallet-manager";
    private static final String CLUSTER_NAME = "aws-resource-lab";
    private static final String APP_NAME = "wallet-manager";
    private static final String APP_LB_NAME = "aws-resource-lab-alb";
    private static final String DATE_FORMAT = "%Y-%m-%dT%H:%M:%S.%f%z";
    private static final Number TASK_DEF_CPU = 256;
    private static final Number TASK_DEF_MEMORY = 1024;
    private static final Number CONTAINER_PORT = 8929;
    private static final String PROTOCOL = "HTTP";
    
    private final Environment environment;
    private final String vpcName;
    private final String imageTag;
    private final String account;
    private final String region;

    public EcsStack(final Construct scope,
                    final String id,
                    final StackProps props,
                    final Environment environment,
                    final String vpcName) {

        super(scope, id, props);

        this.vpcName = environment.withResourceName(vpcName);
        this.environment = environment;

        this.account = getAccount(scope);
        this.region = getRegion(scope);
        this.imageTag = getImageTag(scope);
    }

    public void create(){
        var vpc = Vpc.fromLookup(this, "aws-resources-vpc-stack", VpcLookupOptions.builder().vpcName(vpcName).isDefault(false).build());
        var taskDefinition = getTaskDefinition();
        var cfnTargetGroup = createTargetGroup(vpc);
        var ecsSecurityGroup = SecurityGroup.fromLookupByName(this, "ecsSecurityGroup", "ecsSecurityGroup", vpc);

        var loadBalancerProperty = CfnService.LoadBalancerProperty.builder()
                .containerName(environment.withResourceName(APP_NAME))
                .containerPort(CONTAINER_PORT)
                .targetGroupArn(cfnTargetGroup.getRef())
                .build();

        var ecsService = CfnService.Builder.create(this, "ecsService")
                .cluster(environment.withResourceName(CLUSTER_NAME))
                .launchType("FARGATE")
                .deploymentConfiguration(CfnService.DeploymentConfigurationProperty.builder()
                        .maximumPercent(100)
                        .minimumHealthyPercent(50)
                        .build())
                .desiredCount(2)
                .taskDefinition(taskDefinition.getRef())
                .serviceName(environment.withResourceName(APP_NAME))
                .loadBalancers(Collections.singletonList(loadBalancerProperty))
                .networkConfiguration(CfnService.NetworkConfigurationProperty.builder()
                        .awsvpcConfiguration(CfnService.AwsVpcConfigurationProperty.builder()
                                .assignPublicIp("ENABLED")
                                .securityGroups(Collections.singletonList(ecsSecurityGroup.getSecurityGroupId()))
                                .subnets(vpc.getPublicSubnets().stream().map(ISubnet::getSubnetId).toList())
                                .build())
                        .build())
                .build();


        var httpListenerRule = createHttpListenerRule(cfnTargetGroup);
        ecsService.addDependsOn(httpListenerRule);
    }



    private static String getImageTag(Construct scope) {
        return scope.getNode().tryGetContext("imageTag") == null ? "latest" : (String) scope.getNode().tryGetContext("imageTag");
    }

    private static String getAccount(Construct scope) {
        return scope.getNode().tryGetContext("account") == null ? "latest" : (String) scope.getNode().tryGetContext("account");
    }

    private static String getRegion(Construct scope) {
        return scope.getNode().tryGetContext("region") == null ? "latest" : (String) scope.getNode().tryGetContext("region");
    }

    private CfnTargetGroup createTargetGroup(IVpc vpc){
        return CfnTargetGroup.Builder.create(this, "targetGroup")
                .healthCheckIntervalSeconds(30)
                .healthCheckPath("/actuator/health")
                .healthCheckPort(String.valueOf(CONTAINER_PORT))
                .healthCheckProtocol(PROTOCOL)
                .healthCheckTimeoutSeconds(5)
                .healthyThresholdCount(2)
                .unhealthyThresholdCount(8)
                .targetGroupAttributes(List.of(
                        CfnTargetGroup.TargetGroupAttributeProperty.builder().key("stickiness.enabled").value("true").build(),
                        CfnTargetGroup.TargetGroupAttributeProperty.builder().key("stickiness.type").value("lb_cookie").build(),
                        CfnTargetGroup.TargetGroupAttributeProperty.builder().key("stickiness.lb_cookie.duration_seconds").value("3600").build()
                ))
                .targetType("ip")
                .port(CONTAINER_PORT)
                .protocol(PROTOCOL)
                .vpcId(vpc.getVpcId())
                .build();
    }

    private CfnTaskDefinition.ContainerDefinitionProperty buildContainerConfiguration(){
        var repository = Repository.fromRepositoryName(this, "ecr-ecs-stack", REPOSITORY_NAME);
        var logGroup = LogGroup.fromLogGroupName(this, "ecsLogGroup", environment.withResourceName(CLUSTER_NAME));

        return CfnTaskDefinition.ContainerDefinitionProperty.builder()
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

    private CfnTaskDefinition getTaskDefinition(){
        var container = buildContainerConfiguration();
        var ecsTaskExecutionRole =
                Role.fromRoleName(this, "ecsTaskExecutionRole", environment.withResourceName("aws-resource-ecsTaskExecutionRole"));
        var ecsTaskRole = Role.fromRoleName(this, "ecsTaskRole", environment.withResourceName("aws-resource-ecsTaskRole"));

        return CfnTaskDefinition.Builder.create(this, "taskDefinition")
                .cpu(String.valueOf(TASK_DEF_CPU))
                .memory(String.valueOf(TASK_DEF_MEMORY))
                .networkMode("awsvpc")
                .requiresCompatibilities(Collections.singletonList("FARGATE"))
                .executionRoleArn(ecsTaskExecutionRole.getRoleArn())
                .taskRoleArn(ecsTaskRole.getRoleArn())
                .containerDefinitions(Collections.singletonList(container))
                .build();
    }

    private CfnListenerRule createHttpListenerRule(CfnTargetGroup cfnTargetGroup){
        var actionProperty = CfnListenerRule.ActionProperty.builder()
                .targetGroupArn(cfnTargetGroup.getRef())
                .type("forward")
                .build();
        var ruleConditionProperty = CfnListenerRule.RuleConditionProperty.builder()
                .field("path-pattern")
                .values(Collections.singletonList("*"))
                .build();

        var httpListener = ApplicationListener.fromLookup(this, "httpListener", ApplicationListenerLookupOptions.builder()
                .listenerPort(80)
                .loadBalancerArn("arn:aws:elasticloadbalancing:us-east-1:040335195619:loadbalancer/app/sandbox-aws-resource-lab-alb/47325464f863cbce")
                .build());

        return CfnListenerRule.Builder.create(this, "httpListenerRule")
                .actions(Collections.singletonList(actionProperty))
                .conditions(Collections.singletonList(ruleConditionProperty))
                .listenerArn(httpListener.getListenerArn())
                .priority(2)
                .build();
    }
}
