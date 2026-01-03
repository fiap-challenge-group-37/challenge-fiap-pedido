package com.fiap.challenge.pagamento.application.ports.service;

import com.fiap.challenge.pagamento.adapters.in.http.dto.WebhookPaymentEventDTO;
import com.fiap.challenge.pagamento.adapters.out.MercadoPagoClient;
import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.PagamentoDTO;
import com.fiap.challenge.pagamento.application.ports.in.AtualizarPagamentoUseCase;
import com.fiap.challenge.pedido.application.port.in.AtualizarStatusPedidoUseCase;
import com.fiap.challenge.pedido.application.port.in.ListarPedidosUseCase;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AtualizarPagamentoService implements AtualizarPagamentoUseCase {

    private final MercadoPagoClient mercadoPagoClient;
    private final ListarPedidosUseCase listarPedidosUseCase;
    private final AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;

    @Override
    public void executar(WebhookPaymentEventDTO webhookEvent) {
        if (webhookEvent == null || !"payment".equalsIgnoreCase(webhookEvent.getType())) {
            log.warn("Evento ignorado: tipo inválido ou nulo - {}", webhookEvent != null ? webhookEvent.getType() : "null");
            return;
        }

        String paymentId = webhookEvent.getData() != null ? webhookEvent.getData().getId() : null;
        if (paymentId == null) {
            log.warn("Evento de pagamento recebido sem ID: {}", webhookEvent);
            return;
        }

        log.info("Consultando pagamento no MercadoPago com ID: {}", paymentId);
        PagamentoDTO pagamento = mercadoPagoClient.getPaymentById(paymentId);

        if (pagamento == null || pagamento.getExternalReference() == null) {
            log.warn("Pagamento não encontrado ou sem external_reference. PaymentId: {}", paymentId);
            return;
        }

        String externalReference = pagamento.getExternalReference();
        log.info("Buscando pedido com external_reference: {}", externalReference);
        Pedido pedido = listarPedidosUseCase.executar(externalReference);

        if (pedido == null) {
            log.error("Pedido não encontrado para external_reference: {}", externalReference);
            return;
        }

        if (pedido.getStatus().equals(StatusPedido.AGUARDANDO_PAGAMENTO)) {
            log.info("Atualizando status do pedido (ID: {}) para RECEBIDO", pedido.getId());
            atualizarStatusPedidoUseCase.executar(pedido.getId(), StatusPedido.RECEBIDO.toString());
        }
    }
}
