package com.fiap.produto.domain.entities;

import com.fiap.pedido.produto.domain.entities.Categoria;
import org.junit.jupiter. api.Test;

import static org.junit.jupiter.api. Assertions.*;

class CategoriaTest {

    @Test
    void deveConterTodasAsCategoriasEsperadas() {
        Categoria[] categorias = Categoria.values();

        assertEquals(4, categorias.length);
        assertTrue(java.util.Arrays. asList(categorias).contains(Categoria.LANCHE));
        assertTrue(java.util.Arrays.asList(categorias).contains(Categoria.ACOMPANHAMENTO));
        assertTrue(java. util.Arrays.asList(categorias).contains(Categoria. BEBIDA));
        assertTrue(java.util.Arrays.asList(categorias).contains(Categoria. SOBREMESA));
    }

    @Test
    void deveConverterStringParaCategoria() {
        assertEquals(Categoria.LANCHE, Categoria.fromString("LANCHE"));
        assertEquals(Categoria. ACOMPANHAMENTO, Categoria. fromString("ACOMPANHAMENTO"));
        assertEquals(Categoria. BEBIDA, Categoria.fromString("BEBIDA"));
        assertEquals(Categoria.SOBREMESA, Categoria.fromString("SOBREMESA"));
    }

    @Test
    void deveSerCaseInsensitive() {
        assertEquals(Categoria.LANCHE, Categoria.fromString("lanche"));
        assertEquals(Categoria.LANCHE, Categoria.fromString("LANCHE"));
        assertEquals(Categoria.LANCHE, Categoria.fromString("LaNcHe"));
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaInvalida() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException. class,
                () -> Categoria.fromString("CATEGORIA_INVALIDA")
        );

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Categoria inválida"));
        assertTrue(exception.getMessage().contains("CATEGORIA_INVALIDA"));
    }

    @Test
    void deveLancarExcecaoQuandoStringVazia() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Categoria.fromString("")
        );

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Categoria inválida"));
    }

    @Test
    void deveLancarExcecaoQuandoStringNull() {
        assertThrows(
                IllegalArgumentException. class,
                () -> Categoria.fromString(null)
        );
    }

    @Test
    void deveRetornarNomeCorreto() {
        assertEquals("LANCHE", Categoria. LANCHE.name());
        assertEquals("ACOMPANHAMENTO", Categoria.ACOMPANHAMENTO.name());
        assertEquals("BEBIDA", Categoria.BEBIDA.name());
        assertEquals("SOBREMESA", Categoria.SOBREMESA. name());
    }
}