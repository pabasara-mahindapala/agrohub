package lk.agrohub.market.customrepository;

import java.util.List;

import lk.agrohub.market.model.Product;

public interface ProductCustomRepository {
    public List<Product> findByMultiple(Long categoryId, Long subCategoryId, Long producerId);
}
