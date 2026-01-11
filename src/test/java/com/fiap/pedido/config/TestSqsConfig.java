package com.fiap.pedido.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestSqsConfig {
    @Bean
    public SqsTemplate sqsTemplate() {
        return Mockito.mock(SqsTemplate.class);
    }
}