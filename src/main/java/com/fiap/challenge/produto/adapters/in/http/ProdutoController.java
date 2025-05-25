package com.fiap.challenge.produto.adapters.in.http;

import com.fiap.challenge.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.challenge.produto.application.exception.ApplicationServiceException;
import com.fiap.challenge.produto.application.port.in.*;
import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produto Controller", description = "Operações para visualização e gerenciamento de produtos")
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);
    private static final String ERRO_INESPERADO_MSG = "Ocorreu um erro interno inesperado. Tente novamente mais tarde.";
    private static final String ERRO_KEY = "erro";

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
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para o produto"),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Requer API Key"),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados enviados")
    })
    @SecurityRequirement(name = "ApiKeyAuth") // Protege este endpoint
    @PostMapping
    public ResponseEntity<ProdutoDTO> criarProduto(@Valid @RequestBody ProdutoDTO dto) {
        Produto novoProduto = criarProdutoUseCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoDTO.fromDomain(novoProduto));
    }

    @Operation(summary = "Editar produto existente por ID (Administrativo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização ou categoria inválida"),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Requer API Key"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados enviados")
    })
    @SecurityRequirement(name = "ApiKeyAuth") // Protege este endpoint
    @PutMapping("/{produto_id}")
    public ResponseEntity<ProdutoDTO> editarProduto(@PathVariable("produto_id") Long produtoId, @Valid @RequestBody ProdutoDTO dto) {
        Optional<Produto> produtoAtualizadoOptional = atualizarProdutoUseCase.executar(produtoId, dto);
        return produtoAtualizadoOptional
                .map(produto -> ResponseEntity.ok(ProdutoDTO.fromDomain(produto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Remover produto por ID (Administrativo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Requer API Key"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @SecurityRequirement(name = "ApiKeyAuth") // Protege este endpoint
    @DeleteMapping("/{produto_id}")
    public ResponseEntity<Void> removerProduto(@PathVariable("produto_id") Long produtoId) {
        boolean removido = removerProdutoUseCase.removerPorId(produtoId);
        if (removido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar todos os produtos ou filtrar por categoria (Público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos"),
            @ApiResponse(responseCode = "400", description = "Categoria inválida fornecida no filtro")
    })
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarOuBuscarPorCategoria(
            @Parameter(name = "categoria", description = "Nome da categoria para filtrar os produtos (opcional). Valores: LANCHE, ACOMPANHAMENTO, BEBIDA, SOBREMESA",
                    in = ParameterIn.QUERY, schema = @Schema(type = "string", enumAsRef = true, allowableValues = {"LANCHE", "ACOMPANHAMENTO", "BEBIDA", "SOBREMESA"}))
            @RequestParam(required = false) String categoriaNome) {

        List<Produto> produtos;
        if (categoriaNome != null && !categoriaNome.trim().isEmpty()) {
            try {
                Categoria categoriaEnum = Categoria.fromString(categoriaNome.toUpperCase());
                produtos = buscarProdutoPorCategoriaUseCase.executar(categoriaEnum);
            } catch (IllegalArgumentException e) {
                // Se Categoria.fromString falhar, o ExceptionHandler de IllegalArgumentException tratará
                throw e; // Re-lança para ser pego pelo ExceptionHandler
            }
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
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{produto_id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable("produto_id") Long produtoId) {
        return buscarProdutoPorIdUseCase.buscarPorId(produtoId)
                .map(produto -> ResponseEntity.ok(ProdutoDTO.fromDomain(produto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Exception Handlers

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("Erro de validação: {}", errors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(ERRO_KEY, ex.getMessage());
        logger.warn("Argumento ilegal ou categoria inválida: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ApplicationServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleApplicationServiceException(ApplicationServiceException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(ERRO_KEY, ex.getMessage());
        logger.error("Erro na camada de aplicação: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error("Erro inesperado na aplicação: {}", ex.getMessage(), ex);
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(ERRO_KEY, ERRO_INESPERADO_MSG);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}