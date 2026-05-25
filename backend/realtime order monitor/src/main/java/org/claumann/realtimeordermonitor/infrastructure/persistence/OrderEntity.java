package org.claumann.realtimeordermonitor.infrastructure.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.claumann.realtimeordermonitor.domain.model.OrderStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    private String id;
    private OrderStatus status;
    private LocalDateTime createdAt;

}
