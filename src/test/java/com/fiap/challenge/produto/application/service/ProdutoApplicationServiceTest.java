package com.fiap.challenge.produto.application.service;

import com.fiap.challenge.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.challenge.produto.application.exception.ApplicationServiceException;
import com.fiap.challenge.produto.application.exception.ProdutoNaoEncontradoException;
import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;
import com.fiap.challenge.produto.domain.port.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Para habilitar o uso de anotações do Mockito
class ProdutoApplicationServiceTest {

    @Mock // Cria um mock para ProdutoRepository
    private ProdutoRepository produtoRepository;

    @InjectMocks // Cria uma instância de ProdutoApplicationService e injeta os mocks criados com @Mock
    private ProdutoApplicationService produtoApplicationService;

    private Produto produtoValido;
    private ProdutoDTO produtoDTOValido;

    @BeforeEach
    void setUp() {
        // Configuração inicial para cada teste
        produtoValido = new Produto(1L,"Hamburguer Teste", Categoria.LANCHE, BigDecimal.valueOf(10.99), "Descrição Teste");
        produtoDTOValido = new ProdutoDTO(null, "Hamburguer DTO", Categoria.LANCHE.name(), BigDecimal.valueOf(12.50), "Desc DTO");
    }

    @Test
    @DisplayName("Deve criar um produto com sucesso")
    void deveCriarProdutoComSucesso() {
        // Arrange
        Produto produtoSalvo = new Produto(1L, produtoDTOValido.getNome(), Categoria.fromString(produtoDTOValido.getCategoria()), produtoDTOValido.getPreco(), produtoDTOValido.getDescricao());
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        // Act
        Produto resultado = produtoApplicationService.executar(produtoDTOValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(produtoDTOValido.getNome(), resultado.getNome());
        assertEquals(Categoria.fromString(produtoDTOValido.getCategoria()), resultado.getCategoria());
        assertEquals(produtoDTOValido.getPreco(), resultado.getPreco());
        assertEquals(produtoDTOValido.getDescricao(), resultado.getDescricao());
        verify(produtoRepository, times(1)).save(any(Produto.class)); // Verifica se o save foi chamado 1 vez
    }

    @Test
    @DisplayName("Deve lançar ApplicationServiceException ao tentar criar produto com categoria inválida")
    void deveLancarExcecaoAoCriarProdutoComCategoriaInvalida() {
        // Arrange
        ProdutoDTO produtoDTOCategoriaInvalida = new ProdutoDTO(null, "Nome", "CATEGORIA_INVALIDA", BigDecimal.TEN, "Desc");

        // Act & Assert
        ApplicationServiceException exception = assertThrows(ApplicationServiceException.class, () -> {
            produtoApplicationService.executar(produtoDTOCategoriaInvalida);
        });
        assertEquals("Categoria inválida fornecida: CATEGORIA_INVALIDA", exception.getMessage());
        verify(produtoRepository, never()).save(any(Produto.class)); // Verifica que o save nunca foi chamado
    }


    @Test
    @DisplayName("Deve atualizar um produto existente com sucesso")
    void deveAtualizarProdutoExistenteComSucesso() {
        // Arrange
        Long produtoId = 1L;
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoValido));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoValido); // Assume que o save retorna o produto atualizado

        ProdutoDTO dadosParaAtualizar = new ProdutoDTO();
        dadosParaAtualizar.setNome("Nome Atualizado");
        dadosParaAtualizar.setCategoria(Categoria.ACOMPANHAMENTO.name());
        dadosParaAtualizar.setPreco(BigDecimal.valueOf(15.00));
        dadosParaAtualizar.setDescricao("Descrição Atualizada");

        // Act
        Produto produtoAtualizado = produtoApplicationService.executar(produtoId, dadosParaAtualizar);

        // Assert
        assertNotNull(produtoAtualizado);
        assertEquals(dadosParaAtualizar.getNome(), produtoAtualizado.getNome());
        assertEquals(Categoria.ACOMPANHAMENTO, produtoAtualizado.getCategoria());
        assertEquals(dadosParaAtualizar.getPreco(), produtoAtualizado.getPreco());
        assertEquals(dadosParaAtualizar.getDescricao(), produtoAtualizado.getDescricao());
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar ProdutoNaoEncontradoException ao tentar atualizar produto inexistente")
    void deveLancarExcecaoAoAtualizarProdutoInexistente() {
        // Arrange
        Long produtoIdInexistente = 99L;
        when(produtoRepository.findById(produtoIdInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        ProdutoNaoEncontradoException exception = assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoApplicationService.executar(produtoIdInexistente, produtoDTOValido);
        });
        assertEquals("Produto com ID " + produtoIdInexistente + " não encontrado para atualização.", exception.getMessage());
        verify(produtoRepository, times(1)).findById(produtoIdInexistente);
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar ApplicationServiceException ao tentar atualizar produto com categoria inválida")
    void deveLancarExcecaoAoAtualizarProdutoComCategoriaInvalida() {
        // Arrange
        Long produtoId = 1L;
        ProdutoDTO produtoDTOCategoriaInvalida = new ProdutoDTO(produtoId, "Nome", "CATEGORIA_INVALIDA_ATUALIZACAO", BigDecimal.TEN, "Desc");
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoValido)); // Produto existe

        // Act & Assert
        ApplicationServiceException exception = assertThrows(ApplicationServiceException.class, () -> {
            produtoApplicationService.executar(produtoId, produtoDTOCategoriaInvalida);
        });
        assertEquals("Categoria inválida fornecida: CATEGORIA_INVALIDA_ATUALIZACAO", exception.getMessage());
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve remover um produto com sucesso")
    void deveRemoverProdutoComSucesso() {
        // Arrange
        Long produtoId = 1L;
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoValido));
        doNothing().when(produtoRepository).deleteById(produtoId); // doNothing para métodos void

        // Act
        produtoApplicationService.removerPorId(produtoId);

        // Assert
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, times(1)).deleteById(produtoId);
    }

    @Test
    @DisplayName("Deve lançar ProdutoNaoEncontradoException ao tentar remover produto inexistente")
    void deveLancarExcecaoAoRemoverProdutoInexistente() {
        // Arrange
        Long produtoIdInexistente = 99L;
        when(produtoRepository.findById(produtoIdInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        ProdutoNaoEncontradoException exception = assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoApplicationService.removerPorId(produtoIdInexistente);
        });
        assertEquals("Produto com ID " + produtoIdInexistente + " não encontrado para remoção.", exception.getMessage());
        verify(produtoRepository, times(1)).findById(produtoIdInexistente);
        verify(produtoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve buscar um produto por ID com sucesso")
    void deveBuscarProdutoPorIdComSucesso() {
        // Arrange
        Long produtoId = 1L;
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoValido));

        // Act
        Produto resultado = produtoApplicationService.buscarPorId(produtoId);

        // Assert
        assertNotNull(resultado);
        assertEquals(produtoValido.getId(), resultado.getId());
        assertEquals(produtoValido.getNome(), resultado.getNome());
        verify(produtoRepository, times(1)).findById(produtoId);
    }

    @Test
    @DisplayName("Deve lançar ProdutoNaoEncontradoException ao buscar produto por ID inexistente")
    void deveLancarExcecaoAoBuscarProdutoPorIdInexistente() {
        // Arrange
        Long produtoIdInexistente = 99L;
        when(produtoRepository.findById(produtoIdInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        ProdutoNaoEncontradoException exception = assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoApplicationService.buscarPorId(produtoIdInexistente);
        });
        assertEquals("Produto com ID " + produtoIdInexistente + " não encontrado.", exception.getMessage());
        verify(produtoRepository, times(1)).findById(produtoIdInexistente);
    }

    @Test
    @DisplayName("Deve listar produtos por categoria com sucesso")
    void deveListarProdutosPorCategoriaComSucesso() {
        // Arrange
        Categoria categoria = Categoria.LANCHE;
        List<Produto> produtosDaCategoria = Arrays.asList(produtoValido,
                new Produto(2L, "X-Salada", Categoria.LANCHE, BigDecimal.valueOf(15.00), "Desc Xis"));
        when(produtoRepository.findByCategoria(categoria)).thenReturn(produtosDaCategoria);

        // Act
        List<Produto> resultado = produtoApplicationService.executar(categoria);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(produtosDaCategoria, resultado);
        verify(produtoRepository, times(1)).findByCategoria(categoria);
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao listar produtos por categoria sem produtos")
    void deveRetornarListaVaziaAoListarProdutosPorCategoriaSemProdutos() {
        // Arrange
        Categoria categoria = Categoria.SOBREMESA;
        when(produtoRepository.findByCategoria(categoria)).thenReturn(Collections.emptyList());

        // Act
        List<Produto> resultado = produtoApplicationService.executar(categoria);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(produtoRepository, times(1)).findByCategoria(categoria);
    }

    @Test
    @DisplayName("Deve listar todos os produtos com sucesso")
    void deveListarTodosProdutosComSucesso() {
        // Arrange
        List<Produto> todosProdutos = Arrays.asList(produtoValido,
                new Produto(2L, "Batata Frita", Categoria.ACOMPANHAMENTO, BigDecimal.valueOf(8.00), "Desc Batata"));
        when(produtoRepository.findAll()).thenReturn(todosProdutos);

        // Act
        List<Produto> resultado = produtoApplicationService.executar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(todosProdutos, resultado);
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao listar todos os produtos quando não há produtos")
    void deveRetornarListaVaziaAoListarTodosProdutosQuandoNaoHaProdutos() {
        // Arrange
        when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Produto> resultado = produtoApplicationService.executar();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(produtoRepository, times(1)).findAll();
    }
}