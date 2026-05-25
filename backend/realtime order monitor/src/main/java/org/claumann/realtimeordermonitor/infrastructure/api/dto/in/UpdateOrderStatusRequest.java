package org.claumann.realtimeordermonitor.infrastructure.api.dto.in;

import org.claumann.realtimeordermonitor.domain.model.OrderStatus;

public record UpdateOrderStatusRequest(OrderStatus status) {
}
