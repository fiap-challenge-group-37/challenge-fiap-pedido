package com.fiap. pedido.bdd;

import com.fiap.pedido. pedido.domain.entities.ItemPedido;
import com. fiap.pedido.pedido.domain.entities.Pedido;
import com.fiap. pedido.pedido.domain. entities.StatusPedido;
import com. fiap.pedido.pedido.domain.port.PedidoRepository;
import io.cucumber.java.Before;
import io.cucumber.java.pt. Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber. java.pt.Quando;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java. util.ArrayList;
import java. util.List;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.cloud.aws.sqs.enabled=false",
                "events.queue.pedido-pago=fila-teste-mock"
        }
)
@ActiveProfiles("test")
public class PedidoStepDefinitions {

    @Autowired
    private PedidoRepository pedidoRepository;

    private Pedido pedidoAtual;
    private List<Pedido> pedidosListados;
    private int quantidadeItens;

    @Before
    public void setup() {
        // Limpar dados antes de cada cenário (opcional)
    }

    @Dado("que existem produtos disponíveis")
    public void queExistemProdutosDisponiveis() {
        // Produtos já existem no sistema
    }

    @Quando("eu criar um pedido com {int} itens")
    public void euCriarUmPedidoComItens(int quantidade) {
        this.quantidadeItens = quantidade;

        List<ItemPedido> itens = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            itens.add(new ItemPedido(
                    null,
                    (long) i + 1,
                    "Produto " + (i + 1),
                    1,
                    BigDecimal.valueOf(10.00),
                    BigDecimal.valueOf(10.00)
            ));
        }

        BigDecimal valorTotal = BigDecimal.valueOf(10.00 * quantidade);

        pedidoAtual = new Pedido(
                null, 1L, itens, valorTotal,
                StatusPedido.RECEBIDO, LocalDateTime.now(), LocalDateTime.now(),
                null, null
        );

        pedidoAtual = pedidoRepository.save(pedidoAtual);
    }

    @Entao("o pedido deve ser criado com sucesso")
    public void oPedidoDeveSerCriadoComSucesso() {
        assertNotNull(pedidoAtual);
        assertNotNull(pedidoAtual.getId());
    }

    @Entao("o status do pedido deve ser {string}")
    public void oStatusDoPedidoDeveSer(String statusEsperado) {
        assertNotNull(pedidoAtual);
        assertEquals(statusEsperado, pedidoAtual.getStatus().name());
    }

    @Dado("que existe um pedido com status {string}")
    public void queExisteUmPedidoComStatus(String status) {
        // Criar um item para o pedido (necessário para validação)
        List<ItemPedido> itens = List.of(
                new ItemPedido(
                        null,
                        1L,
                        "Produto Teste",
                        1,
                        BigDecimal.valueOf(10.00),
                        BigDecimal.valueOf(10.00)
                )
        );

        pedidoAtual = new Pedido(
                null, 1L, itens, BigDecimal.valueOf(10.00),
                StatusPedido.valueOf(status), LocalDateTime.now(), LocalDateTime.now(),
                null, null
        );
        pedidoAtual = pedidoRepository.save(pedidoAtual);
    }

    @Quando("eu atualizar o status para {string}")
    public void euAtualizarOStatusPara(String novoStatus) {
        pedidoAtual.atualizarStatus(StatusPedido.valueOf(novoStatus));
        pedidoAtual = pedidoRepository.save(pedidoAtual);
    }

    @Dado("que existem {int} pedidos não finalizados")
    public void queExistemPedidosNaoFinalizados(int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            // Criar um item para cada pedido
            List<ItemPedido> itens = List.of(
                    new ItemPedido(
                            null,
                            1L,
                            "Produto " + (i + 1),
                            1,
                            BigDecimal.valueOf(10.00),
                            BigDecimal. valueOf(10.00)
                    )
            );

            Pedido pedido = new Pedido(
                    null, 1L, itens, BigDecimal.valueOf(10.00),
                    StatusPedido.RECEBIDO, LocalDateTime.now(), LocalDateTime.now(),
                    null, null
            );
            pedidoRepository.save(pedido);
        }
    }

    @Quando("eu listar os pedidos")
    public void euListarOsPedidos() {
        pedidosListados = pedidoRepository.findAll();
    }

    @Entao("devo receber uma lista com pelo menos {int} pedidos")
    public void devoReceberUmaListaComPeloMenosPedidos(int quantidade) {
        assertNotNull(pedidosListados);
        assertTrue(pedidosListados.size() >= quantidade,
                "Esperava pelo menos " + quantidade + " pedidos, mas encontrou " + pedidosListados.size());
    }
}