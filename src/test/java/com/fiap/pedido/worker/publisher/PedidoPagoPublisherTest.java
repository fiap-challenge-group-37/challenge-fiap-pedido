package com.fiap.pedido.worker.publisher;

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

    // ObjectMapper não é mais usado na classe, então removemos o Mock dele.

    @InjectMocks
    private PedidoPagoPublisher publisher;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(publisher, "queueName", "pedido-pago-queue");
    }

    @Test
    void devePublicarEventoComSucesso() {
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, List.of(
                new PedidoPagoEvento.ItemPedido("Hamburguer", 2)
        ));

        // Executa
        publisher.publicarPedidoPago(evento);

        // Verifica se o método send foi chamado passando um Consumer (a lambda to -> ...)
        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    @Test
    void deveLogarMensagemAoPublicar() {
        PedidoPagoEvento evento = new PedidoPagoEvento(456L, List.of(
                new PedidoPagoEvento.ItemPedido("Refrigerante", 1)
        ));

        assertDoesNotThrow(() -> publisher.publicarPedidoPago(evento));

        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoEnviarSqs() {
        PedidoPagoEvento evento = new PedidoPagoEvento(789L, List.of(
                new PedidoPagoEvento.ItemPedido("Erro SQS", 1)
        ));

        // Simula erro genérico ao chamar o send (cobre tanto erro de conexão quanto de serialização interna do SQS)
        doThrow(new RuntimeException("Erro SQS"))
                .when(sqsTemplate).send(any(Consumer.class));

        assertThrows(RuntimeException.class, () -> publisher.publicarPedidoPago(evento));

        verify(sqsTemplate, times(1)).send(any(Consumer.class));
    }

    // O teste 'deveLancarExcecaoQuandoFalharSerializacaoJson' foi removido
    // pois a responsabilidade de serialização agora é interna do SqsTemplate
    // e é coberta pelo teste de exceção genérica acima.
}