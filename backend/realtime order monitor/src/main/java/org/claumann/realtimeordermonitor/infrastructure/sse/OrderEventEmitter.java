package org.claumann.realtimeordermonitor.infrastructure.sse;

import org.claumann.realtimeordermonitor.domain.model.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderEventEmitter {

    private final ConcurrentHashMap<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(final String orderId) {
        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        if (emitters.containsKey(orderId)) {
            emitters.get(orderId).add(emitter);
            return emitter;
        }

        final Runnable cleanup = () -> emitters.remove(orderId);
        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError((e) -> cleanup.run());

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
