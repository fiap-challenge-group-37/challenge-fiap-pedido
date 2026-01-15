package com.fiap.pedido.worker.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.pedido.domain.dto.PedidoPagoEvento;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PedidoPagoPublisher {

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(PedidoPagoPublisher.class);

    @Value("${events.queue.pedido-pago}")
    private String queueName;

    public PedidoPagoPublisher(SqsTemplate sqsTemplate, ObjectMapper objectMapper) {
        this.sqsTemplate = sqsTemplate;
        this.objectMapper = objectMapper;
    }

    public void publicarPedidoPago(PedidoPagoEvento evento) {
        try {
            String payloadJson = objectMapper.writeValueAsString(evento);
            logger.info("Publicando evento de pedido pago: {}", payloadJson);

            sqsTemplate.send(to -> to.queue(queueName).payload(payloadJson));

            logger.info("Evento publicado com sucesso na fila: {}", queueName);
        } catch (Exception e) {
            logger.error("Erro ao publicar evento de pedido pago:  {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao publicar evento de pedido pago", e);
        }
    }
}