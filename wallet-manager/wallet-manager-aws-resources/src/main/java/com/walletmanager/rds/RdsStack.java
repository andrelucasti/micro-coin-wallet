package com.walletmanager.rds;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletmanager.Environment;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.SubnetConfiguration;
import software.amazon.awscdk.services.ec2.SubnetSelection;
import software.amazon.awscdk.services.ec2.SubnetType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcLookupOptions;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.rds.Credentials;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine;
import software.amazon.awscdk.services.rds.IInstanceEngine;
import software.amazon.awscdk.services.rds.NetworkType;
import software.amazon.awscdk.services.rds.PostgresEngineVersion;
import software.amazon.awscdk.services.rds.PostgresInstanceEngineProps;
import software.amazon.awscdk.services.rds.StorageType;
import software.amazon.awscdk.services.rds.SubnetGroup;
import software.amazon.awscdk.services.secretsmanager.ISecret;
import software.amazon.awscdk.services.secretsmanager.Secret;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.constructs.Construct;

import java.util.List;

public class RdsStack extends Stack {

    private static final String RDS_ENGINE = "postgres";
    private static final String RDS_ENGINE_VERSION = "14";
    private static final String RDS_US_EAST_1 = "us-east-1d";
    private static final String DB_NAME = "walletManagerDatabase";
    private static final String RDS_DB_TYPE = "db.t4g.micro";
    private final Environment environment;
    private final String secretName;
    private final String vpcName;


    public RdsStack(final Construct scope,
                    final String id,
                    final StackProps props,
                    final Environment environment,
                    final String secretName,
                    final String vpcName) {

        super(scope, id, props);
        this.environment = environment;
        this.secretName = secretName;
        this.vpcName = environment.withResourceName(vpcName);
    }


    public void create(){
//        var secretsManagerClient = SecretsManagerClient.builder()
//                .credentialsProvider(getAwsCredentialsProvider())
//                .build();
//
//        var secretValueRequest = GetSecretValueRequest
//                .builder()
//                .secretId(secretName)
//                .build();

//        var secretValue = secretsManagerClient.getSecretValue(secretValueRequest).secretString();

       try{
//           var objectMapper = new ObjectMapper();
//           var dbSecretManagerDTO = objectMapper.readValue(secretValue, DBSecretManagerDTO.class);

/*           CfnDBInstance.Builder.create(this, "wallet-manager-database-stack")
                   .vpcSecurityGroups(Collections.singletonList(vpc.getVpcId()))
                   .engine(RDS_ENGINE)
                   .engineVersion(RDS_ENGINE_VERSION)
                   .publiclyAccessible(true)
                   .storageEncrypted(false)
                   .allocatedStorage("30")
                   .availabilityZone(RDS_US_EAST_1)
                   .dbName(DB_NAME)
                   .tags(Collections.singletonList(CfnTag.builder().key("env").value(environment.value()).build()))
                   .dbInstanceClass(RDS_DB_TYPE)
                   .deleteAutomatedBackups(true)
                   .masterUsername(dbSecretManagerDTO.username())
                   .masterUserPassword(dbSecretManagerDTO.password())
                   .build();*/


           var postgres = DatabaseInstanceEngine.postgres(PostgresInstanceEngineProps.builder()
                           .version(PostgresEngineVersion.VER_14)
                           .build());

           var vpc = Vpc.Builder
                   .create(this, "vpc-rds")
                   .vpcName("wallet-manager-vpc")
                   .availabilityZones(List.of(RDS_US_EAST_1))
                   .build();

           SubnetGroup subnetGroup = SubnetGroup.Builder.create(this, "subnet-gp-rds")
                   .description("test-rds-subnet")
                   .vpc(vpc)
                   .vpcSubnets(SubnetSelection.builder()
                           .availabilityZones(List.of(RDS_US_EAST_1))
                           .onePerAz(false)
                           .build())
                   .build();

           var iSecret = Secret.fromSecretNameV2(this, "secretDb", secretName);
           DatabaseInstance
                   .Builder.create(this, "wallet-manager-database")
                   .vpc(vpc)
                   .subnetGroup(subnetGroup)
                   .publiclyAccessible(true)
                   .storageEncrypted(false)
                   .engine(postgres)
                   .allocatedStorage(20)
                   .availabilityZone(RDS_US_EAST_1)
                   .subnetGroup(subnetGroup)
                   .instanceType(InstanceType.of(InstanceClass.T4G, InstanceSize.MICRO))
                   .deleteAutomatedBackups(true)
                   .backupRetention(Duration.days(0))
                   .enablePerformanceInsights(false)
                   .cloudwatchLogsRetention(RetentionDays.ONE_WEEK)
                   .databaseName(DB_NAME)
                   .credentials(Credentials.fromSecret(iSecret))
                   .port(5432)
                   .networkType(NetworkType.IPV4)
                   .allowMajorVersionUpgrade(false)
                   .autoMinorVersionUpgrade(false)
                   .storageType(StorageType.GP2)
                   .multiAz(false)
                   .instanceIdentifier(environment.withResourceName("wallet-manager"))
                   .build();

       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    private record DBSecretManagerDTO(@JsonProperty("username") String username,
                                      @JsonProperty("password") String password) {
    }

    protected AwsCredentialsProvider getAwsCredentialsProvider() {
        return AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
