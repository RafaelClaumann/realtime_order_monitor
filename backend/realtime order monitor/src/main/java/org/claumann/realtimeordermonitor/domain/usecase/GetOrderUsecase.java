package org.claumann.realtimeordermonitor.domain.usecase;

import org.claumann.realtimeordermonitor.domain.model.Order;
import org.claumann.realtimeordermonitor.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class GetOrderUsecase {

    private final OrderRepository repository;

    public GetOrderUsecase(OrderRepository repository) {
        this.repository = repository;
    }

    public Order execute(final String id) {
        return repository.fetchOrderById(id);
    }

}
