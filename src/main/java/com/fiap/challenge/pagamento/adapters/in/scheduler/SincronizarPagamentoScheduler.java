package com.fiap.challenge.pagamento.adapters.in.scheduler;

import com.fiap.challenge.pagamento.application.ports.in.SincronizarPagamentoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SincronizarPagamentoScheduler {

    private final SincronizarPagamentoUseCase sincronizarPagamentoUseCase;

    @Scheduled(fixedDelay = 15000)
    public void sincronizarPagamentos() {
        log.info("Iniciando sincronização de pagamentos...");
        sincronizarPagamentoUseCase.executar();
        log.info("Sincronização finalizada.");
    }
}
