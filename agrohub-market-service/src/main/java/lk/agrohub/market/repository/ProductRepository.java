package lk.agrohub.market.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import lk.agrohub.market.customrepository.ProductCustomRepository;
import lk.agrohub.market.model.Product;

public interface ProductRepository extends MongoRepository<Product, Long>, ProductCustomRepository {
    List<Product> findByCategoryId(long categoryId);

    List<Product> findBySubCategoryId(long subCategoryId);

    List<Product> findByProducerId(long producerId);

    List<Product> findAllByOrderByInsertDateDesc();
}
