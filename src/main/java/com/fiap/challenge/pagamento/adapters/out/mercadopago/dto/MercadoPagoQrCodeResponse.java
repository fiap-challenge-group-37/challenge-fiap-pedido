package com.fiap.challenge.pagamento.adapters.out.mercadopago.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MercadoPagoQrCodeResponse {

    @JsonProperty("in_store_order_id")
    private String inStoreOrderId;

    @JsonProperty("qr_data")
    private String qrData;

}
