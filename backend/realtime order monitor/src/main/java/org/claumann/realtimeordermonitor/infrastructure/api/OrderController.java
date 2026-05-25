package org.claumann.realtimeordermonitor.infrastructure.api;

import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.domain.usecase.GetOrderUsecase;
import org.claumann.realtimeordermonitor.infrastructure.api.dto.out.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final GetOrderUsecase usecase;

    public OrderController(GetOrderUsecase usecase) {
        this.usecase = usecase;
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponse> fetchOrder(@PathVariable final String id) {
        final Order order = usecase.execute(id);
        final OrderResponse response = OrderResponse.fromDomain(order);
        return ResponseEntity.ok(response);
    }

}
