package com.walletmanager.rds;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.rds.CfnDBInstance;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.constructs.Construct;

public class RDSStack extends Stack{

    private static final String RDS_ENGINE = "postgres";
    private static final String RDS_ENGINE_VERSION = "14";
    private static final String RDS_US_EAST_1 = "us-east-1d";
    private static final String DB_NAME = "walletManagerDatabase";
    private static final String RDS_DB_TYPE = "db.t4g.micro";
    private final String secretName;

    public RDSStack(final Construct scope,
                    final StackProps props,
                    final String secretName) {
        super(scope, "rdsStack", props);
        this.secretName = secretName;

    }

    public void execute(){
        var secretsManagerClient = SecretsManagerClient.builder()
                .credentialsProvider(getAwsCredentialsProvider())
                .build();

        var secretValueRequest = GetSecretValueRequest
                .builder()
                .secretId(secretName)
                .build();

        var secretValue = secretsManagerClient.getSecretValue(secretValueRequest).secretString();

       try{
           var objectMapper = new ObjectMapper();
           var dbSecretManagerDTO = objectMapper.readValue(secretValue, DBSecretManagerDTO.class);

           CfnDBInstance.Builder.create(this, "wallet-manager-database-stack")
                   .engine(RDS_ENGINE)
                   .engineVersion(RDS_ENGINE_VERSION)
                   .publiclyAccessible(true)
                   .storageEncrypted(false)
                   .allocatedStorage("30")
                   .availabilityZone(RDS_US_EAST_1)
                   .dbName(DB_NAME)
                   .dbInstanceClass(RDS_DB_TYPE)
                   .deleteAutomatedBackups(true)
                   .masterUsername(dbSecretManagerDTO.username())
                   .masterUserPassword(dbSecretManagerDTO.password())
                   .build();

       }catch (Exception e){
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
