package com.fiap.pedido.domain.entities;

import com.fiap.pedido.pedido.domain.entities.ItemPedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter. api.Assertions.*;

class ItemPedidoTest {

    @Test
    void deveCriarItemPedidoComSucesso() {
        ItemPedido item = new ItemPedido(
                1L,
                "Hamburguer",
                2,
                BigDecimal.valueOf(20.00)
        );

        assertNotNull(item);
        assertEquals(1L, item.getProdutoId());
        assertEquals("Hamburguer", item.getNomeProduto());
        assertEquals(2, item.getQuantidade());
        assertEquals(BigDecimal. valueOf(20.00), item.getPrecoUnitario());
        assertEquals(BigDecimal.valueOf(40.00), item.getPrecoTotal());
        assertNull(item.getId());
    }

    @Test
    void deveCriarItemPedidoComTodosCampos() {
        ItemPedido item = new ItemPedido(
                10L,
                1L,
                "Hamburguer",
                2,
                BigDecimal.valueOf(20.00),
                BigDecimal.valueOf(40.00)
        );

        assertEquals(10L, item.getId());
        assertEquals(1L, item.getProdutoId());
        assertEquals("Hamburguer", item.getNomeProduto());
        assertEquals(2, item. getQuantidade());
        assertEquals(BigDecimal.valueOf(20.00), item.getPrecoUnitario());
        assertEquals(BigDecimal.valueOf(40.00), item.getPrecoTotal());
    }

    @Test
    void deveCalcularPrecoTotalAutomaticamente() {
        ItemPedido item = new ItemPedido(
                1L,
                "Refrigerante",
                3,
                BigDecimal.valueOf(5.00)
        );

        assertEquals(BigDecimal.valueOf(15.00), item.getPrecoTotal());
    }

    @Test
    void deveRecalcularPrecoTotalSePrecoTotalForNull() {
        ItemPedido item = new ItemPedido(
                10L,
                1L,
                "Batata",
                2,
                BigDecimal.valueOf(8.00),
                null
        );

        assertEquals(BigDecimal.valueOf(16.00), item.getPrecoTotal());
    }

    @Test
    void devePermitirSetarId() {
        ItemPedido item = new ItemPedido(
                1L,
                "Hamburguer",
                1,
                BigDecimal.valueOf(20.00)
        );

        item.setId(99L);
        assertEquals(99L, item.getId());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoIdNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ItemPedido(null, "Hamburguer", 2, BigDecimal.valueOf(20.00))
        );

        assertEquals("ID do produto não pode ser nulo.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNomeProdutoNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ItemPedido(1L, null, 2, BigDecimal.valueOf(20.00))
        );

        assertEquals("Nome do produto não pode ser vazio.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNomeProdutoVazio() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ItemPedido(1L, "   ", 2, BigDecimal. valueOf(20.00))
        );

        assertEquals("Nome do produto não pode ser vazio.", exception. getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeNula() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ItemPedido(1L, "Hamburguer", null, BigDecimal.valueOf(20.00))
        );

        assertEquals("Quantidade deve ser positiva.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeZero() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ItemPedido(1L, "Hamburguer", 0, BigDecimal.valueOf(20.00))
        );

        assertEquals("Quantidade deve ser positiva.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeNegativa() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ItemPedido(1L, "Hamburguer", -1, BigDecimal.valueOf(20.00))
        );

        assertEquals("Quantidade deve ser positiva.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPrecoUnitarioNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ItemPedido(1L, "Hamburguer", 2, null)
        );

        assertEquals("Preço unitário não pode ser nulo ou negativo.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPrecoUnitarioNegativo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ItemPedido(1L, "Hamburguer", 2, BigDecimal.valueOf(-10.00))
        );

        assertEquals("Preço unitário não pode ser nulo ou negativo.", exception.getMessage());
    }

    @Test
    void deveAceitarPrecoUnitarioZero() {
        ItemPedido item = new ItemPedido(
                1L,
                "Item Grátis",
                1,
                BigDecimal. ZERO
        );

        assertEquals(BigDecimal.ZERO, item.getPrecoUnitario());
        assertEquals(BigDecimal.ZERO, item.getPrecoTotal());
    }
}