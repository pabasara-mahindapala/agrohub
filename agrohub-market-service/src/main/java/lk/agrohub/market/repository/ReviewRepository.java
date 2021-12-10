package lk.agrohub.market.repository;

import lk.agrohub.market.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, Long> {
//	Review findBy_id(ObjectId _id);
}
