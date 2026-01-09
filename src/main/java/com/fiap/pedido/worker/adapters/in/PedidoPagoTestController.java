package com.fiap.pedido.worker.adapters.in;

import com.fiap.pedido.domain.dto.PedidoPagoEvento;
import com.fiap.pedido.worker.publisher.PedidoPagoPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teste-pedido-pago")
@Tag(name = "Pedido Pago Publisher Controller", description = "Operações para envio de eventos de pedido pago para a fila SQS")
public class PedidoPagoTestController {

    private final PedidoPagoPublisher publisher;

    public PedidoPagoTestController(PedidoPagoPublisher publisher) {
        this.publisher = publisher;
    }

    @Operation(summary = "Envia evento de PedidoPago para fila SQS (apenas para teste, dados mockados)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento mock enviado com sucesso para a fila"),
            @ApiResponse(responseCode = "500", description = "Erro ao publicar evento na fila",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/mock")
    public ResponseEntity<String> publicarEventoPedidoPagoMock() {
        // Cria lista de itens mockados
        List<PedidoPagoEvento.ItemPedido> itens = List.of(
                new PedidoPagoEvento.ItemPedido("Lanche", 2),
                new PedidoPagoEvento.ItemPedido("Bebida", 1)
        );

        // Cria evento mockado
        PedidoPagoEvento eventoMock = new PedidoPagoEvento(
                123L, // idPedido
                itens
        );

        publisher.publicarPedidoPago(eventoMock);
        return ResponseEntity.ok("Evento MOCK enviado para a fila!");
    }

    @Operation(summary = "Envia evento de PedidoPago para fila SQS (evento informado no body)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento enviado com sucesso para a fila"),
            @ApiResponse(responseCode = "400", description = "Evento mal formatado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro ao publicar evento na fila",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<String> publicarEventoPedidoPago(
            @RequestBody(
                    description = "Dados do evento PedidoPago",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PedidoPagoEvento.class)))
            @org.springframework.web.bind.annotation.RequestBody PedidoPagoEvento evento) {
        publisher.publicarPedidoPago(evento);
        return ResponseEntity.ok("Evento enviado para a fila!");
    }
}