package com.fiap.challenge.produto.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import static org.junit.jupiter.api.Assertions.*;

class CategoriaTest {

    @ParameterizedTest
    @CsvSource({
            "Lanche, LANCHE",
            "Acompanhamento, ACOMPANHAMENTO",
            "Bebida, BEBIDA",
            "Sobremesa, SOBREMESA",
            "lanche, LANCHE", // Test case insensitivity for description
            "ACOMPANHAMENTO, ACOMPANHAMENTO" // Test case with enum name
    })
    @DisplayName("Deve converter String para Categoria com sucesso")
    void deveConverterStringParaCategoriaComSucesso(String input, Categoria expected) {
        assertEquals(expected, Categoria.fromString(input));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para string de categoria inválida")
    void deveLancarExcecaoParaStringCategoriaInvalida() {
        String invalido = "INVALIDA";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Categoria.fromString(invalido);
        });
        assertEquals("Nenhuma categoria encontrada com o texto: " + invalido, exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar a descrição correta")
    void deveRetornarDescricaoCorreta() {
        assertEquals("Lanche", Categoria.LANCHE.getDescricao());
        assertEquals("Acompanhamento", Categoria.ACOMPANHAMENTO.getDescricao());
        assertEquals("Bebida", Categoria.BEBIDA.getDescricao());
        assertEquals("Sobremesa", Categoria.SOBREMESA.getDescricao());
    }
}
