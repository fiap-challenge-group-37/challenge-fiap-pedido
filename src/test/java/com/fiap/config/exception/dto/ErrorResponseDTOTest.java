package com.fiap.challenge.config.exception.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseDTOTest {

    @Test
    void deveCriarErrorResponseDTOECorretamenteRetornarValores() {
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 404;
        String error = "Not Found";
        String message = "Recurso não encontrado";
        String path = "/api/recurso/123";

        ErrorResponseDTO dto = new ErrorResponseDTO(timestamp, status, error, message, path);

        assertEquals(timestamp, dto.getTimestamp());
        assertEquals(status, dto.getStatus());
        assertEquals(error, dto.getError());
        assertEquals(message, dto.getMessage());
        assertEquals(path, dto.getPath());
    }
}
