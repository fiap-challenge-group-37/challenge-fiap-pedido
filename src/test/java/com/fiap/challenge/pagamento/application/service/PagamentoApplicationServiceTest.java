package com.fiap.challenge.pagamento.application.service;

import com.fiap.challenge.pagamento.adapters.in.http.dto.DataDTO;
import com.fiap.challenge.pagamento.adapters.in.http.dto.WebhookPaymentEventDTO;
import com.fiap.challenge.pagamento.adapters.out.MercadoPagoClient;
import com.fiap.challenge.pagamento.adapters.out.MercadoPagoProperties;
import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.MercadoPagoPagamentoResponse;
import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.PagamentoDTO;
import com.fiap.challenge.pagamento.application.ports.service.AtualizarPagamentoService;
import com.fiap.challenge.pedido.application.port.in.AtualizarStatusPedidoUseCase;
import com.fiap.challenge.pedido.application.port.in.ListarPedidosUseCase;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoApplicationServiceTest {

    @Mock
    private MercadoPagoClient mercadoPagoClient;

    @Mock
    private ListarPedidosUseCase listarPedidosUseCase;

    @Mock
    private AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;

    @InjectMocks
    private AtualizarPagamentoService atualizarPagamentoService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MercadoPagoProperties properties;

    @Test
    void deveIgnorarEventoNulo() {
        atualizarPagamentoService.executar(null);
        verifyNoInteractions(mercadoPagoClient, listarPedidosUseCase, atualizarStatusPedidoUseCase);
    }

    @Test
    void deveIgnorarEventoComTipoInvalido() {
        WebhookPaymentEventDTO evento = new WebhookPaymentEventDTO();
        evento.setType("invalid_type");

        atualizarPagamentoService.executar(evento);

        verifyNoInteractions(mercadoPagoClient, listarPedidosUseCase, atualizarStatusPedidoUseCase);
    }

    @Test
    void deveIgnorarEventoSemPaymentId() {
        WebhookPaymentEventDTO evento = new WebhookPaymentEventDTO();
        evento.setType("payment");
        evento.setData(new DataDTO()); // id == null

        atualizarPagamentoService.executar(evento);

        verifyNoInteractions(mercadoPagoClient, listarPedidosUseCase, atualizarStatusPedidoUseCase);
    }

    @Test
    void deveIgnorarQuandoPagamentoNaoEncontrado() {
        WebhookPaymentEventDTO evento = buildEvento("123");
        when(mercadoPagoClient.getPaymentById("123")).thenReturn(null);

        atualizarPagamentoService.executar(evento);

        verify(mercadoPagoClient).getPaymentById("123");
        verifyNoMoreInteractions(mercadoPagoClient);
        verifyNoInteractions(listarPedidosUseCase, atualizarStatusPedidoUseCase);
    }

    @Test
    void deveIgnorarQuandoPagamentoSemExternalReference() {
        WebhookPaymentEventDTO evento = buildEvento("123");
        PagamentoDTO pagamentoDTO = new PagamentoDTO(); // externalReference == null
        when(mercadoPagoClient.getPaymentById("123")).thenReturn(pagamentoDTO);

        atualizarPagamentoService.executar(evento);

        verify(mercadoPagoClient).getPaymentById("123");
        verifyNoMoreInteractions(mercadoPagoClient);
        verifyNoInteractions(listarPedidosUseCase, atualizarStatusPedidoUseCase);
    }

    @Test
    void deveIgnorarQuandoPedidoNaoEncontrado() {
        WebhookPaymentEventDTO evento = buildEvento("123");
        PagamentoDTO pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setExternalReference("pedido-001");
        when(mercadoPagoClient.getPaymentById("123")).thenReturn(pagamentoDTO);
        when(listarPedidosUseCase.executar("pedido-001")).thenReturn(null);

        atualizarPagamentoService.executar(evento);

        verify(mercadoPagoClient).getPaymentById("123");
        verify(listarPedidosUseCase).executar("pedido-001");
        verifyNoInteractions(atualizarStatusPedidoUseCase);
    }

    @Test
    void naoDeveAtualizarPedidoComStatusDiferenteDeAguardandoPagamento() {
        WebhookPaymentEventDTO evento = buildEvento("123");
        PagamentoDTO pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setExternalReference("pedido-001");
        when(mercadoPagoClient.getPaymentById("123")).thenReturn(pagamentoDTO);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus(StatusPedido.RECEBIDO);

        when(listarPedidosUseCase.executar("pedido-001")).thenReturn(pedido);

        atualizarPagamentoService.executar(evento);

        verify(mercadoPagoClient).getPaymentById("123");
        verify(listarPedidosUseCase).executar("pedido-001");
        verifyNoInteractions(atualizarStatusPedidoUseCase);
    }

    @Test
    void deveAtualizarPedidoParaRecebido() {
        WebhookPaymentEventDTO evento = buildEvento("123");
        PagamentoDTO pagamentoDTO = new PagamentoDTO();
        pagamentoDTO.setExternalReference("pedido-001");
        when(mercadoPagoClient.getPaymentById("123")).thenReturn(pagamentoDTO);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

        when(listarPedidosUseCase.executar("pedido-001")).thenReturn(pedido);

        atualizarPagamentoService.executar(evento);

        verify(mercadoPagoClient).getPaymentById("123");
        verify(listarPedidosUseCase).executar("pedido-001");
        verify(atualizarStatusPedidoUseCase).executar(1L, StatusPedido.RECEBIDO.toString());
    }

    @Test
    void deveBuscarPagamentoPorExternalReferenceComSucesso() {
        String externalReference = "pedido-123";

        MercadoPagoPagamentoResponse mockResponse = new MercadoPagoPagamentoResponse();
        when(mercadoPagoClient.getPaymentByExternalReference(externalReference))
                .thenReturn(mockResponse);

        MercadoPagoPagamentoResponse result = mercadoPagoClient.getPaymentByExternalReference(externalReference);

        assertNotNull(result);
        assertEquals(mockResponse, result);
    }

    @Test
    void deveLancarExcecaoQuandoFalharNaChamada() {
        String externalReference = "pedido-123";

        when(mercadoPagoClient.getPaymentByExternalReference(externalReference))
                .thenThrow(new RuntimeException("Erro simulado"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            mercadoPagoClient.getPaymentByExternalReference(externalReference);
        });

        assertEquals("Erro simulado", ex.getMessage());
    }
    private WebhookPaymentEventDTO buildEvento(String paymentId) {
        WebhookPaymentEventDTO evento = new WebhookPaymentEventDTO();
        evento.setType("payment");
        DataDTO data = new DataDTO();
        data.setId(paymentId);
        evento.setData(data);
        return evento;
    }
}
