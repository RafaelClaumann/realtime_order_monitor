package org.claumann.realtimeordermonitor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Order {

    private final String id;
    private final OrderStatus status;
    private final LocalDateTime createdAt;

}
