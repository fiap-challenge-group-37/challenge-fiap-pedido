package com.fiap.produto.domain.entities;

import com.fiap.pedido.produto.domain.entities.Categoria;
import com.fiap.pedido.produto.domain.entities.Produto;
import org. junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter. api.Assertions.*;

class ProdutoTest {

    @Test
    void deveCriarProdutoComTodosCampos() {
        Produto produto = new Produto(
                1L,
                "Hamburguer",
                Categoria.LANCHE,
                BigDecimal.valueOf(20.00),
                "Hamburguer artesanal"
        );

        assertEquals(1L, produto.getId());
        assertEquals("Hamburguer", produto. getNome());
        assertEquals(Categoria.LANCHE, produto. getCategoria());
        assertEquals(BigDecimal.valueOf(20.00), produto.getPreco());
        assertEquals("Hamburguer artesanal", produto.getDescricao());
    }

    @Test
    void deveCriarProdutoSemId() {
        Produto produto = new Produto(
                "Refrigerante",
                Categoria. BEBIDA,
                BigDecimal. valueOf(5.00),
                "Refrigerante 350ml"
        );

        assertNull(produto.getId());
        assertEquals("Refrigerante", produto. getNome());
        assertEquals(Categoria.BEBIDA, produto. getCategoria());
    }

    @Test
    void devePermitirAlterarCampos() {
        Produto produto = new Produto(
                1L,
                "Nome Original",
                Categoria.LANCHE,
                BigDecimal. valueOf(10.00),
                "Descrição original"
        );

        produto.setNome("Nome Alterado");
        produto.setCategoria(Categoria.BEBIDA);
        produto.setPreco(BigDecimal.valueOf(15.00));
        produto.setDescricao("Descrição alterada");

        assertEquals("Nome Alterado", produto.getNome());
        assertEquals(Categoria. BEBIDA, produto.getCategoria());
        assertEquals(BigDecimal.valueOf(15.00), produto.getPreco());
        assertEquals("Descrição alterada", produto.getDescricao());
    }

    @Test
    void deveValidarNomeNaoNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Produto(null, Categoria.LANCHE, BigDecimal.valueOf(10.00), "Descrição")
        );

        assertTrue(exception.getMessage().contains("Nome do produto não pode ser vazio"));
    }

    @Test
    void deveValidarNomeNaoVazio() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Produto("   ", Categoria.LANCHE, BigDecimal.valueOf(10.00), "Descrição")
        );

        assertTrue(exception. getMessage().contains("Nome do produto não pode ser vazio"));
    }

    @Test
    void deveValidarCategoriaNaoNula() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Produto("Nome", null, BigDecimal.valueOf(10.00), "Descrição")
        );

        assertTrue(exception.getMessage().contains("Categoria não pode ser nula"));
    }

    @Test
    void deveValidarPrecoNaoNulo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException. class,
                () -> new Produto("Nome", Categoria. LANCHE, null, "Descrição")
        );

        assertTrue(exception.getMessage().contains("Preço não pode ser nulo"));
    }

    @Test
    void deveValidarPrecoNaoNegativo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Produto("Nome", Categoria.LANCHE, BigDecimal.valueOf(-10.00), "Descrição")
        );

        assertTrue(exception. getMessage().contains("Preço não pode ser negativo"));
    }

    @Test
    void deveAceitarPrecoZero() {
        Produto produto = new Produto(
                "Item Grátis",
                Categoria. LANCHE,
                BigDecimal. ZERO,
                "Item promocional"
        );

        assertEquals(BigDecimal.ZERO, produto. getPreco());
    }
}