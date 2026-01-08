package com.fiap.produto.application.service;

import com.fiap. pedido.produto.adapters. in.http.dto.ProdutoDTO;
import com.fiap.pedido.produto.application.exception.ApplicationServiceException;
import com.fiap.pedido.produto.application.exception.ProdutoNaoEncontradoException;
import com.fiap.pedido.produto.application.service.ProdutoApplicationService;
import com.fiap.pedido.produto.domain. entities.Categoria;
import com.fiap.pedido.produto.domain.entities.Produto;
import com.fiap.pedido.produto.domain.port. ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org. junit.jupiter.api.Test;
import org.junit.jupiter. api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org. mockito.Mock;
import org.mockito.junit.jupiter. MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit. jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito. Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoApplicationServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoApplicationService service;

    private ProdutoDTO produtoDTO;
    private Produto produtoMock;

    @BeforeEach
    void setUp() {
        produtoDTO = new ProdutoDTO(
                null,
                "Hamburguer",
                "LANCHE",
                BigDecimal.valueOf(20.00),
                "Hamburguer artesanal"
        );

        produtoMock = new Produto(
                1L,
                "Hamburguer",
                Categoria. LANCHE,
                BigDecimal.valueOf(20.00),
                "Hamburguer artesanal"
        );
    }

    @Test
    void deveCriarProdutoComSucesso() {
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoMock);

        Produto resultado = service. executar(produtoDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Hamburguer", resultado.getNome());
        assertEquals(Categoria.LANCHE, resultado.getCategoria());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaInvalidaNaCriacao() {
        produtoDTO.setCategoria("CATEGORIA_INVALIDA");

        ApplicationServiceException exception = assertThrows(
                ApplicationServiceException.class,
                () -> service.executar(produtoDTO)
        );

        assertTrue(exception.getMessage().contains("Categoria inválida fornecida"));
        assertTrue(exception.getMessage().contains("CATEGORIA_INVALIDA"));
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoMock));
        when(produtoRepository. save(any(Produto.class))).thenReturn(produtoMock);

        ProdutoDTO dtoAtualizado = new ProdutoDTO(
                1L,
                "Hamburguer Premium",
                "LANCHE",
                BigDecimal.valueOf(25.00),
                "Hamburguer premium"
        );

        Produto resultado = service.executar(1L, dtoAtualizado);

        assertNotNull(resultado);
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontradoParaAtualizar() {
        when(produtoRepository.findById(999L)).thenReturn(Optional. empty());

        ProdutoNaoEncontradoException exception = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> service.executar(999L, produtoDTO)
        );

        assertTrue(exception.getMessage().contains("999"));
        assertTrue(exception.getMessage().contains("não encontrado para atualização"));
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaInvalidaNaAtualizacao() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoMock));
        produtoDTO.setCategoria("CATEGORIA_INVALIDA");

        ApplicationServiceException exception = assertThrows(
                ApplicationServiceException.class,
                () -> service.executar(1L, produtoDTO)
        );

        assertTrue(exception.getMessage().contains("Categoria inválida fornecida"));
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void deveRemoverProdutoComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoMock));
        doNothing().when(produtoRepository).deleteById(1L);

        service.removerPorId(1L);

        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontradoParaRemover() {
        when(produtoRepository. findById(999L)).thenReturn(Optional.empty());

        ProdutoNaoEncontradoException exception = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> service.removerPorId(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        assertTrue(exception.getMessage().contains("não encontrado para remoção"));
        verify(produtoRepository, never()).deleteById(any());
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoMock));

        Produto resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontradoPorId() {
        when(produtoRepository.findById(999L)).thenReturn(Optional. empty());

        ProdutoNaoEncontradoException exception = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> service.buscarPorId(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        assertTrue(exception.getMessage().contains("não encontrado"));
    }

    @Test
    void deveBuscarProdutosPorCategoria() {
        when(produtoRepository.findByCategoria(Categoria.LANCHE))
                .thenReturn(Arrays.asList(produtoMock));

        List<Produto> resultado = service.executar(Categoria.LANCHE);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(Categoria.LANCHE, resultado. get(0).getCategoria());
        verify(produtoRepository, times(1)).findByCategoria(Categoria.LANCHE);
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumProdutoNaCategoria() {
        when(produtoRepository.findByCategoria(Categoria.SOBREMESA))
                .thenReturn(Collections.emptyList());

        List<Produto> resultado = service. executar(Categoria.SOBREMESA);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveListarTodosProdutos() {
        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produtoMock));

        List<Produto> resultado = service.executar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumProduto() {
        when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Produto> resultado = service.executar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveAtualizarTodosCamposDoProduto() {
        Produto produtoExistente = new Produto(
                1L,
                "Nome Antigo",
                Categoria. BEBIDA,
                BigDecimal. valueOf(5.00),
                "Descrição antiga"
        );

        when(produtoRepository.findById(1L)).thenReturn(Optional. of(produtoExistente));
        when(produtoRepository. save(any(Produto.class))).thenAnswer(i -> i.getArguments()[0]);

        ProdutoDTO dtoAtualizado = new ProdutoDTO(
                1L,
                "Nome Novo",
                "LANCHE",
                BigDecimal.valueOf(30.00),
                "Descrição nova"
        );

        Produto resultado = service.executar(1L, dtoAtualizado);

        assertEquals("Nome Novo", resultado.getNome());
        assertEquals(Categoria. LANCHE, resultado.getCategoria());
        assertEquals(BigDecimal.valueOf(30.00), resultado.getPreco());
        assertEquals("Descrição nova", resultado.getDescricao());
    }
}