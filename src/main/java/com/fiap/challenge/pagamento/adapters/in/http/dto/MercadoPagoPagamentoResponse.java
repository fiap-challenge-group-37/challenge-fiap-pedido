package com.fiap.challenge.pagamento.adapters.in.http.dto;

import lombok.Data;

import java.util.List;

@Data
public class MercadoPagoPagamentoResponse {

    private List<PagamentoDTO> results;

}
