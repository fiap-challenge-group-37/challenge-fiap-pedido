package com.fiap.challenge.produto.adapters.in.http;

import com.fiap.challenge.config.exception.dto.ErrorResponseDTO; // New import for Swagger
import com.fiap.challenge.produto.adapters.in.http.dto.ProdutoDTO;
// ApplicationServiceException import can be removed if not thrown directly from controller methods
import com.fiap.challenge.produto.application.port.in.*;
import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content; // New import for Swagger
import io.swagger.v3.oas.annotations.media.Schema; // New import for Swagger
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// FieldError, MethodArgumentNotValidException, ResponseStatus, ExceptionHandler imports can be removed
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
// HashMap and Map imports can be removed
import java.util.List;
// Optional import can be removed
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produto Controller", description = "Operações para visualização e gerenciamento de produtos")
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);
    // ERRO_INESPERADO_MSG and ERRO_KEY can be removed if local handlers are removed

    private final CriarProdutoUseCase criarProdutoUseCase;
    private final AtualizarProdutoUseCase atualizarProdutoUseCase;
    private final RemoverProdutoUseCase removerProdutoUseCase;
    private final BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;
    private final BuscarProdutoPorCategoriaUseCase buscarProdutoPorCategoriaUseCase;
    private final ListarTodosProdutosUseCase listarTodosProdutosUseCase;

    public ProdutoController(CriarProdutoUseCase criarProdutoUseCase,
                             AtualizarProdutoUseCase atualizarProdutoUseCase,
                             RemoverProdutoUseCase removerProdutoUseCase,
                             BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase,
                             BuscarProdutoPorCategoriaUseCase buscarProdutoPorCategoriaUseCase,
                             ListarTodosProdutosUseCase listarTodosProdutosUseCase) {
        this.criarProdutoUseCase = criarProdutoUseCase;
        this.atualizarProdutoUseCase = atualizarProdutoUseCase;
        this.removerProdutoUseCase = removerProdutoUseCase;
        this.buscarProdutoPorIdUseCase = buscarProdutoPorIdUseCase;
        this.buscarProdutoPorCategoriaUseCase = buscarProdutoPorCategoriaUseCase;
        this.listarTodosProdutosUseCase = listarTodosProdutosUseCase;
    }

    @Operation(summary = "Cadastrar novo produto (Administrativo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para o produto (ex: categoria inválida)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Requer API Key"),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados enviados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @SecurityRequirement(name = "ApiKeyAuth")
    @PostMapping
    public ResponseEntity<ProdutoDTO> criarProduto(@Valid @RequestBody ProdutoDTO dto) {
        Produto novoProduto = criarProdutoUseCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoDTO.fromDomain(novoProduto));
    }

    @Operation(summary = "Editar produto existente por ID (Administrativo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização ou categoria inválida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Requer API Key"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados enviados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @SecurityRequirement(name = "ApiKeyAuth")
    @PutMapping("/{produto_id}")
    public ResponseEntity<ProdutoDTO> editarProduto(@PathVariable("produto_id") Long produtoId, @Valid @RequestBody ProdutoDTO dto) {
        // Now returns Produto directly or throws ProdutoNaoEncontradoException
        Produto produtoAtualizado = atualizarProdutoUseCase.executar(produtoId, dto);
        return ResponseEntity.ok(ProdutoDTO.fromDomain(produtoAtualizado));
    }

    @Operation(summary = "Remover produto por ID (Administrativo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Requer API Key"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @SecurityRequirement(name = "ApiKeyAuth")
    @DeleteMapping("/{produto_id}")
    public ResponseEntity<Void> removerProduto(@PathVariable("produto_id") Long produtoId) {
        // Now returns void or throws ProdutoNaoEncontradoException
        removerProdutoUseCase.removerPorId(produtoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos os produtos ou filtrar por categoria (Público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoDTO.class))), // Should be array
            @ApiResponse(responseCode = "400", description = "Categoria inválida fornecida no filtro",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarOuBuscarPorCategoria(
            @Parameter(name = "categoria", description = "Nome da categoria para filtrar os produtos (opcional). Valores: LANCHE, ACOMPANHAMENTO, BEBIDA, SOBREMESA",
                    in = ParameterIn.QUERY, schema = @Schema(type = "string", enumAsRef = true, allowableValues = {"LANCHE", "ACOMPANHAMENTO", "BEBIDA", "SOBREMESA"}))
            @RequestParam(required = false) String categoriaNome) {

        List<Produto> produtos;
        if (categoriaNome != null && !categoriaNome.trim().isEmpty()) {
            // IllegalArgumentException will be caught by GlobalRestExceptionHandler
            Categoria categoriaEnum = Categoria.fromString(categoriaNome.toUpperCase());
            produtos = buscarProdutoPorCategoriaUseCase.executar(categoriaEnum);
        } else {
            produtos = listarTodosProdutosUseCase.executar();
        }

        if (produtos == null || produtos.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<ProdutoDTO> dtos = produtos.stream()
                .map(ProdutoDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar produto por ID (Público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{produto_id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable("produto_id") Long produtoId) {
        // Now returns Produto directly or throws ProdutoNaoEncontradoException
        Produto produto = buscarProdutoPorIdUseCase.buscarPorId(produtoId);
        return ResponseEntity.ok(ProdutoDTO.fromDomain(produto));
    }

    // Local Exception Handlers are REMOVED as they are handled by GlobalRestExceptionHandler
    // @ExceptionHandler(MethodArgumentNotValidException.class) ...
    // @ExceptionHandler(IllegalArgumentException.class) ...
    // @ExceptionHandler(ApplicationServiceException.class) ...
    // @ExceptionHandler(Exception.class) ...
}