package com.fiap.challenge.pedido.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class StatusPedidoTest {

    @ParameterizedTest
    @CsvSource({
            "Recebido, RECEBIDO",
            "Em preparação, EM_PREPARACAO",
            "Pronto, PRONTO",
            "Finalizado, FINALIZADO",
            "recebido, RECEBIDO", // Test case insensitivity for description
            "EM_PREPARACAO, EM_PREPARACAO" // Test case with enum name
    })
    @DisplayName("Deve converter String para StatusPedido com sucesso")
    void deveConverterStringParaStatusPedidoComSucesso(String input, StatusPedido expected) {
        assertEquals(expected, StatusPedido.fromString(input));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para string de status inválida")
    void deveLancarExcecaoParaStringStatusInvalida() {
        String invalido = "INVALIDO";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            StatusPedido.fromString(invalido);
        });
        assertEquals("Nenhum status encontrado com o texto: " + invalido + ". Valores permitidos: RECEBIDO, EM_PREPARACAO, PRONTO, FINALIZADO.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar a descrição correta")
    void deveRetornarDescricaoCorreta() {
        assertEquals("Recebido", StatusPedido.RECEBIDO.getDescricao());
        assertEquals("Em preparação", StatusPedido.EM_PREPARACAO.getDescricao());
        assertEquals("Pronto", StatusPedido.PRONTO.getDescricao());
        assertEquals("Finalizado", StatusPedido.FINALIZADO.getDescricao());
    }
}
