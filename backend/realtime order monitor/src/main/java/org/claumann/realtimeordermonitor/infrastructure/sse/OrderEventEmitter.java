package org.claumann.realtimeordermonitor.infrastructure.sse;

import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.infrastructure.sse.dto.out.OrderStatusResponse;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class OrderEventEmitter {

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(final Order order) {
        // Cria novo emitter
        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // Registra callbacks para remover o emitter recém criado quando estiver completado
        final Runnable cleanup = () -> {
            final List<SseEmitter> list = emitters.get(order.getId());
            if (list != null) list.remove(emitter);
        };

        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(ex -> cleanup.run());

        // Se a chave não existe, cria a lista e retorna ela. Se já existe, retorna a lista existente.
        // Em ambos os casos você chama .add(emitter) no resultado.
        emitters.computeIfAbsent(order.getId(), k -> new CopyOnWriteArrayList<>()).add(emitter);
        publish(order);
        return emitter;
    }

    public void publish(final Order order) {
        final List<SseEmitter> sseEmitters = emitters.get(order.getId());
        if (sseEmitters == null || sseEmitters.isEmpty()) return;

        final OrderStatusResponse payload = OrderStatusResponse.fromDomain(order);

        sseEmitters.forEach(emitter -> {
            try {
                emitter.send(payload, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                sseEmitters.remove(emitter);
            }
        });
    }

    public void complete(final String orderId) {
        final List<SseEmitter> sseEmitters = emitters.get(orderId);
        if (!sseEmitters.isEmpty()) {
            sseEmitters.forEach(SseEmitter::complete);
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void heartbeat() {
        emitters.forEachValue(Long.MAX_VALUE, list -> {
            list.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().comment("heartbeat"));
                } catch (IOException e) {
                    list.remove(emitter);
                }
            });
        });
    }

}
