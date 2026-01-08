package com.fiap.produto.adapters.out.persistence;

import com.fiap.pedido.produto.adapters.out.persistence.ProdutoEntity;
import com.fiap.pedido.produto.adapters.out.persistence.ProdutoJpaRepository;
import com.fiap.pedido.produto.adapters.out.persistence.ProdutoRepositoryDatabase;
import com.fiap.pedido.produto.domain.entities.Categoria;
import com.fiap.pedido. produto.domain.entities.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter. api.Test;
import org. junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api. Assertions.*;
import static org. mockito.ArgumentMatchers. any;
import static org.mockito. Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoRepositoryDatabaseTest {

    @Mock
    private ProdutoJpaRepository jpaRepository;

    @InjectMocks
    private ProdutoRepositoryDatabase repository;

    private ProdutoEntity produtoEntity;
    private Produto produtoDomain;

    @BeforeEach
    void setUp() {
        produtoEntity = new ProdutoEntity(
                "Hamburguer",
                Categoria.LANCHE,
                BigDecimal.valueOf(20.00),
                "Hamburguer artesanal"
        );
        produtoEntity.setId(1L);

        produtoDomain = new Produto(
                null,
                "Hamburguer",
                Categoria.LANCHE,
                BigDecimal.valueOf(20.00),
                "Hamburguer artesanal"
        );
    }

    @Test
    void deveSalvarProduto() {
        when(jpaRepository.save(any(ProdutoEntity.class))).thenReturn(produtoEntity);

        Produto resultado = repository.save(produtoDomain);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Hamburguer", resultado. getNome());
        verify(jpaRepository, times(1)).save(any(ProdutoEntity.class));
    }

    @Test
    void deveSalvarTodosProdutos() {
        List<Produto> produtos = Arrays.asList(produtoDomain);
        when(jpaRepository.saveAll(anyList())).thenReturn(Arrays.asList(produtoEntity));

        List<Produto> resultado = repository.saveAll(produtos);

        assertEquals(1, resultado.size());
        verify(jpaRepository, times(1)).saveAll(anyList());
    }

    @Test
    void deveBuscarProdutoPorId() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(produtoEntity));

        Optional<Produto> resultado = repository. findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(jpaRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarVazioQuandoProdutoNaoEncontrado() {
        when(jpaRepository.findById(999L)).thenReturn(Optional. empty());

        Optional<Produto> resultado = repository.findById(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void deveListarTodosProdutos() {
        when(jpaRepository.findAll()).thenReturn(Arrays.asList(produtoEntity));

        List<Produto> resultado = repository. findAll();

        assertEquals(1, resultado.size());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void deveDeletarProdutoPorId() {
        doNothing().when(jpaRepository).deleteById(1L);

        repository.deleteById(1L);

        verify(jpaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveBuscarProdutosPorCategoria() {
        when(jpaRepository.findByCategoria(Categoria.LANCHE))
                .thenReturn(Arrays.asList(produtoEntity));

        List<Produto> resultado = repository.findByCategoria(Categoria. LANCHE);

        assertEquals(1, resultado.size());
        assertEquals(Categoria.LANCHE, resultado. get(0).getCategoria());
        verify(jpaRepository, times(1)).findByCategoria(Categoria.LANCHE);
    }

    @Test
    void deveContarProdutos() {
        when(jpaRepository.count()).thenReturn(10L);

        long resultado = repository.count();

        assertEquals(10L, resultado);
        verify(jpaRepository, times(1)).count();
    }
}