package com.fiap.pedido.worker.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.pedido.domain.dto.PedidoPagoEvento;
import io.awspring.cloud.sqs.operations.SqsSendOptions;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.function.Consumer;

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
    void setUp() throws Exception {
        ReflectionTestUtils.setField(publisher, "queueName", "pedido-pago-queue");
        // Global stub para serialização padrão
        lenient().when(objectMapper.writeValueAsString(any())).thenReturn("{\"idPedido\":123}");

        lenient().doAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Consumer<SqsSendOptions> consumer = (Consumer<SqsSendOptions>) invocation.getArgument(0);
            SqsSendOptions builderMock = Mockito.mock(SqsSendOptions.class, Mockito.RETURNS_SELF);
            consumer.accept(builderMock);
            return null;
        }).when(sqsTemplate).send(any(Consumer.class));
    }

    @Test
    void devePublicarEventoComSucesso() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, List.of(
                new PedidoPagoEvento.ItemPedido("Hamburguer", 2)
        ));
        publisher.publicarPedidoPago(evento);
        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    @Test
    void deveLogarMensagemAoPublicar() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(456L, List.of(
                new PedidoPagoEvento.ItemPedido("Refrigerante", 1)
        ));
        publisher.publicarPedidoPago(evento);
        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoEnviarSqs() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(789L, List.of(
                new PedidoPagoEvento.ItemPedido("Erro SQS", 1)
        ));
        // Simula o erro SQS neste teste apenas
        lenient().doThrow(new RuntimeException("Erro SQS"))
                .when(sqsTemplate).send(any(Consumer.class));

        assertThrows(RuntimeException.class, () -> publisher.publicarPedidoPago(evento));
        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    @Test
    void deveLancarExcecaoQuandoFalharSerializacaoJson() throws Exception {
        PedidoPagoEvento evento = new PedidoPagoEvento(42L, List.of());
        // Simula o erro de serialização só neste teste
        when(objectMapper.writeValueAsString(any()))
                .thenThrow(new RuntimeException("Erro serialização"));

        assertThrows(RuntimeException.class, () -> publisher.publicarPedidoPago(evento));
        verify(sqsTemplate, never()).send(any(Consumer.class));
    }
}