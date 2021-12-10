package lk.agrohub.market.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.agrohub.market.model.Category;
import lk.agrohub.market.repository.CategoryRepository;

import java.util.Date;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    public Category getCategoryById(long id) {
        return this.categoryRepository.findById(id).get();
    }

    public Category addCategory(Category category) {
        category.setInsertDate(new Date());
        category.setLastUpdateDate(new Date());
        Category newCategory = this.categoryRepository.save(category);
        return newCategory;
    }

    public Category updateCategory(Category category) {
        category.setLastUpdateDate(new Date());
        return this.categoryRepository.save(category);
    }

    public void deleteCategory(Category category) {
        this.categoryRepository.delete(category);
    }
}
