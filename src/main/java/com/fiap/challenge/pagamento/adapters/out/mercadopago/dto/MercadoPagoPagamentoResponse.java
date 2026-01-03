package com.fiap.challenge.pagamento.adapters.out.mercadopago.dto;

import lombok.Data;

import java.util.List;

@Data
public class MercadoPagoPagamentoResponse {

    private List<PagamentoDTO> results;

}
