package com.fiap.pedido.application.service;

import com.fiap. pedido.pedido.adapters.in.http.dto.ItemPedidoDTO;
import com.fiap.pedido. pedido.adapters.in. http.dto.PedidoDTO;
import com.fiap.pedido.pedido.application.exception.PedidoNaoEncontradoException;
import com.fiap.pedido.pedido. application.exception.ValidacaoPedidoException;
import com.fiap.pedido.pedido.application.service.PedidoApplicationService;
import com.fiap.pedido.pedido.domain.entities.ItemPedido;
import com. fiap.pedido.pedido.domain.entities.Pedido;
import com.fiap. pedido.pedido.domain. entities.StatusPedido;
import com.fiap.pedido. pedido.domain.port.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org. junit.jupiter.api.Test;
import org.junit.jupiter. api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org. mockito.Mock;
import org.mockito.junit.jupiter. MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java. util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit. jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers. any;
import static org.mockito. Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoApplicationServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoApplicationService service;

    private PedidoDTO pedidoDTO;
    private Pedido pedidoMock;
    private ItemPedidoDTO itemDTO;

    @BeforeEach
    void setUp() {
        // Setup ItemPedidoDTO
        itemDTO = new ItemPedidoDTO();
        itemDTO.setProdutoId(1L);
        itemDTO.setNomeProduto("Hamburguer");
        itemDTO.setQuantidade(2);
        itemDTO.setPreco(BigDecimal.valueOf(20.00));

        // Setup PedidoDTO
        pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(1L);
        pedidoDTO.setItens(Arrays.asList(itemDTO));

        // Setup Pedido Mock
        List<ItemPedido> itens = Arrays.asList(
                new ItemPedido(1L, "Hamburguer", 2, BigDecimal.valueOf(20.00))
        );
        pedidoMock = new Pedido(
                1L, 1L, itens, BigDecimal.valueOf(40.00),
                StatusPedido. RECEBIDO, LocalDateTime.now(), LocalDateTime.now(),
                "PED-001", null
        );
    }

    @Test
    void deveCriarPedidoComSucesso() {
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMock);

        Pedido resultado = service.executar(pedidoDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(StatusPedido. RECEBIDO, resultado.getStatus());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void deveLancarExcecaoQuandoPedidoSemItens() {
        pedidoDTO.setItens(new ArrayList<>());

        ValidacaoPedidoException exception = assertThrows(
                ValidacaoPedidoException. class,
                () -> service.executar(pedidoDTO)
        );

        assertEquals("O pedido deve conter pelo menos um item.", exception.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoPedidoComItensNull() {
        pedidoDTO.setItens(null);

        ValidacaoPedidoException exception = assertThrows(
                ValidacaoPedidoException.class,
                () -> service.executar(pedidoDTO)
        );

        assertEquals("O pedido deve conter pelo menos um item.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoIdNulo() {
        itemDTO.setProdutoId(null);

        ValidacaoPedidoException exception = assertThrows(
                ValidacaoPedidoException.class,
                () -> service.executar(pedidoDTO)
        );

        assertEquals("Produto ID não pode ser nulo", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeZero() {
        itemDTO.setQuantidade(0);

        ValidacaoPedidoException exception = assertThrows(
                ValidacaoPedidoException. class,
                () -> service.executar(pedidoDTO)
        );

        assertEquals("Quantidade deve ser maior que zero", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeNegativa() {
        itemDTO.setQuantidade(-1);

        ValidacaoPedidoException exception = assertThrows(
                ValidacaoPedidoException.class,
                () -> service.executar(pedidoDTO)
        );

        assertEquals("Quantidade deve ser maior que zero", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPrecoZero() {
        itemDTO.setPreco(BigDecimal.ZERO);

        ValidacaoPedidoException exception = assertThrows(
                ValidacaoPedidoException.class,
                () -> service.executar(pedidoDTO)
        );

        assertEquals("Preço deve ser maior que zero", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPrecoNegativo() {
        itemDTO.setPreco(BigDecimal. valueOf(-10.00));

        ValidacaoPedidoException exception = assertThrows(
                ValidacaoPedidoException.class,
                () -> service.executar(pedidoDTO)
        );

        assertEquals("Preço deve ser maior que zero", exception.getMessage());
    }

    @Test
    void deveListarTodosPedidos() {
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedidoMock));

        List<Pedido> resultado = service.executar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void deveListarPedidosPorStatus() {
        when(pedidoRepository.findByStatus(StatusPedido.RECEBIDO))
                .thenReturn(Arrays.asList(pedidoMock));

        List<Pedido> resultado = service.executar(Optional.of("RECEBIDO"));

        assertNotNull(resultado);
        assertEquals(1, resultado. size());
        verify(pedidoRepository, times(1)).findByStatus(StatusPedido. RECEBIDO);
    }

    @Test
    void deveListarTodosPedidosQuandoStatusVazio() {
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedidoMock));

        List<Pedido> resultado = service.executar(Optional.of(""));

        assertNotNull(resultado);
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void deveListarTodosPedidosQuandoStatusOptionalEmpty() {
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedidoMock));

        List<Pedido> resultado = service.executar(Optional.empty());

        assertNotNull(resultado);
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void deveLancarExcecaoQuandoStatusInvalido() {
        ValidacaoPedidoException exception = assertThrows(
                ValidacaoPedidoException. class,
                () -> service.executar(Optional.of("STATUS_INVALIDO"))
        );

        assertTrue(exception.getMessage().contains("Status inválido fornecido"));
    }

    @Test
    void deveBuscarPedidoPorExternalId() {
        when(pedidoRepository.findByExternalId("PED-001"))
                .thenReturn(Optional.of(pedidoMock));

        Pedido resultado = service.executar("PED-001");

        assertNotNull(resultado);
        assertEquals("PED-001", resultado.getExternalReference());
        verify(pedidoRepository, times(1)).findByExternalId("PED-001");
    }

    @Test
    void deveLancarExcecaoQuandoExternalIdNaoEncontrado() {
        when(pedidoRepository.findByExternalId("PED-999"))
                .thenReturn(Optional.empty());

        PedidoNaoEncontradoException exception = assertThrows(
                PedidoNaoEncontradoException.class,
                () -> service.executar("PED-999")
        );

        assertTrue(exception.getMessage().contains("PED-999"));
    }

    @Test
    void deveBuscarPedidoPorId() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));

        Pedido resultado = service. buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoEncontradoPorId() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional. empty());

        PedidoNaoEncontradoException exception = assertThrows(
                PedidoNaoEncontradoException.class,
                () -> service.buscarPorId(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
    }

    @Test
    void deveAtualizarStatusDoPedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMock);

        Pedido resultado = service.executar(1L, "EM_PREPARACAO");

        assertNotNull(resultado);
        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoEncontradoParaAtualizarStatus() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional. empty());

        PedidoNaoEncontradoException exception = assertThrows(
                PedidoNaoEncontradoException.class,
                () -> service.executar(999L, "EM_PREPARACAO")
        );

        assertTrue(exception.getMessage().contains("999"));
        assertTrue(exception.getMessage().contains("atualização de status"));
    }

    @Test
    void deveLancarExcecaoQuandoStatusInvalidoParaAtualizar() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));

        ValidacaoPedidoException exception = assertThrows(
                ValidacaoPedidoException.class,
                () -> service.executar(1L, "STATUS_INVALIDO")
        );

        assertTrue(exception. getMessage().contains("STATUS_INVALIDO"));
        assertTrue(exception.getMessage().contains("inválido"));
    }
}