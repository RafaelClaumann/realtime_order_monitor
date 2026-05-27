package org.claumann.realtimeordermonitor.infrastructure.api;

import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.domain.model.OrderStatus;
import org.claumann.realtimeordermonitor.domain.usecase.GetOrderUsecase;
import org.claumann.realtimeordermonitor.domain.usecase.UpdateOrderStatusUsecase;
import org.claumann.realtimeordermonitor.infrastructure.api.dto.in.UpdateOrderStatusRequest;
import org.claumann.realtimeordermonitor.infrastructure.api.dto.out.OrderResponse;
import org.claumann.realtimeordermonitor.infrastructure.sse.OrderEventEmitter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final GetOrderUsecase getOrderUsecase;
    private final UpdateOrderStatusUsecase updateOrderStatusUsecase;
    private final OrderEventEmitter eventEmitter;

    public OrderController(GetOrderUsecase getOrderUsecase, UpdateOrderStatusUsecase updateOrderStatusUsecase, OrderEventEmitter eventEmitter) {
        this.getOrderUsecase = getOrderUsecase;
        this.updateOrderStatusUsecase = updateOrderStatusUsecase;
        this.eventEmitter = eventEmitter;
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponse> fetchOrder(@PathVariable final String id) {
        final Order order = getOrderUsecase.execute(id);
        final OrderResponse response = OrderResponse.fromDomain(order);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> patchOrderStatus(@PathVariable final String id, @RequestBody UpdateOrderStatusRequest requestBody) {
        final Order order = updateOrderStatusUsecase.execute(id, requestBody.status());
        eventEmitter.publish(order);

        if (OrderStatus.DELIVERED == requestBody.status())
            eventEmitter.complete(id);

        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("{id}/events")
    public SseEmitter fetchOrderEvents(@PathVariable final String id) {
        final Order order = getOrderUsecase.execute(id);
        return eventEmitter.register(order);
    }

}
