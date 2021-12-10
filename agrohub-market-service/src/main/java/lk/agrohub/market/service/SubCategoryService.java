package lk.agrohub.market.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.agrohub.market.model.SubCategory;
import lk.agrohub.market.repository.SubCategoryRepository;

@Service
public class SubCategoryService {
    @Autowired
    SubCategoryRepository subCategoryRepository;

    public List<SubCategory> getAllSubCategory() {
        return this.subCategoryRepository.findAll();
    }

    public SubCategory getSubCategoryById(long _id) {
        return this.subCategoryRepository.findById(_id).get();
    }

    public List<SubCategory> getSubCategoryByCategoryId(long categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }

    public SubCategory addSubCategory(SubCategory subCategory) {
        subCategory.setInsertDate(new Date());
        subCategory.setLastUpdateDate(new Date());
        SubCategory newSubCategory = this.subCategoryRepository.save(subCategory);
        return newSubCategory;
    }

    public SubCategory updateSubCategory(SubCategory subCategory) {
        subCategory.setLastUpdateDate(new Date());
        return this.subCategoryRepository.save(subCategory);
    }

    public void deleteSubCategory(SubCategory subCategory) {
        this.subCategoryRepository.delete(subCategory);
    }
}
