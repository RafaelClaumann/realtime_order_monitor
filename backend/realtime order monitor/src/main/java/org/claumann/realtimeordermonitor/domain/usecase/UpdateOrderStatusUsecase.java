package org.claumann.realtimeordermonitor.domain.usecase;

import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.domain.model.OrderStatus;
import org.claumann.realtimeordermonitor.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateOrderStatusUsecase {

    private final OrderRepository repository;

    public UpdateOrderStatusUsecase(final OrderRepository repository) {
        this.repository = repository;
    }

    public Order execute(final String orderId, final OrderStatus newStatus) {
        final Order order = repository.fetchOrderById(orderId);
        order.updateStatus(newStatus);
        repository.saveOrder(order);
        return order;
    }

}
