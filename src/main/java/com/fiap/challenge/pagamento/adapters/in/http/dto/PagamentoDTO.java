package com.fiap.challenge.pagamento.adapters.in.http.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagamentoDTO {

    @JsonProperty("external_reference")
    private String externalReference;

    @JsonProperty("date_approved")
    private String dateApproved;

    @JsonProperty("status_detail")
    private String statusDetail;

    @JsonProperty("status")
    private String status;
}