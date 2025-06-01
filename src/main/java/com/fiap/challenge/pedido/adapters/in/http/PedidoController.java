package com.fiap.challenge.pedido.adapters.in.http;

import com.fiap.challenge.pedido.adapters.in.http.dto.PedidoDTO;
import com.fiap.challenge.pedido.adapters.in.http.dto.PedidoResponseDTO;
import com.fiap.challenge.pedido.adapters.in.http.dto.StatusUpdateRequestDTO;
import com.fiap.challenge.pedido.application.port.in.AtualizarStatusPedidoUseCase;
import com.fiap.challenge.pedido.application.port.in.BuscarPedidoPorIdUseCase;
import com.fiap.challenge.pedido.application.port.in.CriarPedidoUseCase;
import com.fiap.challenge.pedido.application.port.in.ListarPedidosUseCase;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedido Controller", description = "Operações para criação e acompanhamento de pedidos")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    private final CriarPedidoUseCase criarPedidoUseCase;
    private final ListarPedidosUseCase listarPedidosUseCase;
    private final BuscarPedidoPorIdUseCase buscarPedidoPorIdUseCase;
    private final AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;

    public PedidoController(CriarPedidoUseCase criarPedidoUseCase,
                            ListarPedidosUseCase listarPedidosUseCase,
                            BuscarPedidoPorIdUseCase buscarPedidoPorIdUseCase,
                            AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase) {
        this.criarPedidoUseCase = criarPedidoUseCase;
        this.listarPedidosUseCase = listarPedidosUseCase;
        this.buscarPedidoPorIdUseCase = buscarPedidoPorIdUseCase;
        this.atualizarStatusPedidoUseCase = atualizarStatusPedidoUseCase;
    }

    @Operation(summary = "Criar um novo pedido (Fake Checkout)(Público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para o pedido"),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/checkout")
    public ResponseEntity<PedidoResponseDTO> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        logger.info("Recebida requisição para criar pedido.");
        Pedido novoPedido = criarPedidoUseCase.executar(pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoResponseDTO.fromDomain(novoPedido));
    }

    @Operation(summary = "Listar todos os pedidos ou filtrar por status (Público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PedidoResponseDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Status inválido para filtro"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos(
            @Parameter(description = "Status do pedido para filtro (RECEBIDO, EM_PREPARACAO, PRONTO, FINALIZADO). Se não informado, lista os pedidos na fila de acompanhamento.")
            @RequestParam(required = false) String status) {
        List<Pedido> pedidos = listarPedidosUseCase.executar(Optional.ofNullable(status));
        List<PedidoResponseDTO> responseDTOs = pedidos.stream()
                .map(PedidoResponseDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    @Operation(summary = "Buscar pedido por ID (Público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{pedido_id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedidoPorId(@PathVariable("pedido_id") Long pedidoId) {
        Pedido pedido = buscarPedidoPorIdUseCase.buscarPorId(pedidoId);
        return ResponseEntity.ok(PedidoResponseDTO.fromDomain(pedido));
    }

    @Operation(summary = "Atualizar status de um pedido (Uso interno/administrativo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Status inválido ou transição não permitida"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "422", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{pedido_id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatusPedido(
            @PathVariable("pedido_id") Long pedidoId,
            @Valid @RequestBody StatusUpdateRequestDTO statusUpdateRequestDTO) {
        logger.info("Requisição para atualizar status do pedido {}: {}", pedidoId, statusUpdateRequestDTO.getNovoStatus());
        Pedido pedidoAtualizado = atualizarStatusPedidoUseCase.executar(pedidoId, statusUpdateRequestDTO.getNovoStatus());
        return ResponseEntity.ok(PedidoResponseDTO.fromDomain(pedidoAtualizado));
    }
}
