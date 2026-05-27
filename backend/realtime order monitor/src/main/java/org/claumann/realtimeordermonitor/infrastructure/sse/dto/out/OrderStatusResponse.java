package org.claumann.realtimeordermonitor.infrastructure.sse.dto.out;

import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.domain.model.OrderStatus;

import java.time.LocalDateTime;

public record OrderStatusResponse(OrderStatus orderStatus, String message, LocalDateTime updatedAt) {

    public static OrderStatusResponse fromDomain(final Order order) {
        return new OrderStatusResponse(
                order.getStatus(),
                resolveMessage(order.getStatus()),
                LocalDateTime.now()
        );
    }

    private static String resolveMessage(final OrderStatus status) {
        return switch (status) {
            case RECEIVED -> "Order received";
            case BEING_PREPARED -> "Your order is being prepared";
            case READY -> "Your order is ready";
            case IN_TRANSIT -> "Your order is on the way";
            case DELIVERED -> "Your order has been delivered";
        };
    }

}
