package com.fiap.challenge.produto.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    @Test
    @DisplayName("Deve criar Produto com sucesso usando construtor principal")
    void deveCriarProdutoComSucessoConstrutorPrincipal() {
        Produto produto = new Produto("Hamburguer", Categoria.LANCHE, BigDecimal.TEN, "Delicioso hamburguer");
        assertNotNull(produto);
        assertEquals("Hamburguer", produto.getNome());
        assertEquals(Categoria.LANCHE, produto.getCategoria());
        assertEquals(BigDecimal.TEN, produto.getPreco());
        assertEquals("Delicioso hamburguer", produto.getDescricao());
        assertNull(produto.getId()); // ID não é definido neste construtor
    }

    @Test
    @DisplayName("Deve criar Produto com sucesso usando construtor com ID")
    void deveCriarProdutoComSucessoConstrutorComId() {
        Produto produto = new Produto(1L, "Batata Frita", Categoria.ACOMPANHAMENTO, BigDecimal.valueOf(5.50), "Crocante");
        assertNotNull(produto);
        assertEquals(1L, produto.getId());
        assertEquals("Batata Frita", produto.getNome());
        assertEquals(Categoria.ACOMPANHAMENTO, produto.getCategoria());
        assertEquals(BigDecimal.valueOf(5.50), produto.getPreco());
        assertEquals("Crocante", produto.getDescricao());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para nome nulo no construtor")
    void deveLancarExcecaoParaNomeNuloNoConstrutor() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Produto(null, Categoria.LANCHE, BigDecimal.TEN, "Descricao");
        });
        assertEquals("Nome do produto não pode ser vazio.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para nome vazio no construtor")
    void deveLancarExcecaoParaNomeVazioNoConstrutor() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Produto(" ", Categoria.LANCHE, BigDecimal.TEN, "Descricao");
        });
        assertEquals("Nome do produto não pode ser vazio.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para categoria nula no construtor")
    void deveLancarExcecaoParaCategoriaNulaNoConstrutor() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Produto("Nome", null, BigDecimal.TEN, "Descricao");
        });
        assertEquals("Categoria do produto não pode ser nula.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para preco nulo no construtor")
    void deveLancarExcecaoParaPrecoNuloNoConstrutor() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Produto("Nome", Categoria.LANCHE, null, "Descricao");
        });
        assertEquals("Preço do produto não pode ser nulo ou negativo.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para descricao nula no construtor")
    void deveLancarExcecaoParaDescricaoNulaNoConstrutor() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Produto("Nome", Categoria.LANCHE, BigDecimal.TEN, null);
        });
        assertEquals("Descrição do produto não pode ser vazia.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para descricao vazia no construtor")
    void deveLancarExcecaoParaDescricaoVaziaNoConstrutor() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Produto("Nome", Categoria.LANCHE, BigDecimal.TEN, "  ");
        });
        assertEquals("Descrição do produto não pode ser vazia.", exception.getMessage());
    }

    // Testes para Setters
    @Test
    @DisplayName("Deve setar nome com sucesso")
    void deveSetarNomeComSucesso() {
        Produto produto = new Produto("Nome Antigo", Categoria.LANCHE, BigDecimal.TEN, "Desc Antiga");
        produto.setNome("Nome Novo");
        assertEquals("Nome Novo", produto.getNome());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao setar nome nulo")
    void deveLancarExcecaoAoSetarNomeNulo() {
        Produto produto = new Produto("Nome", Categoria.LANCHE, BigDecimal.TEN, "Desc");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            produto.setNome(null);
        });
        assertEquals("Nome do produto não pode ser vazio.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve setar categoria com sucesso")
    void deveSetarCategoriaComSucesso() {
        Produto produto = new Produto("Nome", Categoria.LANCHE, BigDecimal.TEN, "Desc");
        produto.setCategoria(Categoria.BEBIDA);
        assertEquals(Categoria.BEBIDA, produto.getCategoria());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao setar categoria nula")
    void deveLancarExcecaoAoSetarCategoriaNula() {
        Produto produto = new Produto("Nome", Categoria.LANCHE, BigDecimal.TEN, "Desc");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            produto.setCategoria(null);
        });
        assertEquals("Categoria do produto não pode ser nula.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve setar preco com sucesso")
    void deveSetarPrecoComSucesso() {
        Produto produto = new Produto("Nome", Categoria.LANCHE, BigDecimal.TEN, "Desc");
        produto.setPreco(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, produto.getPreco());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao setar preco nulo")
    void deveLancarExcecaoAoSetarPrecoNulo() {
        Produto produto = new Produto("Nome", Categoria.LANCHE, BigDecimal.TEN, "Desc");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            produto.setPreco(null);
        });
        assertEquals("Preço do produto não pode ser nulo ou negativo.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve setar descricao com sucesso")
    void deveSetarDescricaoComSucesso() {
        Produto produto = new Produto("Nome", Categoria.LANCHE, BigDecimal.TEN, "Desc Antiga");
        produto.setDescricao("Desc Nova");
        assertEquals("Desc Nova", produto.getDescricao());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao setar descricao nula")
    void deveLancarExcecaoAoSetarDescricaoNula() {
        Produto produto = new Produto("Nome", Categoria.LANCHE, BigDecimal.TEN, "Desc");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            produto.setDescricao(null);
        });
        assertEquals("Descrição do produto não pode ser vazia.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve setar id com sucesso")
    void deveSetarIdComSucesso() {
        Produto produto = new Produto("Nome", Categoria.LANCHE, BigDecimal.TEN, "Desc");
        produto.setId(123L);
        assertEquals(123L, produto.getId());
    }
}
