package com.fiap.challenge.pagamento.application.ports.out;

import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.MercadoPagoPagamentoResponse;
import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.MercadoPagoQrCodeResponse;
import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.PagamentoDTO;
import com.fiap.challenge.pedido.domain.entities.Pedido;

public interface MercadoPagoGateway {

    MercadoPagoQrCodeResponse criarPagamento(Pedido pedido);

    MercadoPagoPagamentoResponse getPaymentByExternalReference(String externalReference);

    PagamentoDTO getPaymentById(String externalReference);
}
