package com.fiap.pedido.worker.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
@ConditionalOnProperty(name = "spring.cloud.aws.sqs.enabled", havingValue = "true", matchIfMissing = true)
public class AwsSqsConfig {

    @Value("${spring.cloud.aws.region.static:us-east-1}")
    private String region;

    @Value("${spring.cloud.aws.sqs.endpoint:}")
    private String endpoint;

    @Value("${spring.cloud.aws.credentials.access-key:}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key:}")
    private String secretKey;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        var builder = SqsAsyncClient.builder()
                .region(Region.of(region));
        if (endpoint != null && !endpoint.trim().isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                            accessKey == null ? "test" : accessKey,
                            secretKey == null ? "test" : secretKey)
            ));
        }
        return builder.build();
    }

    @Bean
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }
}