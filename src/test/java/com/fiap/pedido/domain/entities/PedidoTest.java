package com.fiap.pedido.domain.entities;

import com.fiap.pedido.pedido.domain.entities.ItemPedido;
import com.fiap.pedido.pedido.domain.entities.Pedido;
import com.fiap.pedido.pedido.domain.entities.StatusPedido;
import org.junit.jupiter.api. BeforeEach;
import org. junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time. LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit. jupiter.api.Assertions.*;

class PedidoTest {

    private List<ItemPedido> itens;

    @BeforeEach
    void setUp() {
        itens = Arrays.asList(
                new ItemPedido(1L, "Hamburguer", 2, BigDecimal.valueOf(20.00)),
                new ItemPedido(2L, "Refrigerante", 1, BigDecimal.valueOf(5.00))
        );
    }

    @Test
    void deveCriarPedidoComSucesso() {
        Pedido pedido = new Pedido(1L, itens);

        assertNotNull(pedido);
        assertEquals(1L, pedido.getClienteId());
        assertEquals(2, pedido.getItens().size());
        assertEquals(BigDecimal.valueOf(45.00), pedido.getValorTotal());
        assertEquals(StatusPedido.AGUARDANDO_PAGAMENTO, pedido. getStatus());
        assertNotNull(pedido.getDataCriacao());
        assertNotNull(pedido.getDataAtualizacao());
    }

    @Test
    void deveCriarPedidoComTodosCampos() {
        LocalDateTime agora = LocalDateTime.now();
        Pedido pedido = new Pedido(
                1L,
                2L,
                itens,
                BigDecimal.valueOf(100.00),
                StatusPedido.RECEBIDO,
                agora,
                agora,
                "PED-001",
                "qr-code-123"
        );

        assertEquals(1L, pedido.getId());
        assertEquals(2L, pedido.getClienteId());
        assertEquals(BigDecimal.valueOf(100.00), pedido.getValorTotal());
        assertEquals(StatusPedido.RECEBIDO, pedido.getStatus());
        assertEquals("qr-code-123", pedido.getQrCode());
    }

    @Test
    void deveCalcularValorTotalAutomaticamente() {
        Pedido pedido = new Pedido(1L, itens);

        // 2 * 20. 00 + 1 * 5.00 = 45.00
        assertEquals(BigDecimal.valueOf(45.00), pedido.getValorTotal());
    }

    @Test
    void deveAdicionarItemAoPedido() {
        Pedido pedido = new Pedido(1L, itens);
        LocalDateTime dataAntes = pedido.getDataAtualizacao();

        ItemPedido novoItem = new ItemPedido(3L, "Batata", 1, BigDecimal.valueOf(8.00));
        pedido.adicionarItem(novoItem);

        assertEquals(3, pedido.getItens().size());
        assertEquals(BigDecimal.valueOf(53.00), pedido.getValorTotal());
        assertTrue(pedido.getDataAtualizacao().isAfter(dataAntes) ||
                pedido.getDataAtualizacao().isEqual(dataAntes));
    }

    @Test
    void deveAtualizarStatusDoPedido() {
        Pedido pedido = new Pedido(1L, itens);
        LocalDateTime dataAntes = pedido.getDataAtualizacao();

        pedido.atualizarStatus(StatusPedido. EM_PREPARACAO);

        assertEquals(StatusPedido. EM_PREPARACAO, pedido.getStatus());
        assertTrue(pedido.getDataAtualizacao().isAfter(dataAntes) ||
                pedido.getDataAtualizacao().isEqual(dataAntes));
    }

    @Test
    void deveAtualizarClienteId() {
        Pedido pedido = new Pedido(1L, itens);

        pedido.setClienteId(999L);

        assertEquals(999L, pedido.getClienteId());
    }

    @Test
    void deveLancarExcecaoQuandoItensVazio() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Pedido(1L, new ArrayList<>())
        );

        assertEquals("Pedido deve conter ao menos um item.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoItensNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Pedido(1L, null)
        );

        assertEquals("Pedido deve conter ao menos um item.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoAdicionarItemNulo() {
        Pedido pedido = new Pedido(1L, itens);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException. class,
                () -> pedido.adicionarItem(null)
        );

        assertEquals("Item não pode ser nulo.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNovoStatusNulo() {
        Pedido pedido = new Pedido(1L, itens);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pedido.atualizarStatus(null)
        );

        assertEquals("Novo status não pode ser nulo.", exception.getMessage());
    }

    @Test
    void deveUsarValorTotalFornecidoQuandoNaoNulo() {
        Pedido pedido = new Pedido(
                1L,
                1L,
                itens,
                BigDecimal.valueOf(999.99),
                StatusPedido.RECEBIDO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null
        );

        assertEquals(BigDecimal.valueOf(999.99), pedido.getValorTotal());
    }

    @Test
    void deveRecalcularValorTotalQuandoNull() {
        Pedido pedido = new Pedido(
                1L,
                1L,
                itens,
                null,
                StatusPedido. RECEBIDO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null
        );

        assertEquals(BigDecimal.valueOf(45.00), pedido.getValorTotal());
    }

    @Test
    void deveManterListaDeItensIndependente() {
        List<ItemPedido> itensOriginais = new ArrayList<>(itens);
        Pedido pedido = new Pedido(1L, itensOriginais);

        itensOriginais.clear();

        assertEquals(2, pedido.getItens().size());
    }
}