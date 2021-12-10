package lk.agrohub.market.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "image")
public class ImageModel {
    @Transient
    public static final String SEQUENCE_NAME = "images_sequence";

    @Id
    private long id;
    private int order;
    private Long userId;
    private Long productId;
    private String name;
    private String type;
    private Date insertDate;
    private String url;

    public ImageModel(int order, Long userId, Long productId, String name, String type, Date insertDate, String url) {
        super();
        this.order = order;
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.type = type;
        this.insertDate = insertDate;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}