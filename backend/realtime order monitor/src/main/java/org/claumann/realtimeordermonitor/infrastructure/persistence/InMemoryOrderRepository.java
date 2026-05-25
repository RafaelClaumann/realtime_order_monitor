package org.claumann.realtimeordermonitor.infrastructure.persistence;

import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, OrderEntity> orderRepository = new HashMap<>();

    public Order saveOrder(final Order order) {
        final OrderEntity entity = OrderMapper.fromDomain(order);
        orderRepository.put(entity.getId(), entity);
        return order;
    }

    public Order fetchOrderById(final String id) {
        final OrderEntity entity = orderRepository.get(id);
        return OrderMapper.toDomain(entity);
    }

}
