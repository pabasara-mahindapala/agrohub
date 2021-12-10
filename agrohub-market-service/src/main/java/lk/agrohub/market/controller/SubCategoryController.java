package lk.agrohub.market.controller;

import java.util.ArrayList;
import java.util.List;

import lk.agrohub.market.model.Node;
import lk.agrohub.market.security.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lk.agrohub.market.model.SubCategory;
import lk.agrohub.market.service.SubCategoryService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rest/subCategory")
public class SubCategoryController {
    private static final Logger logger = LoggerFactory.getLogger(SubCategoryController.class);

    @Autowired
    SubCategoryService subCategoryService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<SubCategory>> listAllSubCategory(@RequestParam(required = false) Long categoryId) {
        try {
            if (categoryId != null) {
                return new ResponseEntity<>(subCategoryService.getSubCategoryByCategoryId(categoryId), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(subCategoryService.getAllSubCategory(), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Unable to get list", e);
            return new ResponseEntity(new MessageResponse("Unable to get list"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{subCategoryId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<SubCategory> getSubCategory(@PathVariable long subCategoryId) {
        try {
            SubCategory subCategory = subCategoryService.getSubCategoryById(subCategoryId);

            // throw exception if null

            if (subCategory == null) {
                throw new RuntimeException("SubCategory not found");
            }

            return new ResponseEntity<>(subCategory, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get subCategory", e);
            return new ResponseEntity(new MessageResponse("Unable to get subCategory"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubCategory> createSubCategory(@RequestBody SubCategory subCategory) throws Exception {
        ResponseEntity<SubCategory> result;
        try {
            this.subCategoryService.addSubCategory(subCategory);
            result = new ResponseEntity<>(subCategory, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to add subCategory", e);
            result = new ResponseEntity(new MessageResponse("Unable to add subCategory"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubCategory> updateSubCategory(@RequestBody SubCategory subCategory) throws Exception {
        ResponseEntity<SubCategory> result;
        try {
            this.subCategoryService.updateSubCategory(subCategory);
            result = new ResponseEntity<>(subCategory, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to update subCategory", e);
            result = new ResponseEntity(new MessageResponse("Unable to update subCategory"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @DeleteMapping("/delete/{subCategoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSubCategory(@PathVariable long subCategoryId) {
        try {
            SubCategory subCategory = subCategoryService.getSubCategoryById(subCategoryId);

            // throw exception if null
            if (subCategory == null) {
                throw new RuntimeException("Sub Category not found");
            }

            subCategoryService.deleteSubCategory(subCategory);

            return new ResponseEntity<>(new MessageResponse("Deleted Sub Category : " + subCategory.getSubCategoryName()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to delete subCategory", e);
            return new ResponseEntity<>(new MessageResponse("Unable to delete subCategory"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
