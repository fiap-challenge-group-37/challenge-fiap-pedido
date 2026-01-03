package com.fiap.challenge.pagamento.adapters.out;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.MercadoPagoPagamentoResponse;
import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.MercadoPagoQrCodeResponse;
import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.PagamentoDTO;
import com.fiap.challenge.pagamento.application.ports.out.MercadoPagoGateway;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class MercadoPagoClient implements MercadoPagoGateway {

    private final RestTemplate restTemplate;
    private final MercadoPagoProperties properties;

    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoClient.class);

    public MercadoPagoClient(RestTemplate restTemplate, MercadoPagoProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public MercadoPagoQrCodeResponse criarPagamento(Pedido pedido) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(properties.getUrl());
        UriBuilder builder = factory.builder()
                .pathSegment("instore", "orders", "qr", "seller", "collectors", properties.getUserId(), "pos", properties.getExternalPosId(), "qrs");

        String url = builder.build().toString();

        Map<String, Object> body = new HashMap<>();
        body.put("external_reference", pedido.getExternalID());
        body.put("notification_url", properties.getWebhookPagamento());
        body.put("title", "Compra na loja");
        body.put("description", "Pedido " + pedido.getId());
        body.put("total_amount", pedido.getValorTotal());

        List<Map<String, Object>> itens = pedido.getItens().stream().map(itemPedido -> {
            Map<String, Object> item = new HashMap<>();
            item.put("category", "food");
            item.put("title", itemPedido.getNomeProduto());
            item.put("description", itemPedido.getNomeProduto());
            item.put("unit_price", itemPedido.getPrecoUnitario());
            item.put("quantity", itemPedido.getQuantidade());
            item.put("unit_measure", "unit");
            item.put("total_amount", itemPedido.getPrecoTotal());
            return item;
        }).toList();

        body.put("items", itens);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getAccessToken());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<MercadoPagoQrCodeResponse> response = restTemplate.postForEntity(url, entity, MercadoPagoQrCodeResponse.class);
            return response.getBody();
        } catch (Exception e) {
            logger.error("Erro ao criar pagamento no MercadoPago", e);
            throw new RuntimeException("Falha ao criar pagamento no MercadoPago");
        }
    }

    @Override
    public MercadoPagoPagamentoResponse getPaymentByExternalReference(String externalReference) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(properties.getUrl());
        UriBuilder builder = factory.builder()
                .path("v1/payments/search")
                .queryParam("external_reference", externalReference);

        String url = builder.build().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<MercadoPagoPagamentoResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    MercadoPagoPagamentoResponse.class
            );

            return response.getBody();
        } catch (Exception e) {
            logger.error("Erro ao buscar pagamento no MercadoPago", e);
            throw new RuntimeException("Falha ao buscar pagamento no MercadoPago");
        }
    }

    @Override
    public PagamentoDTO getPaymentById(String paymentId) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(properties.getUrl());
        UriBuilder builder = factory.builder()
                .path("v1/payments/")
                .path(paymentId);

        String url = builder.build().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getAccessToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<PagamentoDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    PagamentoDTO.class
            );
            return response.getBody();
        } catch (Exception e) {
            logger.error("Erro ao buscar pagamento no MercadoPago", e);
            throw new RuntimeException("Falha ao buscar pagamento no MercadoPago");
        }
    }
}
