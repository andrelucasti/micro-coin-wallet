package com.crypto.wallettransaction.ecs.loadbalancer;


import com.crypto.Environment;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.CfnSecurityGroupIngress;
import software.amazon.awscdk.services.ec2.ISecurityGroup;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcLookupOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.AddApplicationTargetGroupsProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListener;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationLoadBalancer;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationProtocol;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationTargetGroup;
import software.amazon.awscdk.services.elasticloadbalancingv2.BaseApplicationListenerProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.elasticloadbalancingv2.IApplicationTargetGroup;
import software.amazon.awscdk.services.elasticloadbalancingv2.TargetType;
import software.constructs.Construct;

import java.util.Collections;

public class LoadBalancerStack extends Stack {

    public static final String APP_LB_NAME = "wallet-transaction-alb";
    private final Environment environment;
    private final String vpcName;

    public LoadBalancerStack(final Construct scope,
                             final String id,
                             final StackProps props,
                             final Environment environment,
                             final String vpcName) {

        super(scope, id, props);
        this.environment = environment;
        this.vpcName = vpcName;
    }

    public void create(){
        IVpc vpc = Vpc.fromLookup(this, "vpn", VpcLookupOptions.builder().vpcName(vpcName).isDefault(false).build());
        ISecurityGroup loadbalancerSecGroup = SecurityGroup.fromSecurityGroupId(this, "loadbalancerSecGroup", "sg-043120be73476aecc");

        ApplicationLoadBalancer applicationLoadBalancer = ApplicationLoadBalancer.Builder.create(this, "loadBalancer")
                .loadBalancerName(environment.withResourceName(APP_LB_NAME))
                .vpc(vpc)
                .internetFacing(true)
                .securityGroup(loadbalancerSecGroup)
                .build();

        IApplicationTargetGroup defaultAppTargetGroup = ApplicationTargetGroup.Builder.create(this, "defaultTargetGroup")
                .vpc(vpc)
                .port(8929)
                .protocol(ApplicationProtocol.HTTP)
                .targetGroupName(environment.withResourceName("defaultTargetGroup"))
                .targetType(TargetType.IP)
                .deregistrationDelay(Duration.seconds(5))
                .healthCheck(HealthCheck.builder()
                        .healthyThresholdCount(2)
                        .interval(Duration.seconds(10))
                        .timeout(Duration.seconds(5))
                        .build())
                .build();

        ApplicationListener httpListener = applicationLoadBalancer.addListener("httpListener",
                BaseApplicationListenerProps.builder()
                        .port(80)
                        .protocol(ApplicationProtocol.HTTP)
                        .open(true)
                        .build());

        httpListener.addTargetGroups("http-default-target-group", AddApplicationTargetGroupsProps.builder()
                .targetGroups(Collections.singletonList(defaultAppTargetGroup))
                .build());
    }
}
