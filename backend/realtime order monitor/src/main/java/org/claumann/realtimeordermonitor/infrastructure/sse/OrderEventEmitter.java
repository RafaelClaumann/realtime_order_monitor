package org.claumann.realtimeordermonitor.infrastructure.sse;

import org.claumann.realtimeordermonitor.domain.model.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class OrderEventEmitter {

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(final String orderId) {
        // Cria novo emitter
        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // Registra callbacks para remover o emitter recém criado quando estiver completado
        final Runnable cleanup = () -> {
            final List<SseEmitter> list = emitters.get(orderId);
            if (list != null) list.remove(emitter);
        };

        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(ex -> cleanup.run());

        // Verifica se o pedido já foi adicionado ao mapa
        // Adiciona um novo emitter a lista de emitters do pedido
        // Se nao existir pedido registrado no mapa de emitters, cria uma lista e registra o pedido no mapa
        if (emitters.containsKey(orderId)) {
            emitters.get(orderId).add(emitter);
        } else {
            emitters.put(orderId, new CopyOnWriteArrayList<>(List.of(emitter)));
        }

        return emitter;
    }

    public void publish(final String orderId, final OrderStatus status) {
        final List<SseEmitter> sseEmitters = emitters.get(orderId);
        if (emitters.isEmpty()) return;

        sseEmitters.forEach(emitter -> {
            try {
                emitter.send(status.name());
            } catch (IOException e) {
                emitters.remove(orderId);
            }
        });
    }

    public void complete(final String orderId) {
        final List<SseEmitter> sseEmitters = emitters.get(orderId);
        if (!emitters.isEmpty()) {
            sseEmitters.forEach(SseEmitter::complete);
        }
    }

}
