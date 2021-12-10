/**
 *
 */
package lk.agrohub.market.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import lk.agrohub.market.customrepository.OrderCustomRepository;
import lk.agrohub.market.model.Order;

public interface OrderRepository extends MongoRepository<Order, Long>, OrderCustomRepository {
    List<Order> findByProductId(long productId);
    List<Order> findByProducerId(long producerId);
}
