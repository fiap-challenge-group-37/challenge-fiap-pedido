package com.fiap.challenge.pagamento.adapters.in.http;

import com.fiap.challenge.pagamento.adapters.in.http.dto.WebhookPaymentEventDTO;
import com.fiap.challenge.pagamento.application.ports.in.AtualizarPagamentoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook")
public class PagamentoController {

    private final AtualizarPagamentoUseCase atualizarPagamentoUseCase;

    @PostMapping("/payment")
    public ResponseEntity<Void> handlePaymentWebhook(@RequestBody WebhookPaymentEventDTO eventDTO) {
        atualizarPagamentoUseCase.executar(eventDTO);
        return ResponseEntity.ok().build();
    }
}
