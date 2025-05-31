package com.fiap.challenge.pagamento.domain.service;

import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.MercadoPagoPagamentoResponse;
import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.PagamentoDTO;
import com.fiap.challenge.pagamento.application.ports.in.SincronizarPagamentoUseCase;
import com.fiap.challenge.pagamento.application.ports.out.MercadoPagoGateway;
import com.fiap.challenge.pedido.application.port.in.AtualizarStatusPedidoUseCase;
import com.fiap.challenge.pedido.application.port.in.ListarPedidosUseCase;
import com.fiap.challenge.pedido.application.service.PedidoApplicationService;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SincronizarPagamentoService implements SincronizarPagamentoUseCase {

    private final MercadoPagoGateway mercadoPagoGateway;
    private final ListarPedidosUseCase listarPedidosUseCase;
    private final AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;

    @Override
    public void executar() {
        List<Pedido> aguardandoPagamento = listarPedidosUseCase.executar(Optional.ofNullable(StatusPedido.AGUARDANDO_PAGAMENTO.toString()));

        aguardandoPagamento.forEach(pedido -> {
            MercadoPagoPagamentoResponse pagamentoResponse = mercadoPagoGateway.getPaymentByExternalReference(pedido.getId().toString());
            if (!pagamentoResponse.getResults().isEmpty()) {
                PagamentoDTO pagamentoDTO = pagamentoResponse.getResults().getFirst();
                if (Objects.equals(pagamentoDTO.getStatus(), "approved")) {
                    atualizarStatusPedidoUseCase.executar(pedido.getId(), StatusPedido.RECEBIDO.toString());
                }
            }
        });

    }
}