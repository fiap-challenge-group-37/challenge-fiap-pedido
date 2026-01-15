package com.fiap.pedido.worker.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.pedido.domain.dto.PedidoPagoEvento;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoPagoPublisherTest {

    @Mock
    private SqsTemplate sqsTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PedidoPagoPublisher publisher;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(publisher, "queueName", "pedido-pago-queue");
    }

    @Test
    void devePublicarEventoComSucesso() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, List.of(
                new PedidoPagoEvento.ItemPedido("Hamburguer", 2)
        ));

        // Mocka a serialização
        when(objectMapper.writeValueAsString(evento)).thenReturn("{\"idPedido\":123,\"itens\":[]}");

        publisher.publicarPedidoPago(evento);

        verify(objectMapper, times(1)).writeValueAsString(evento);
        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    @Test
    void deveLogarMensagemAoPublicar() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(456L, List.of(
                new PedidoPagoEvento.ItemPedido("Refrigerante", 1)
        ));

        when(objectMapper.writeValueAsString(evento)).thenReturn("{\"idPedido\":456,\"itens\":[]}");

        assertDoesNotThrow(() -> publisher.publicarPedidoPago(evento));

        verify(objectMapper, times(1)).writeValueAsString(evento);
        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoEnviarSqs() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(789L, List.of(
                new PedidoPagoEvento.ItemPedido("Erro SQS", 1)
        ));

        when(objectMapper.writeValueAsString(evento)).thenReturn("{\"idPedido\":789,\"itens\":[]}");
        doThrow(new RuntimeException("Erro SQS"))
                .when(sqsTemplate).send(any(Consumer.class));

        assertThrows(RuntimeException.class, () -> publisher.publicarPedidoPago(evento));

        verify(objectMapper, times(1)).writeValueAsString(evento);
        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    @Test
    void deveLancarExcecaoQuandoFalharSerializacaoJson() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(159L, List.of(
                new PedidoPagoEvento.ItemPedido("Falha JSON", 1)
        ));

        when(objectMapper.writeValueAsString(evento)).thenThrow(new RuntimeException("Falha de serialização"));

        assertThrows(RuntimeException.class, () -> publisher.publicarPedidoPago(evento));

        verify(objectMapper, times(1)).writeValueAsString(evento);
        verify(sqsTemplate, never()).send(any(Consumer.class));
    }
}