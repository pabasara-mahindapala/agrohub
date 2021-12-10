package lk.agrohub.market.controller;

import lk.agrohub.market.dtos.ProductDto;
import lk.agrohub.market.model.ImageModel;
import lk.agrohub.market.model.Node;
import lk.agrohub.market.model.Product;
import lk.agrohub.market.repository.ImageRepository;
import lk.agrohub.market.security.response.MessageResponse;
import lk.agrohub.market.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rest/product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/all")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> listAllProduct(@RequestParam(required = false) Long categoryId,
                                                           @RequestParam(required = false) Long subCategoryId, @RequestParam(required = false) Long producerId) {
        try {
            return new ResponseEntity<>(this.productService.getAllProductsFiltered(categoryId, subCategoryId, producerId), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get products", e);
            return new ResponseEntity(new MessageResponse("Unable to get products"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/newproducts")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> listNewProducts() {
        try {
            return new ResponseEntity<>(this.productService.getNewProducts(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get products", e);
            return new ResponseEntity(new MessageResponse("Unable to get products"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<ProductDto> getProduct(@PathVariable long productId) {
        try {
            ProductDto product = productService.productsSearchById(productId);

            // throw exception if null

            if (product == null) {
                throw new RuntimeException("Product not found");
            }

            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get product", e);
            return new ResponseEntity(new MessageResponse("Unable to get product"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) throws Exception {
        ResponseEntity<Product> result;
        try {
            this.productService.addProduct(product);
            result = new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            result = new ResponseEntity<>(product, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) throws Exception {
        ResponseEntity<Product> result;
        try {
            this.productService.updateProduct(product);
            result = new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            result = new ResponseEntity<>(product, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public String deleteProduct(@PathVariable long productId) {

        ProductDto product = productService.productsSearchById(productId);

        // throw exception if null

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        productService.deleteProduct(product.getProduct());

        return "Deleted Product : " + product.getProduct().getProductName();
    }

    @PostMapping("/images/add")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public String addImage(@RequestParam String imageName, @RequestParam String imageUrl, @RequestParam String imageType, @RequestParam Long productId,
                           @RequestParam int order) {
        ImageModel image = new ImageModel(order, null, productId, imageName, imageType, new Date(), imageUrl);
        this.imageRepository.save(image);
        return "Added image : " + imageName;
    }
}
