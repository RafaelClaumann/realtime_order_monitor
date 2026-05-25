package org.claumann.realtimeordermonitor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private String id;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public void updateStatus(final OrderStatus newStatus) {
        switch (this.status) {
            case RECEIVED -> {
                if (newStatus != OrderStatus.BEING_PREPARED)
                    throw new RuntimeException("RECEIVED can only go to BEING_PREPARED");

            }
            case BEING_PREPARED -> {
                if (newStatus != OrderStatus.READY)
                    throw new RuntimeException("BEING_PREPARED can only go to READY");

            }
            case READY -> {
                if (newStatus != OrderStatus.IN_TRANSIT)
                    throw new RuntimeException("READY can only go to IN_TRANSIT");

            }
            case IN_TRANSIT -> {
                if (newStatus != OrderStatus.DELIVERED)
                    throw new RuntimeException("IN_TRANSIT can only go to DELIVERED");

            }
            case DELIVERED -> {
                throw new RuntimeException("DELIVERED is final");
            }
        }

        this.status = newStatus;
    }

}
