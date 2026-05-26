package org.claumann.realtimeordermonitor.infrastructure.sse;

import org.claumann.realtimeordermonitor.domain.model.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderEventEmitter {

    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(final String orderId) {
        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(orderId, emitter);
        return emitter;
    }

    public void publish(final String orderId, final OrderStatus status) {
        final SseEmitter emitter = emitters.get(orderId);
        if (emitter == null) return;

        try {
            emitter.send(status.name());
        } catch (IOException e) {
            emitters.remove(orderId);
        }
    }

    public void complete(final String orderId) {
        final SseEmitter emitter = emitters.get(orderId);
        emitter.complete();
        emitters.remove(orderId);
    }

}
