package com.fiap.challenge.pedido.application.service;

import com.fiap.challenge.pedido.adapters.in.http.dto.ItemPedidoDTO;
import com.fiap.challenge.pedido.adapters.in.http.dto.PedidoDTO;
import com.fiap.challenge.pedido.application.exception.PedidoNaoEncontradoException;
import com.fiap.challenge.pedido.application.exception.ValidacaoPedidoException;
import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import com.fiap.challenge.pedido.domain.port.PedidoRepository;
import com.fiap.challenge.produto.application.port.in.BuscarProdutoPorIdUseCase;
import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;
import com.fiap.challenge.produto.application.exception.ProdutoNaoEncontradoException; // Para simular erro ao buscar produto

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoApplicationServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;

    @InjectMocks
    private PedidoApplicationService pedidoApplicationService;

    private PedidoDTO pedidoDTOValido;
    private ItemPedidoDTO itemPedidoDTOValido;
    private Produto produtoValido;
    private Pedido pedidoValido;
    private ItemPedido itemPedidoDominioValido; // Adicionado para reuso

    @BeforeEach
    void setUp() {
        produtoValido = new Produto(1L, "Hamburguer Clássico", Categoria.LANCHE, BigDecimal.valueOf(25.50), "Delicioso hamburguer");

        itemPedidoDTOValido = new ItemPedidoDTO();
        itemPedidoDTOValido.setProdutoId(produtoValido.getId());
        itemPedidoDTOValido.setQuantidade(2);

        pedidoDTOValido = new PedidoDTO();
        pedidoDTOValido.setClienteId(10L);
        pedidoDTOValido.setItens(Collections.singletonList(itemPedidoDTOValido));

        itemPedidoDominioValido = new ItemPedido(produtoValido.getId(), produtoValido.getNome(), itemPedidoDTOValido.getQuantidade(), produtoValido.getPreco());
        pedidoValido = new Pedido(1L, 10L, Collections.singletonList(itemPedidoDominioValido), BigDecimal.valueOf(51.00), StatusPedido.RECEBIDO, LocalDateTime.now(), LocalDateTime.now());
    }

    // --- Testes para criarPedido (executar(PedidoDTO)) ---

    @Test
    @DisplayName("Deve criar um pedido com sucesso")
    void deveCriarPedidoComSucesso() {
        // Arrange
        when(buscarProdutoPorIdUseCase.buscarPorId(produtoValido.getId())).thenReturn(produtoValido);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedidoParaSalvar = invocation.getArgument(0);
            // Simula a atribuição de um ID ao salvar e recalcula o valor total se necessário
            return new Pedido(1L, pedidoParaSalvar.getClienteId(), pedidoParaSalvar.getItens(),
                    pedidoParaSalvar.getValorTotal(), pedidoParaSalvar.getStatus(),
                    pedidoParaSalvar.getDataCriacao(), pedidoParaSalvar.getDataAtualizacao());
        });

        // Act
        Pedido resultado = pedidoApplicationService.executar(pedidoDTOValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedidoDTOValido.getClienteId(), resultado.getClienteId());
        assertEquals(1, resultado.getItens().size());
        assertEquals(produtoValido.getId(), resultado.getItens().get(0).getProdutoId());
        assertEquals(itemPedidoDTOValido.getQuantidade(), resultado.getItens().get(0).getQuantidade());
        assertEquals(produtoValido.getPreco().multiply(BigDecimal.valueOf(itemPedidoDTOValido.getQuantidade())), resultado.getValorTotal());
        assertEquals(StatusPedido.RECEBIDO, resultado.getStatus());
        verify(buscarProdutoPorIdUseCase, times(1)).buscarPorId(produtoValido.getId());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar ValidacaoPedidoException ao criar pedido sem itens")
    void deveLancarExcecaoAoCriarPedidoSemItens() {
        // Arrange
        PedidoDTO pedidoSemItensDTO = new PedidoDTO();
        pedidoSemItensDTO.setClienteId(1L);
        pedidoSemItensDTO.setItens(Collections.emptyList()); // Lista de itens vazia

        // Act & Assert
        ValidacaoPedidoException exception = assertThrows(ValidacaoPedidoException.class, () -> {
            pedidoApplicationService.executar(pedidoSemItensDTO);
        });
        assertEquals("O pedido deve conter pelo menos um item.", exception.getMessage());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar ValidacaoPedidoException ao criar pedido com lista de itens nula")
    void deveLancarExcecaoAoCriarPedidoComListaItensNula() {
        // Arrange
        PedidoDTO pedidoItensNulosDTO = new PedidoDTO();
        pedidoItensNulosDTO.setClienteId(1L);
        pedidoItensNulosDTO.setItens(null); // Lista de itens nula

        // Act & Assert
        ValidacaoPedidoException exception = assertThrows(ValidacaoPedidoException.class, () -> {
            pedidoApplicationService.executar(pedidoItensNulosDTO);
        });
        assertEquals("O pedido deve conter pelo menos um item.", exception.getMessage());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }


    @Test
    @DisplayName("Deve lançar ProdutoNaoEncontradoException ao criar pedido com produto inexistente")
    void deveLancarExcecaoAoCriarPedidoComProdutoInexistente() {
        // Arrange
        Long produtoInexistenteId = 999L;
        ItemPedidoDTO itemComProdutoInexistente = new ItemPedidoDTO();
        itemComProdutoInexistente.setProdutoId(produtoInexistenteId);
        itemComProdutoInexistente.setQuantidade(1);
        pedidoDTOValido.setItens(Collections.singletonList(itemComProdutoInexistente));

        when(buscarProdutoPorIdUseCase.buscarPorId(produtoInexistenteId))
                .thenThrow(new ProdutoNaoEncontradoException("Produto com ID " + produtoInexistenteId + " não encontrado."));

        // Act & Assert
        ProdutoNaoEncontradoException exception = assertThrows(ProdutoNaoEncontradoException.class, () -> {
            pedidoApplicationService.executar(pedidoDTOValido);
        });
        assertEquals("Produto com ID " + produtoInexistenteId + " não encontrado.", exception.getMessage());
        verify(buscarProdutoPorIdUseCase, times(1)).buscarPorId(produtoInexistenteId);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    // --- Testes para listarPedidos (executar() e executar(Optional<String>)) ---
    @Test
    @DisplayName("Deve listar todos os pedidos (não finalizados e ordenados) quando nenhum status é fornecido")
    void deveListarTodosPedidosQuandoNenhumStatusFornecido() {
        // Arrange
        // Cria um segundo item de pedido para o segundo pedido mockado
        Produto outroProduto = new Produto(2L, "Refrigerante", Categoria.BEBIDA, BigDecimal.valueOf(7.00), "Refrescante");
        ItemPedido outroItemPedido = new ItemPedido(outroProduto.getId(), outroProduto.getNome(), 1, outroProduto.getPreco());

        List<Pedido> listaPedidosMock = Arrays.asList(pedidoValido,
                new Pedido(2L, 11L, Collections.singletonList(outroItemPedido), BigDecimal.valueOf(7.00), StatusPedido.EM_PREPARACAO, LocalDateTime.now(), LocalDateTime.now()));

        when(pedidoRepository.findAll()).thenReturn(listaPedidosMock);

        // Act
        List<Pedido> resultado = pedidoApplicationService.executar(Optional.empty());

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(listaPedidosMock, resultado);
        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, never()).findByStatus(any(StatusPedido.class));
    }

    @Test
    @DisplayName("Deve listar pedidos por status específico com sucesso")
    void deveListarPedidosPorStatusComSucesso() {
        // Arrange
        StatusPedido statusFiltro = StatusPedido.RECEBIDO;
        List<Pedido> listaPedidosMock = Collections.singletonList(pedidoValido);
        when(pedidoRepository.findByStatus(statusFiltro)).thenReturn(listaPedidosMock);

        // Act
        List<Pedido> resultado = pedidoApplicationService.executar(Optional.of(statusFiltro.name()));

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(pedidoValido, resultado.get(0));
        verify(pedidoRepository, times(1)).findByStatus(statusFiltro);
        verify(pedidoRepository, never()).findAll();
    }

    @Test
    @DisplayName("Deve buscar pedido por ID com sucesso")
    void deveBuscarPedidoPorIdComSucesso() {
        // Arrange
        Long pedidoId = pedidoValido.getId();
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoValido));

        // Act
        Pedido resultado = pedidoApplicationService.buscarPorId(pedidoId);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedidoValido.getId(), resultado.getId());
        verify(pedidoRepository, times(1)).findById(pedidoId);
    }

    @Test
    @DisplayName("Deve lançar PedidoNaoEncontradoException ao buscar pedido por ID inexistente")
    void deveLancarExcecaoAoBuscarPedidoPorIdInexistente() {
        // Arrange
        Long idInexistente = 99L;
        when(pedidoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        PedidoNaoEncontradoException exception = assertThrows(PedidoNaoEncontradoException.class, () -> {
            pedidoApplicationService.buscarPorId(idInexistente);
        });
        assertEquals("Pedido com ID " + idInexistente + " não encontrado.", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deve atualizar status do pedido com sucesso")
    void deveAtualizarStatusPedidoComSucesso() {
        // Arrange
        Long pedidoId = pedidoValido.getId();
        String novoStatusStr = StatusPedido.EM_PREPARACAO.name();
        Pedido pedidoAtualizadoMock = new Pedido(pedidoId, pedidoValido.getClienteId(), pedidoValido.getItens(),
                pedidoValido.getValorTotal(), StatusPedido.EM_PREPARACAO,
                pedidoValido.getDataCriacao(), LocalDateTime.now());

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoValido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoAtualizadoMock);

        // Act
        Pedido resultado = pedidoApplicationService.executar(pedidoId, novoStatusStr);

        // Assert
        assertNotNull(resultado);
        assertEquals(StatusPedido.EM_PREPARACAO, resultado.getStatus());
        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar PedidoNaoEncontradoException ao tentar atualizar status de pedido inexistente")
    void deveLancarExcecaoAoAtualizarStatusDePedidoInexistente() {
        // Arrange
        Long idInexistente = 99L;
        String novoStatusStr = StatusPedido.PRONTO.name();
        when(pedidoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        PedidoNaoEncontradoException exception = assertThrows(PedidoNaoEncontradoException.class, () -> {
            pedidoApplicationService.executar(idInexistente, novoStatusStr);
        });
        assertEquals("Pedido com ID " + idInexistente + " não encontrado para atualização de status.", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(idInexistente);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar ValidacaoPedidoException ao tentar atualizar status com string inválida")
    void deveLancarExcecaoAoAtualizarStatusComStringInvalida() {
        // Arrange
        Long pedidoId = pedidoValido.getId();
        String statusInvalidoStr = "STATUS_MUITO_INVALIDO";
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoValido)); // Pedido existe

        // Act & Assert
        ValidacaoPedidoException exception = assertThrows(ValidacaoPedidoException.class, () -> {
            pedidoApplicationService.executar(pedidoId, statusInvalidoStr);
        });
        assertTrue(exception.getMessage().startsWith("Status '" + statusInvalidoStr + "' inválido."));
        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }
}
