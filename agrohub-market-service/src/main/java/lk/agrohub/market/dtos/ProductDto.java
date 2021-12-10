package lk.agrohub.market.dtos;

import java.util.HashMap;

import lk.agrohub.market.model.Product;

public class ProductDto {
    private Product product;

    private String categoryName;

    private String subCategoryName;

    private String producerName;

    private String producerMobileNumber;

    private HashMap<Integer, String> imagePaths;

    public ProductDto(Product product, String categoryName, String subCategoryName, String producerName,
                      String producerMobileNumber, HashMap<Integer, String> imagePaths) {
        super();
        this.product = product;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.producerName = producerName;
        this.producerMobileNumber = producerMobileNumber;
        this.imagePaths = imagePaths;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getProducerMobileNumber() {
        return producerMobileNumber;
    }

    public void setProducerMobileNumber(String producerMobileNumber) {
        this.producerMobileNumber = producerMobileNumber;
    }

    public HashMap<Integer, String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(HashMap<Integer, String> imagePaths) {
        this.imagePaths = imagePaths;
    }

}
