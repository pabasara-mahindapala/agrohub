package lk.agrohub.market.customrepository;

import java.util.List;

import lk.agrohub.market.enums.OrderStatusEnum;
import lk.agrohub.market.model.Order;

public interface OrderCustomRepository {
    public List<Order> findByMultiple(Long productId, Long producerId, Long customerId, OrderStatusEnum status);
}
