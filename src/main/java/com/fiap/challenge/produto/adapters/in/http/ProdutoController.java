package com.fiap.challenge.produto.adapters.in.http;

import com.fiap.challenge.config.exception.dto.ErrorResponseDTO;
import com.fiap.challenge.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.challenge.produto.application.port.in.*;
import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produto Controller", description = "Operações para visualização e gerenciamento de produtos")
public class ProdutoController {

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
            @ApiResponse(responseCode = "400", description = "Dados inválidos para o produto",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Requer token JWT",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados enviados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProdutoDTO> criarProduto(@Valid @RequestBody ProdutoDTO dto) {
        Produto novoProduto = criarProdutoUseCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoDTO.fromDomain(novoProduto));
    }

    @Operation(summary = "Editar produto existente por ID (Administrativo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Requer token JWT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados enviados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{produto_id}")
    public ResponseEntity<ProdutoDTO> editarProduto(@PathVariable("produto_id") Long produtoId,
                                                    @Valid @RequestBody ProdutoDTO dto) {
        Produto produtoAtualizado = atualizarProdutoUseCase.executar(produtoId, dto);
        return ResponseEntity.ok(ProdutoDTO.fromDomain(produtoAtualizado));
    }

    @Operation(summary = "Remover produto por ID (Administrativo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado - Requer token JWT",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{produto_id}")
    public ResponseEntity<Void> removerProduto(@PathVariable("produto_id") Long produtoId) {
        removerProdutoUseCase.removerPorId(produtoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos os produtos ou filtrar por categoria (Público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Categoria inválida fornecida no filtro",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarOuBuscarPorCategoria(
            @Parameter(name = "categoria", description = "Nome da categoria (LANCHE, ACOMPANHAMENTO, BEBIDA, SOBREMESA)",
                    in = ParameterIn.QUERY, schema = @Schema(type = "string", allowableValues = {"LANCHE", "ACOMPANHAMENTO", "BEBIDA", "SOBREMESA"}))
            @RequestParam(required = false) Categoria categoria) {

        List<Produto> produtos;
        if (categoria != null) {
            produtos = buscarProdutoPorCategoriaUseCase.executar(categoria);
        } else {
            produtos = listarTodosProdutosUseCase.executar();
        }

        if (produtos == null || produtos.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<ProdutoDTO> dtos = produtos.stream()
                .map(ProdutoDTO::fromDomain)
                .toList();
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
        Produto produto = buscarProdutoPorIdUseCase.buscarPorId(produtoId);
        return ResponseEntity.ok(ProdutoDTO.fromDomain(produto));
    }
}
