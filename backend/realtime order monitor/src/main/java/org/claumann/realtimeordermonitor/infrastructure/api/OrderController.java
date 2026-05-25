package org.claumann.realtimeordermonitor.infrastructure.api;

import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.domain.usecase.GetOrderUsecase;
import org.claumann.realtimeordermonitor.domain.usecase.UpdateOrderStatusUsecase;
import org.claumann.realtimeordermonitor.infrastructure.api.dto.in.UpdateOrderStatusRequest;
import org.claumann.realtimeordermonitor.infrastructure.api.dto.out.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final GetOrderUsecase getOrderUsecase;
    private final UpdateOrderStatusUsecase updateOrderStatusUsecase;

    public OrderController(GetOrderUsecase getOrderUsecase, UpdateOrderStatusUsecase updateOrderStatusUsecase) {
        this.getOrderUsecase = getOrderUsecase;
        this.updateOrderStatusUsecase = updateOrderStatusUsecase;
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponse> fetchOrder(@PathVariable final String id) {
        final Order order = getOrderUsecase.execute(id);
        final OrderResponse response = OrderResponse.fromDomain(order);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> patchOrderStatus(@PathVariable final String id, @RequestBody UpdateOrderStatusRequest requestBody) {
        updateOrderStatusUsecase.execute(id, requestBody.status());
        return ResponseEntity.noContent().build();
    }

}
