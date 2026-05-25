package org.claumann.realtimeordermonitor.infrastructure.persistence;

import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.domain.model.OrderStatus;
import org.claumann.realtimeordermonitor.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, OrderEntity> orderRepository = new HashMap<>();

    {
        final String firstOrderId = "c3fe75bf-a5ff-4026-a261-097112fead43";
        orderRepository.put(firstOrderId, new OrderEntity(firstOrderId, OrderStatus.RECEIVED, LocalDateTime.now()));

        final String secondOrderId = "d397135e-70ff-4d17-a003-8e8aa17ef12b";
        orderRepository.put(secondOrderId, new OrderEntity(secondOrderId, OrderStatus.READY, LocalDateTime.now()));

        final String thirdOrderId = "f8afdb91-7496-457a-95b6-a5a07d4976a3";
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
