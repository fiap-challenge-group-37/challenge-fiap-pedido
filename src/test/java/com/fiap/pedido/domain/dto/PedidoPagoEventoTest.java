package com.fiap.pedido.domain.dto;

import com.fiap.pedido.worker.dto.PedidoPagoEvento;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoPagoEventoTest {

    @Test
    void deveCriarEventoComSucesso() {
        List<PedidoPagoEvento.ItemPedido> itens = Arrays.asList(
                new PedidoPagoEvento.ItemPedido("Hamburguer", 2),
                new PedidoPagoEvento.ItemPedido("Refrigerante", 1)
        );

        PedidoPagoEvento evento = new PedidoPagoEvento(123L, itens);

        assertNotNull(evento);
        assertEquals(123L, evento.idPedido());
        assertEquals(2, evento.itens().size());
    }

    @Test
    void deveCriarItemPedidoComSucesso() {
        PedidoPagoEvento. ItemPedido item = new PedidoPagoEvento.ItemPedido("Hamburguer", 2);

        assertEquals("Hamburguer", item.nome());
        assertEquals(2, item.quantidade());
    }

    @Test
    void deveSerIgualQuandoMesmosValores() {
        List<PedidoPagoEvento.ItemPedido> itens = Arrays.asList(
                new PedidoPagoEvento.ItemPedido("Hamburguer", 2)
        );

        PedidoPagoEvento evento1 = new PedidoPagoEvento(123L, itens);
        PedidoPagoEvento evento2 = new PedidoPagoEvento(123L, itens);

        assertEquals(evento1, evento2);
        assertEquals(evento1.hashCode(), evento2.hashCode());
    }

    @Test
    void deveSerDiferenteQuandoValoresDiferentes() {
        List<PedidoPagoEvento.ItemPedido> itens1 = Arrays.asList(
                new PedidoPagoEvento.ItemPedido("Hamburguer", 2)
        );
        List<PedidoPagoEvento. ItemPedido> itens2 = Arrays.asList(
                new PedidoPagoEvento.ItemPedido("Batata", 1)
        );

        PedidoPagoEvento evento1 = new PedidoPagoEvento(123L, itens1);
        PedidoPagoEvento evento2 = new PedidoPagoEvento(456L, itens2);

        assertNotEquals(evento1, evento2);
    }

    @Test
    void devePermitirItensNull() {
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, null);

        assertNotNull(evento);
        assertEquals(123L, evento.idPedido());
        assertNull(evento.itens());
    }

    @Test
    void devePermitirItensVazio() {
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, Arrays.asList());

        assertNotNull(evento);
        assertEquals(0, evento.itens().size());
    }
}