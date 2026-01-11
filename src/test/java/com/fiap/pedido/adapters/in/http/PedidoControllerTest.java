package com.fiap.pedido.adapters.in.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.pedido.config.TestSecurityConfig;
import com.fiap.pedido.config.exception.GlobalRestExceptionHandler;
import com.fiap.pedido.pedido.adapters.in.http.PedidoController;
import com.fiap.pedido.pedido.adapters.in.http.dto.ItemPedidoDTO;
import com.fiap.pedido.pedido.adapters.in.http.dto.PedidoDTO;
import com.fiap.pedido.pedido.adapters.in.http.dto.StatusUpdateRequestDTO;
import com.fiap.pedido.pedido.application.port.in.AtualizarStatusPedidoUseCase;
import com.fiap.pedido.pedido.application.port.in.BuscarPedidoPorIdUseCase;
import com.fiap.pedido.pedido.application.port.in.CriarPedidoUseCase;
import com.fiap.pedido.pedido.application.port.in.ListarPedidosUseCase;
import com.fiap.pedido.pedido.domain.entities.ItemPedido;
import com.fiap.pedido.pedido.domain.entities.Pedido;
import com.fiap.pedido.pedido.domain.entities.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
class PedidoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CriarPedidoUseCase criarPedidoUseCase;

    @Mock
    private ListarPedidosUseCase listarPedidosUseCase;

    @Mock
    private BuscarPedidoPorIdUseCase buscarPedidoPorIdUseCase;

    @Mock
    private AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        WebClient.RequestBodyUriSpec uriSpecMock = Mockito.mock(WebClient.RequestBodyUriSpec.class, Mockito.RETURNS_SELF);
        WebClient.ResponseSpec responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        lenient().when(webClientBuilder.build()).thenReturn(webClient);
        lenient().when(webClient.post()).thenReturn(uriSpecMock);
        lenient().when(uriSpecMock.retrieve()).thenReturn(responseSpecMock);
        lenient().when(responseSpecMock.bodyToMono(eq(String.class)))
                .thenReturn(Mono.just("{ \"in_store_order_id\":\"1\", \"qr_data\": \"qr-code-mock\" }"));

        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
    }

    private Pedido criarPedidoMock() {
        return new Pedido(
                1L, 1L,
                Collections.singletonList(new ItemPedido(1L, "Hamburguer", 2, BigDecimal.valueOf(20.00))),
                BigDecimal.valueOf(40.00),
                StatusPedido.RECEBIDO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "PED-001",
                "qr-code-mock"
        );
    }

    @Test
    void deveCriarPedidoComSucesso() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(1L);

        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(1L);
        item.setNomeProduto("Hamburguer");
        item.setQuantidade(2);
        item.setPreco(BigDecimal.valueOf(20.00));
        pedidoDTO.setItens(Collections.singletonList(item));

        when(criarPedidoUseCase.executar(any(PedidoDTO.class))).thenReturn(criarPedidoMock());

        // Mock autenticação JWT
        Jwt jwtMock = Mockito.mock(Jwt.class);
        when(jwtMock.getTokenValue()).thenReturn("jwt-mock-token");
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        when(authenticationMock.getPrincipal()).thenReturn(jwtMock);
        SecurityContextHolder.getContext().setAuthentication(authenticationMock);

        mockMvc.perform(post("/pedidos/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("Recebido"));
    }

    @Test
    void deveCriarPedidoQuandoPagamentoFalhar() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(1L);
        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(1L);
        item.setNomeProduto("Hamburguer");
        item.setQuantidade(2);
        item.setPreco(BigDecimal.valueOf(20.00));
        pedidoDTO.setItens(Collections.singletonList(item));

        when(criarPedidoUseCase.executar(any(PedidoDTO.class))).thenReturn(criarPedidoMock());

        // Mock autenticação JWT
        Jwt jwtMock = Mockito.mock(Jwt.class);
        when(jwtMock.getTokenValue()).thenReturn("jwt-mock-token");
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        when(authenticationMock.getPrincipal()).thenReturn(jwtMock);
        SecurityContextHolder.getContext().setAuthentication(authenticationMock);

        // Mock chain para lançar exceção ao tentar chamar o serviço de pagamento
        WebClient.RequestBodyUriSpec uriSpecMock = Mockito.mock(WebClient.RequestBodyUriSpec.class, Mockito.RETURNS_DEEP_STUBS);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(anyString())).thenReturn(uriSpecMock);
        when(uriSpecMock.header(anyString(), anyString())).thenReturn(uriSpecMock);
        when(uriSpecMock.bodyValue(any())).thenThrow(new RuntimeException("Pagamento offline"));

        mockMvc.perform(post("/pedidos/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("Recebido"));
    }

    @Test
    void deveListarTodosPedidos() throws Exception {
        when(listarPedidosUseCase.executar(any(Optional.class)))
                .thenReturn(Collections.singletonList(criarPedidoMock()));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deveListarPedidosPorStatus() throws Exception {
        when(listarPedidosUseCase.executar(eq(Optional.of("RECEBIDO"))))
                .thenReturn(Collections.singletonList(criarPedidoMock()));

        mockMvc.perform(get("/pedidos").param("status", "RECEBIDO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("Recebido"));
    }

    @Test
    void deveBuscarPedidoPorId() throws Exception {
        when(buscarPedidoPorIdUseCase.buscarPorId(1L)).thenReturn(criarPedidoMock());

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveAtualizarStatusDoPedido() throws Exception {
        StatusUpdateRequestDTO statusDTO = new StatusUpdateRequestDTO();
        statusDTO.setNovoStatus("EM_PREPARACAO");

        Pedido pedidoAtualizado = new Pedido(
                1L, 1L,
                Collections.singletonList(new ItemPedido(1L, "Hamburguer", 2, BigDecimal.valueOf(20.00))),
                BigDecimal.valueOf(40.00),
                StatusPedido.EM_PREPARACAO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "PED-001",
                null
        );

        when(atualizarStatusPedidoUseCase.executar(eq(1L), eq("EM_PREPARACAO")))
                .thenReturn(pedidoAtualizado);

        mockMvc.perform(patch("/pedidos/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Em preparação"));
    }

    @Test
    void deveRetornar400QuandoPedidoInvalido() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setItens(new ArrayList<>());

        mockMvc.perform(post("/pedidos/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoDTO)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornarErroParaStatusInvalido() throws Exception {
        StatusUpdateRequestDTO statusDTO = new StatusUpdateRequestDTO();
        statusDTO.setNovoStatus("VOANDO");

        when(atualizarStatusPedidoUseCase.executar(eq(1L), eq("VOANDO")))
                .thenThrow(new IllegalArgumentException("Status inválido"));

        mockMvc.perform(patch("/pedidos/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isBadRequest());
    }
}