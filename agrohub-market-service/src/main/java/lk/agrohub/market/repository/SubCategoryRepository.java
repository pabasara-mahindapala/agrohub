package lk.agrohub.market.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import lk.agrohub.market.model.SubCategory;

public interface SubCategoryRepository extends MongoRepository<SubCategory, Long> {
    //    SubCategory findBy_id(ObjectId _id);
    List<SubCategory> findByCategoryId(long categoryId);
}
