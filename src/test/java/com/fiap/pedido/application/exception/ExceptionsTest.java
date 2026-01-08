package com.fiap.pedido.application.exception;

import com.fiap.pedido.pedido.application.exception.PedidoNaoEncontradoException;
import com.fiap.pedido.pedido.application.exception.ValidacaoPedidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api. Assertions.*;

class ExceptionsTest {

    @Test
    void deveCriarPedidoNaoEncontradoExceptionComMensagem() {
        String mensagem = "Pedido não encontrado";
        PedidoNaoEncontradoException exception = new PedidoNaoEncontradoException(mensagem);

        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void deveCriarPedidoNaoEncontradoExceptionComMensagemECausa() {
        String mensagem = "Pedido não encontrado";
        Throwable causa = new RuntimeException("Causa raiz");
        PedidoNaoEncontradoException exception = new PedidoNaoEncontradoException(mensagem, causa);

        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }

    @Test
    void deveCriarValidacaoPedidoExceptionComMensagem() {
        String mensagem = "Validação falhou";
        ValidacaoPedidoException exception = new ValidacaoPedidoException(mensagem);

        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void deveSerRuntimeException() {
        PedidoNaoEncontradoException pedidoNaoEncontrado = new PedidoNaoEncontradoException("Teste");
        ValidacaoPedidoException validacao = new ValidacaoPedidoException("Teste");

        assertTrue(pedidoNaoEncontrado instanceof RuntimeException);
        assertTrue(validacao instanceof RuntimeException);
    }
}