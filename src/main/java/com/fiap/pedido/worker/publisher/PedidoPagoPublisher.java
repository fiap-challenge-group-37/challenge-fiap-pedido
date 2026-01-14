package com.fiap.pedido.worker.publisher;

// O ObjectMapper não é mais necessário aqui, o SqsTemplate resolve sozinho
// import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.pedido.domain.dto.PedidoPagoEvento;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoPagoPublisher {

    private final SqsTemplate sqsTemplate;
    // private final ObjectMapper objectMapper; // Pode remover a injeção dele se quiser limpar

    @Value("${events.queue.pedido-pago}")
    private String queueName;

    public void publicarPedidoPago(PedidoPagoEvento evento) {
        try {
            log.info("Publicando evento de pedido pago: {}", evento.idPedido());

            // --- AQUI ESTÁ A CORREÇÃO ---
            // Removemos a conversão manual para String.
            // Usamos o método fluente (.payload) para enviar o Objeto Java.
            sqsTemplate.send(to -> to.queue(queueName).payload(evento));
            // -----------------------------

            log.info("Evento publicado com sucesso na fila: {}", queueName);
        } catch (Exception e) {
            log.error("Erro ao publicar evento de pedido pago:  {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao publicar evento de pedido pago", e);
        }
    }
}