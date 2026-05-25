package org.claumann.realtimeordermonitor.infrastructure.api.dto.out;


import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.domain.model.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponse(String id, OrderStatus status, LocalDateTime createdAt) {

    public static OrderResponse fromDomain(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

}
