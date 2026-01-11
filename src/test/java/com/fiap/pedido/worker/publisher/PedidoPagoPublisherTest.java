package com.fiap.pedido.worker.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.pedido.domain.dto.PedidoPagoEvento;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class PedidoPagoPublisherTest {

    @Mock
    private SqsTemplate sqsTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PedidoPagoPublisher publisher;

    @BeforeEach
    void setUp() throws Exception {
        ReflectionTestUtils.setField(publisher, "queueName", "pedido-pago-queue");
        // Normalmente responde com JSON válido
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"idPedido\":123}");
    }

    @Test
    void devePublicarEventoComSucesso() {
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, List.of(
                new PedidoPagoEvento.ItemPedido("Hamburguer", 2)
        ));

        publisher.publicarPedidoPago(evento);
        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    @Test
    void deveLogarMensagemAoPublicar() {
        PedidoPagoEvento evento = new PedidoPagoEvento(456L, List.of(
                new PedidoPagoEvento.ItemPedido("Refrigerante", 1)
        ));

        publisher.publicarPedidoPago(evento);
        verify(sqsTemplate).send(any(Consumer.class));
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoEnviarSqs() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(789L, List.of(
                new PedidoPagoEvento.ItemPedido("Erro SQS", 1)
        ));

        when(objectMapper.writeValueAsString(any())).thenReturn("{\"idPedido\":789}");
        doThrow(new RuntimeException("Erro SQS"))
                .when(sqsTemplate)
                .send(any(Consumer.class));

        assertThrows(RuntimeException.class, () -> publisher.publicarPedidoPago(evento));
        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    @Test
    void deveLancarExcecaoQuandoFalharSerializacaoJson() throws Exception {
        // Setup: forçar falha na serialização para cair no catch
        PedidoPagoEvento evento = new PedidoPagoEvento(42L, List.of());

        when(objectMapper.writeValueAsString(any()))
                .thenThrow(new RuntimeException("Erro serialização"));

        assertThrows(RuntimeException.class, () -> publisher.publicarPedidoPago(evento));
        // Não deve nem chegar a chamar SQS pois falha antes!
        verify(sqsTemplate, never()).send(any(Consumer.class));
    }
}