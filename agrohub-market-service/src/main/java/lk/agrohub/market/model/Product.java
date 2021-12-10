package lk.agrohub.market.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
public class Product {
    @Transient
    public static final String SEQUENCE_NAME = "products_sequence";

    @Id
    private long id;

    private String productName;
    private String description;

    private Date insertDate;
    private Date lastUpdateDate;
    private Date availableDate;

    private long categoryId;
    private long subCategoryId;
    private long producerId;

    private Integer quantity;
    private Float pricePerUnit;
    private String city;

    public Product(String productName, String description, Date insertDate, Date lastUpdateDate, Date availableDate,
                   long categoryId, long subCategoryId, long producerId, Integer quantity, Float pricePerUnit, String city) {
        super();
        this.productName = productName;
        this.description = description;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.availableDate = availableDate;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.producerId = producerId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = availableDate;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
