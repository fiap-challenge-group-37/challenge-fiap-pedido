package com.fiap.pedido.config.exception.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseDTOTest {

    @Test
    void gettersDevemFuncionar() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponseDTO dto = new ErrorResponseDTO(now, 422, "Validation Error", "Fail!", "/xpto");

        assertEquals(now, dto.getTimestamp());
        assertEquals(422, dto.getStatus());
        assertEquals("Validation Error", dto.getError());
        assertEquals("Fail!", dto.getMessage());
        assertEquals("/xpto", dto.getPath());
    }
}