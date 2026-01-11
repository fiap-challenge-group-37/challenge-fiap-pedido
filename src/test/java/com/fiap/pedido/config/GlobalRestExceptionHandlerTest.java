package com.fiap.pedido.config;

import com.fiap.pedido.config.exception.GlobalRestExceptionHandler;
import com.fiap.pedido.config.exception.dto.ErrorResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalRestExceptionHandlerTest {

    GlobalRestExceptionHandler handler = new GlobalRestExceptionHandler();

    @Test
    void deveRetornarUnprocessableEntityParaValidationException() {
        // Mock da exceção
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class, RETURNS_DEEP_STUBS);
        when(ex.getBindingResult().getFieldErrors()).thenReturn(java.util.List.of());

        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/test");

        ResponseEntity<ErrorResponseDTO> resp = handler.handleValidationExceptions(ex, request);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resp.getStatusCode());
        assertEquals("/test", resp.getBody().getPath());
        assertNotNull(resp.getBody().getTimestamp());
    }

    @Test
    void deveRetornarBadRequestParaIllegalArgument() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/arg");

        ResponseEntity<ErrorResponseDTO> resp = handler.handleIllegalArgumentException(
                new IllegalArgumentException("argumento invalido"), request);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("argumento invalido", resp.getBody().getMessage());
        assertEquals("/arg", resp.getBody().getPath());
    }

    @Test
    void deveRetornarErroGenericoParaExcecao() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/fail");

        ResponseEntity<ErrorResponseDTO> resp = handler.handleGenericException(
                new RuntimeException("explodeu!"), request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertTrue(resp.getBody().getMessage().contains("Ocorreu um erro interno"));
        assertEquals("/fail", resp.getBody().getPath());
    }
}