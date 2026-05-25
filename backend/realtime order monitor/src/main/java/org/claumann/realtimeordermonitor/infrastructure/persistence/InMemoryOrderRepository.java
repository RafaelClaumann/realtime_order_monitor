package org.claumann.realtimeordermonitor.infrastructure.persistence;

import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.domain.model.OrderStatus;
import org.claumann.realtimeordermonitor.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, OrderEntity> orderRepository = new HashMap<>();

    {
        final String firstOrderId = UUID.randomUUID().toString();
        orderRepository.put(firstOrderId, new OrderEntity(firstOrderId, OrderStatus.RECEIVED, LocalDateTime.now()));

        final String secondOrderId = UUID.randomUUID().toString();
        orderRepository.put(secondOrderId, new OrderEntity(secondOrderId, OrderStatus.READY, LocalDateTime.now()));

        final String thirdOrderId = UUID.randomUUID().toString();
        orderRepository.put(thirdOrderId, new OrderEntity(thirdOrderId, OrderStatus.IN_TRANSIT, LocalDateTime.now()));
    }

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
