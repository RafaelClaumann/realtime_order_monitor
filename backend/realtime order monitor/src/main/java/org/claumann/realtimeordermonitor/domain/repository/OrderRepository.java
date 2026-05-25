package org.claumann.realtimeordermonitor.domain.repository;

import org.claumann.realtimeordermonitor.domain.model.Order;

public interface OrderRepository {

    Order saveOrder(final Order order);

    Order fetchOrderById(final String id);

}
