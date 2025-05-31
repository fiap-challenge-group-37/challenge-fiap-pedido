package com.fiap.challenge.pagamento.adapters.out;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MercadoPagoProperties {

    @Value("${MP_ACCESS_TOKEN}")
    private String accessToken;

    @Value("${MP_USER_ID}")
    private String userId;

    @Value("${MP_EXTERNAL_POS_ID}")
    private String externalPosId;

    @Value("${MP_URL}")
    private String url;
}