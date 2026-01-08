package com.fiap.produto.adapters.in.dto;

import com.fiap.pedido.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.pedido.produto.domain.entities.Categoria;
import com.fiap.pedido. produto.domain.entities.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter. api.Assertions.*;

class ProdutoDTOTest {

    @Test
    void deveConverterDeDomainParaDTO() {
        Produto produto = new Produto(
                1L,
                "Hamburguer",
                Categoria.LANCHE,
                BigDecimal.valueOf(20.00),
                "Hamburguer artesanal"
        );

        ProdutoDTO dto = ProdutoDTO.fromDomain(produto);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Hamburguer", dto.getNome());
        assertEquals("LANCHE", dto.getCategoria());
        assertEquals(BigDecimal.valueOf(20.00), dto.getPreco());
        assertEquals("Hamburguer artesanal", dto.getDescricao());
    }

    @Test
    void deveRetornarNullQuandoProdutoNull() {
        ProdutoDTO dto = ProdutoDTO.fromDomain(null);

        assertNull(dto);
    }

    @Test
    void deveConverterDeDTOParaDomain() {
        ProdutoDTO dto = new ProdutoDTO(
                null,
                "Refrigerante",
                "BEBIDA",
                BigDecimal.valueOf(5.00),
                "Refrigerante 350ml"
        );

        Produto produto = dto.toDomain();

        assertNotNull(produto);
        assertNull(produto.getId());
        assertEquals("Refrigerante", produto. getNome());
        assertEquals(Categoria.BEBIDA, produto. getCategoria());
        assertEquals(BigDecimal.valueOf(5.00), produto.getPreco());
        assertEquals("Refrigerante 350ml", produto.getDescricao());
    }

    @Test
    void deveManterIdQuandoPresente() {
        ProdutoDTO dto = new ProdutoDTO(
                99L,
                "Batata",
                "ACOMPANHAMENTO",
                BigDecimal.valueOf(8.00),
                "Batata frita"
        );

        Produto produto = dto.toDomain();

        assertEquals(99L, produto.getId());
    }

    @Test
    void deveSetarCamposCorretamente() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(1L);
        dto.setNome("Sorvete");
        dto.setCategoria("SOBREMESA");
        dto.setPreco(BigDecimal.valueOf(7.00));
        dto.setDescricao("Sorvete de chocolate");

        assertEquals(1L, dto.getId());
        assertEquals("Sorvete", dto.getNome());
        assertEquals("SOBREMESA", dto. getCategoria());
        assertEquals(BigDecimal.valueOf(7.00), dto.getPreco());
        assertEquals("Sorvete de chocolate", dto.getDescricao());
    }
}