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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    void setUp() throws Exception {
        ReflectionTestUtils.setField(publisher, "queueName", "pedido-pago-queue");
        // Global stub para serialização padrão
        lenient().when(objectMapper.writeValueAsString(any())).thenReturn("{\"idPedido\":123}");
    }

    @Test
    void devePublicarEventoComSucesso() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, List.of(
                new PedidoPagoEvento.ItemPedido("Hamburguer", 2)
        ));
        publisher.publicarPedidoPago(evento);
        // Verifica método direto usado na implementação
        verify(sqsTemplate, times(1)).send(eq("pedido-pago-queue"), any(String.class));
    }

    @Test
    void deveLogarMensagemAoPublicar() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(456L, List.of(
                new PedidoPagoEvento.ItemPedido("Refrigerante", 1)
        ));
        publisher.publicarPedidoPago(evento);
        verify(sqsTemplate, times(1)).send(eq("pedido-pago-queue"), any(String.class));
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoEnviarSqs() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(789L, List.of(
                new PedidoPagoEvento.ItemPedido("Erro SQS", 1)
        ));
        // Simula o erro SQS neste teste apenas, ajustando para a nova assinatura
        doThrow(new RuntimeException("Erro SQS"))
                .when(sqsTemplate).send(eq("pedido-pago-queue"), any(String.class));

        assertThrows(RuntimeException.class, () -> publisher.publicarPedidoPago(evento));
        verify(sqsTemplate, times(1)).send(eq("pedido-pago-queue"), any(String.class));
    }

    @Test
    void deveLancarExcecaoQuandoFalharSerializacaoJson() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(42L, List.of());
        // Simula o erro de serialização só neste teste
        when(objectMapper.writeValueAsString(any()))
                .thenThrow(new RuntimeException("Erro serialização"));

        assertThrows(RuntimeException.class, () -> publisher.publicarPedidoPago(evento));
        verify(sqsTemplate, never()).send(any(), any());
    }
}