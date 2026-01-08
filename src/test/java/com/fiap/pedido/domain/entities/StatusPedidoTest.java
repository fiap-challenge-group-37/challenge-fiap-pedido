package com.fiap.pedido.domain.entities;

import com.fiap.pedido.pedido.domain.entities.StatusPedido;
import org.junit.jupiter.api. Test;

import static org.junit.jupiter. api.Assertions.*;

class StatusPedidoTest {

    @Test
    void deveRetornarDescricaoCorreta() {
        assertEquals("Aguardando pagamento", StatusPedido.AGUARDANDO_PAGAMENTO. getDescricao());
        assertEquals("Recebido", StatusPedido.RECEBIDO.getDescricao());
        assertEquals("Em preparação", StatusPedido. EM_PREPARACAO.getDescricao());
        assertEquals("Pronto", StatusPedido. PRONTO.getDescricao());
        assertEquals("Finalizado", StatusPedido. FINALIZADO.getDescricao());
    }

    @Test
    void deveConverterStringParaStatusPorNome() {
        assertEquals(StatusPedido.RECEBIDO, StatusPedido. fromString("RECEBIDO"));
        assertEquals(StatusPedido.EM_PREPARACAO, StatusPedido.fromString("EM_PREPARACAO"));
        assertEquals(StatusPedido.PRONTO, StatusPedido.fromString("PRONTO"));
        assertEquals(StatusPedido.FINALIZADO, StatusPedido.fromString("FINALIZADO"));
    }

    @Test
    void deveConverterStringParaStatusPorDescricao() {
        assertEquals(StatusPedido. RECEBIDO, StatusPedido.fromString("Recebido"));
        assertEquals(StatusPedido.EM_PREPARACAO, StatusPedido.fromString("Em preparação"));
        assertEquals(StatusPedido.PRONTO, StatusPedido.fromString("Pronto"));
        assertEquals(StatusPedido.FINALIZADO, StatusPedido.fromString("Finalizado"));
    }

    @Test
    void deveSerCaseInsensitive() {
        assertEquals(StatusPedido.RECEBIDO, StatusPedido. fromString("recebido"));
        assertEquals(StatusPedido. RECEBIDO, StatusPedido. fromString("RECEBIDO"));
        assertEquals(StatusPedido. RECEBIDO, StatusPedido. fromString("ReCeBiDo"));
    }

    @Test
    void deveLancarExcecaoQuandoStatusInvalido() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatusPedido.fromString("STATUS_INVALIDO")
        );

        assertTrue(exception.getMessage().contains("Nenhum status encontrado"));
        assertTrue(exception.getMessage().contains("STATUS_INVALIDO"));
        assertTrue(exception.getMessage().contains("AGUARDANDO_PAGAMENTO"));
    }

    @Test
    void deveLancarExcecaoQuandoStringVazia() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatusPedido.fromString("")
        );

        assertTrue(exception.getMessage().contains("Nenhum status encontrado"));
    }

    @Test
    void deveLancarExcecaoQuandoStringNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> StatusPedido.fromString(null)
        );
    }

    @Test
    void deveConterTodosOsStatusEsperados() {
        StatusPedido[] valores = StatusPedido.values();

        assertEquals(5, valores.length);
        assertTrue(java.util.Arrays.asList(valores).contains(StatusPedido.AGUARDANDO_PAGAMENTO));
        assertTrue(java.util.Arrays.asList(valores).contains(StatusPedido.RECEBIDO));
        assertTrue(java.util.Arrays.asList(valores).contains(StatusPedido.EM_PREPARACAO));
        assertTrue(java.util.Arrays.asList(valores).contains(StatusPedido.PRONTO));
        assertTrue(java.util.Arrays. asList(valores).contains(StatusPedido.FINALIZADO));
    }
}