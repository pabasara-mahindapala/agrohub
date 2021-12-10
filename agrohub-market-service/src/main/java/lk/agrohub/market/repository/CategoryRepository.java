package lk.agrohub.market.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import lk.agrohub.market.model.Category;

public interface CategoryRepository extends MongoRepository<Category, Long> {
//    Category findBy_id(ObjectId _id);
}
