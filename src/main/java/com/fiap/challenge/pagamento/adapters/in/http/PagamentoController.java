package com.fiap.challenge.pagamento.adapters.in.http;

import com.fiap.challenge.pagamento.adapters.in.http.dto.MercadoPagoQrCodeResponse;
import com.fiap.challenge.pagamento.domain.service.MercadoPagoQrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamento")
@Tag(name = "Pagamento Controller", description = "Operações relacionadas ao pagamento")
public class PagamentoController {

    private final MercadoPagoQrService mercadoPagoQrService;

    public PagamentoController(MercadoPagoQrService mercadoPagoQrService) {
        this.mercadoPagoQrService = mercadoPagoQrService;
    }

    @Operation(summary = "Gerar pagamento")
    @PostMapping
    public ResponseEntity<MercadoPagoQrCodeResponse> cadastrar() {
        return ResponseEntity.status(HttpStatus.CREATED).body(mercadoPagoQrService.gerarPedido("PEDIDO001", 100));
    }

}
