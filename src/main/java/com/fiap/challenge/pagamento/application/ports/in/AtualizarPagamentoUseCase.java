package com.fiap.challenge.pagamento.application.ports.in;

import com.fiap.challenge.pagamento.adapters.in.http.dto.WebhookPaymentEventDTO;

public interface AtualizarPagamentoUseCase {
    void executar(WebhookPaymentEventDTO  webhookPaymentEventDTO);
}
