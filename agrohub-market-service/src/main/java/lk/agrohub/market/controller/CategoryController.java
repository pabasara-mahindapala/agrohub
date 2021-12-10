package lk.agrohub.market.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lk.agrohub.market.dtos.UserDto;
import lk.agrohub.market.model.User;
import lk.agrohub.market.security.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lk.agrohub.market.model.Category;
import lk.agrohub.market.service.CategoryService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rest/category")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<Category>> listAllCategory() {
        try {
            return new ResponseEntity<>(this.categoryService.getAllCategory(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get list", e);
            return new ResponseEntity(new MessageResponse("Unable to get list"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Category> getCategory(@PathVariable long categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);

            // throw exception if null

            if (category == null) {
                throw new RuntimeException("Account not found");
            }

            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get category", e);
            return new ResponseEntity(new MessageResponse("Unable to get category"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        ResponseEntity<Category> result;
        try {
            this.categoryService.addCategory(category);
            result = new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to add category", e);
            result = new ResponseEntity(new MessageResponse("Unable to add category"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) throws Exception {
        ResponseEntity<Category> result;
        try {
            this.categoryService.updateCategory(category);
            result = new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to update category", e);
            result = new ResponseEntity(new MessageResponse("Unable to update category"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @DeleteMapping("/delete/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable long categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);

            // throw exception if null
            if (category == null) {
                throw new RuntimeException("Category not found");
            }

            categoryService.deleteCategory(category);

            return new ResponseEntity<>(new MessageResponse("Deleted Category : " + category.getCategoryName()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to delete category", e);
            return new ResponseEntity<>(new MessageResponse("Unable to delete category"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
