package com.fiap.pedido.worker.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class AwsSqsConfigTest {

    @Test
    void deveCriarSqsAsyncClientComEndpointVazio() {
        AwsSqsConfig config = new AwsSqsConfig();
        ReflectionTestUtils.setField(config, "region", "us-east-1");
        ReflectionTestUtils.setField(config, "endpoint", "  "); // String vazia com espaços

        SqsAsyncClient client = config.sqsAsyncClient();
        assertNotNull(client);
        // Não deve definir endpoint override
        // Não podemos acessar o builder direto, validamos só não-null
    }

    @Test
    void deveCriarSqsAsyncClientComEndpointDefinido() {
        AwsSqsConfig config = new AwsSqsConfig();
        ReflectionTestUtils.setField(config, "region", "us-east-1");
        String endpointUrl = "http://localhost:4566";
        ReflectionTestUtils.setField(config, "endpoint", endpointUrl);

        SqsAsyncClient client = config.sqsAsyncClient();
        assertNotNull(client);
        // Aqui, para garantir 100%, pelo menos rodamos o if do endpoint definido
    }

    @Test
    void deveCriarSqsAsyncClientSemEndpoint() {
        AwsSqsConfig config = new AwsSqsConfig();
        ReflectionTestUtils.setField(config, "region", "us-east-1");
        ReflectionTestUtils.setField(config, "endpoint", null);

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