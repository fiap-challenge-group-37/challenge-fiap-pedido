package com.fiap.produto.application.exception;

import com.fiap.pedido.produto.application.exception.ApplicationServiceException;
import com.fiap.pedido.produto.application.exception.ProdutoNaoEncontradoException;
import org.junit.jupiter. api.Test;
import org. springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api. Assertions.*;

class ExceptionsTest {

    @Test
    void deveCriarApplicationServiceExceptionComMensagem() {
        String mensagem = "Erro no serviço";
        ApplicationServiceException exception = new ApplicationServiceException(mensagem);

        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void deveCriarApplicationServiceExceptionComMensagemECausa() {
        String mensagem = "Erro no serviço";
        Throwable causa = new RuntimeException("Causa raiz");
        ApplicationServiceException exception = new ApplicationServiceException(mensagem, causa);

        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }

    @Test
    void deveCriarProdutoNaoEncontradoExceptionComMensagem() {
        String mensagem = "Produto não encontrado";
        ProdutoNaoEncontradoException exception = new ProdutoNaoEncontradoException(mensagem);

        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void deveSerRuntimeException() {
        ApplicationServiceException appException = new ApplicationServiceException("Teste");
        ProdutoNaoEncontradoException produtoException = new ProdutoNaoEncontradoException("Teste");

        assertTrue(appException instanceof RuntimeException);
        assertTrue(produtoException instanceof RuntimeException);
    }

    @Test
    void deveTerAnotacaoResponseStatus() {
        ResponseStatus annotation = ProdutoNaoEncontradoException.class.getAnnotation(ResponseStatus.class);

        assertNotNull(annotation);
        assertEquals(HttpStatus.NOT_FOUND, annotation.value());
    }

    @Test
    void deveLancarECapturarApplicationServiceException() {
        try {
            throw new ApplicationServiceException("Erro de teste");
        } catch (ApplicationServiceException e) {
            assertEquals("Erro de teste", e.getMessage());
        }
    }

    @Test
    void deveLancarECapturarProdutoNaoEncontradoException() {
        try {
            throw new ProdutoNaoEncontradoException("Produto não existe");
        } catch (ProdutoNaoEncontradoException e) {
            assertEquals("Produto não existe", e.getMessage());
        }
    }

    @Test
    void devePreservarStackTraceComCausa() {
        Throwable causa = new IllegalArgumentException("Argumento inválido");
        ApplicationServiceException exception = new ApplicationServiceException("Erro wrapper", causa);

        assertNotNull(exception.getStackTrace());
        assertEquals(causa, exception.getCause());
        assertEquals("Argumento inválido", exception.getCause().getMessage());
    }
}