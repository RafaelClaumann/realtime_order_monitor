package org.claumann.realtimeordermonitor.infrastructure.persistence;

import org.claumann.realtimeordermonitor.domain.model.Order;

public class OrderMapper {

    public static OrderEntity fromDomain(final Order order) {
        return new OrderEntity(order.getId(), order.getStatus(), order.getCreatedAt());
    }

    public static Order toDomain(final OrderEntity orderEntity) {
        return new Order(orderEntity.getId(), orderEntity.getStatus(), orderEntity.getCreatedAt());
    }

}
