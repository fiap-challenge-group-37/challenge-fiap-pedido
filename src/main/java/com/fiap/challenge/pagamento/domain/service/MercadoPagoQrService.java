package com.fiap.challenge.pagamento.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fiap.challenge.pagamento.adapters.in.http.dto.MercadoPagoPagamentoResponse;
import com.fiap.challenge.pagamento.adapters.in.http.dto.MercadoPagoQrCodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;


@Service
public class MercadoPagoQrService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoQrService.class);

    private static final String accessToken = System.getenv("MP_ACCESS_TOKEN");
    private static final String userId = System.getenv("MP_USER_ID");
    private static final String externalPosId = System.getenv("MP_EXTERNAL_POS_ID");

    public MercadoPagoQrCodeResponse gerarPedido(String externalReference, double totalAmount) {


        String url = String.format(
                "https://api.mercadopago.com/instore/orders/qr/seller/collectors/%s/pos/%s/qrs",
                userId, externalPosId
        );

        Map<String, Object> body = new HashMap<>();
        body.put("external_reference", externalReference);
        body.put("notification_url", "https://www.seusite.com/notificacao");
        body.put("title", "Compra na loja");
        body.put("description", "Pedido " + externalReference);
        body.put("total_amount", totalAmount);

        Map<String, Object> item = new HashMap<>();
        item.put("sku_number", "1234");
        item.put("category", "food");
        item.put("title", "Lanche");
        item.put("description", "Lanche saboroso");
        item.put("unit_price", totalAmount);
        item.put("quantity", 1);
        item.put("unit_measure", "unit");
        item.put("total_amount", totalAmount);

        body.put("items", List.of(item));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);


        try {
            ResponseEntity<MercadoPagoQrCodeResponse> response = restTemplate.postForEntity(url, entity, MercadoPagoQrCodeResponse.class);
            return response.getBody();
        } catch (Exception e) {
            logger.error("Erro ao processar dados", e);
            return null;
        }
    }

    public void getPaymentByExternalReference() {
        String externalReference = "51";

        String url = String.format(
                "https://api.mercadopago.com/v1/payments/search?external_reference=%s",
                externalReference
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);


        try {
            ResponseEntity<MercadoPagoPagamentoResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    MercadoPagoPagamentoResponse.class
            );

            MercadoPagoPagamentoResponse body = response.getBody();
        } catch (Exception e) {
            logger.error("Erro ao processar dados", e);
        }
    }

}
