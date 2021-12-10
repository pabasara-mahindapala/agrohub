package lk.agrohub.market.customrepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import lk.agrohub.market.enums.OrderStatusEnum;
import lk.agrohub.market.model.Order;

@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Order> findByMultiple(Long productId, Long producerId, Long customerId, OrderStatusEnum status) {
        final Query query = new Query();

        final List<Criteria> criteria = new ArrayList<>();

        if (productId != null)
            criteria.add(Criteria.where("productId").is(productId));

        if (producerId != null)
            criteria.add(Criteria.where("producerId").in(producerId));

        if (customerId != null)
            criteria.add(Criteria.where("customerId").is(customerId));

        if (status != null)
            criteria.add(Criteria.where("status").is(status));

        if (!criteria.isEmpty())
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));

        return mongoTemplate.find(query, Order.class);
    }
}