package com.fiap.produto.adapters.out.persistence;

import com.fiap.pedido.produto.adapters.out.persistence.ProdutoEntity;
import com.fiap. pedido.produto.domain.entities.Categoria;
import com. fiap.pedido.produto. domain.entities.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org. junit.jupiter.api.Assertions.*;

class ProdutoEntityTest {

    @Test
    void deveConverterParaDomain() {
        ProdutoEntity entity = new ProdutoEntity(
                "Hamburguer",
                Categoria. LANCHE,
                BigDecimal.valueOf(20.00),
                "Hamburguer artesanal"
        );
        entity.setId(1L);

        Produto produto = entity.toDomain();

        assertNotNull(produto);
        assertEquals(1L, produto.getId());
        assertEquals("Hamburguer", produto.getNome());
        assertEquals(Categoria.LANCHE, produto.getCategoria());
        assertEquals(BigDecimal.valueOf(20.00), produto.getPreco());
        assertEquals("Hamburguer artesanal", produto. getDescricao());
    }

    @Test
    void deveConverterDeDomain() {
        Produto produto = new Produto(
                1L,
                "Refrigerante",
                Categoria.BEBIDA,
                BigDecimal.valueOf(5.00),
                "Refrigerante 350ml"
        );

        ProdutoEntity entity = ProdutoEntity.fromDomain(produto);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Refrigerante", entity. getNome());
        assertEquals(Categoria.BEBIDA, entity. getCategoria());
        assertEquals(BigDecimal.valueOf(5.00), entity.getPreco());
        assertEquals("Refrigerante 350ml", entity.getDescricao());
    }

    @Test
    void deveConverterDeDomainSemId() {
        Produto produto = new Produto(
                null,
                "Batata",
                Categoria. ACOMPANHAMENTO,
                BigDecimal.valueOf(8.00),
                "Batata frita"
        );

        ProdutoEntity entity = ProdutoEntity.fromDomain(produto);

        assertNull(entity. getId());
        assertEquals("Batata", entity. getNome());
    }
}