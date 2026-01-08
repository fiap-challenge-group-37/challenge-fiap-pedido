package com.fiap.produto.adapters.in.http;

import com.fasterxml.jackson.databind. ObjectMapper;
import com.fiap.pedido.produto.adapters.in.http.ProdutoController;
import com.fiap.pedido.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap. pedido.produto.application.port.in.*;
import com.fiap.pedido.produto.domain.entities.Categoria;
import com.fiap.pedido. produto.domain.entities.Produto;
import org.junit.jupiter.api.BeforeEach;
import org. junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org. springframework.test.web.servlet. MockMvc;
import org. springframework.test.web.servlet. setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito. Mockito.*;
import static org.springframework.test. web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web. servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CriarProdutoUseCase criarProdutoUseCase;

    @Mock
    private AtualizarProdutoUseCase atualizarProdutoUseCase;

    @Mock
    private RemoverProdutoUseCase removerProdutoUseCase;

    @Mock
    private BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;

    @Mock
    private BuscarProdutoPorCategoriaUseCase buscarProdutoPorCategoriaUseCase;

    @Mock
    private ListarTodosProdutosUseCase listarTodosProdutosUseCase;

    @InjectMocks
    private ProdutoController produtoController;

    private ObjectMapper objectMapper;
    private Produto produtoMock;
    private ProdutoDTO produtoDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();
        objectMapper = new ObjectMapper();

        produtoMock = new Produto(
                1L,
                "Hamburguer",
                Categoria. LANCHE,
                BigDecimal.valueOf(20.00),
                "Hamburguer artesanal"
        );

        produtoDTO = new ProdutoDTO(
                null,
                "Hamburguer",
                "LANCHE",
                BigDecimal.valueOf(20.00),
                "Hamburguer artesanal"
        );
    }

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        when(criarProdutoUseCase.executar(any(ProdutoDTO.class))).thenReturn(produtoMock);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Hamburguer"))
                .andExpect(jsonPath("$.categoria").value("LANCHE"));

        verify(criarProdutoUseCase, times(1)).executar(any(ProdutoDTO.class));
    }

    @Test
    void deveEditarProdutoComSucesso() throws Exception {
        when(atualizarProdutoUseCase.executar(eq(1L), any(ProdutoDTO.class)))
                .thenReturn(produtoMock);

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Hamburguer"));

        verify(atualizarProdutoUseCase, times(1)).executar(eq(1L), any(ProdutoDTO.class));
    }

    @Test
    void deveRemoverProdutoComSucesso() throws Exception {
        doNothing().when(removerProdutoUseCase).removerPorId(1L);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());

        verify(removerProdutoUseCase, times(1)).removerPorId(1L);
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() throws Exception {
        when(buscarProdutoPorIdUseCase.buscarPorId(1L)).thenReturn(produtoMock);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Hamburguer"));

        verify(buscarProdutoPorIdUseCase, times(1)).buscarPorId(1L);
    }

    @Test
    void deveListarTodosProdutos() throws Exception {
        List<Produto> produtos = Arrays.asList(produtoMock);
        when(listarTodosProdutosUseCase.executar()).thenReturn(produtos);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Hamburguer"));

        verify(listarTodosProdutosUseCase, times(1)).executar();
    }

    @Test
    void deveBuscarProdutosPorCategoria() throws Exception {
        List<Produto> produtos = Arrays. asList(produtoMock);
        when(buscarProdutoPorCategoriaUseCase.executar(Categoria.LANCHE)).thenReturn(produtos);

        mockMvc.perform(get("/produtos")
                        .param("categoria", "LANCHE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoria").value("LANCHE"));

        verify(buscarProdutoPorCategoriaUseCase, times(1)).executar(Categoria.LANCHE);
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumProduto() throws Exception {
        when(listarTodosProdutosUseCase.executar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(listarTodosProdutosUseCase, times(1)).executar();
    }

    @Test
    void deveRetornarListaVaziaQuandoProdutosNull() throws Exception {
        when(listarTodosProdutosUseCase.executar()).thenReturn(null);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(listarTodosProdutosUseCase, times(1)).executar();
    }
}