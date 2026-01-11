package com.fiap.pedido.worker.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static org.junit.jupiter.api.Assertions.*;

class AwsSqsConfigTest {

    @Test
    void deveCriarSqsAsyncClientComEndpointVazio() {
        AwsSqsConfig config = new AwsSqsConfig();
        ReflectionTestUtils.setField(config, "region", "us-east-1");
        ReflectionTestUtils.setField(config, "endpoint", ""); // endpoint vazio

        SqsAsyncClient client = config.sqsAsyncClient();
        assertNotNull(client);
    }

    @Test
    void deveCriarSqsAsyncClientComEndpointDefinido() {
        AwsSqsConfig config = new AwsSqsConfig();
        ReflectionTestUtils.setField(config, "region", "us-east-1");
        ReflectionTestUtils.setField(config, "endpoint", "http://localhost:4566");

        SqsAsyncClient client = config.sqsAsyncClient();
        assertNotNull(client);
    }

    @Test
    void deveCriarSqsTemplate() {
        AwsSqsConfig config = new AwsSqsConfig();
        ReflectionTestUtils.setField(config, "region", "us-east-1");
        ReflectionTestUtils.setField(config, "endpoint", "");

        SqsAsyncClient client = config.sqsAsyncClient();
        SqsTemplate template = config.sqsTemplate(client);

        assertNotNull(template);
    }
}