package lk.agrohub.market.customrepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import lk.agrohub.market.model.Product;

@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<Product> findByMultiple(Long categoryId, Long subCategoryId, Long producerId) {
        final Query query = new Query();
        final List<Criteria> criteria = new ArrayList<>();
        if (categoryId != null)
            criteria.add(Criteria.where("categoryId").is(categoryId));
        if (subCategoryId != null)
            criteria.add(Criteria.where("subCategoryId").in(subCategoryId));
        if (producerId != null)
            criteria.add(Criteria.where("producerId").is(producerId));

        if (!criteria.isEmpty())
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        return mongoTemplate.find(query, Product.class);
    }
}