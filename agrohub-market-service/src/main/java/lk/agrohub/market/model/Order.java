/**
 *
 */
package lk.agrohub.market.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lk.agrohub.market.enums.OrderStatusEnum;

@Document(collection = "order")
public class Order {
    @Transient
    public static final String SEQUENCE_NAME = "orders_sequence";

    @Id
    private long id;

    private Date insertDate = new Date();
    private Date lastUpdateDate;
    private OrderStatusEnum status;
    private long productId;
    private long producerId;
    private long customerId;
    private long locationId;
    private Integer quantity;

    private String receiverName;
    private String receiverMobileNumber;
    private String deliveryAddress;
    private String deliveryNearestCity;
    private String deliveryPostalCode;

    public Order(Date insertDate, Date lastUpdateDate, OrderStatusEnum status, long productId, long producerId,
                 long customerId, long locationId, Integer quantity, String receiverName, String receiverMobileNumber,
                 String deliveryAddress, String deliveryNearestCity, String deliveryPostalCode) {
        super();
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.status = status;
        this.productId = productId;
        this.producerId = producerId;
        this.customerId = customerId;
        this.locationId = locationId;
        this.quantity = quantity;
        this.receiverName = receiverName;
        this.receiverMobileNumber = receiverMobileNumber;
        this.deliveryAddress = deliveryAddress;
        this.deliveryNearestCity = deliveryNearestCity;
        this.deliveryPostalCode = deliveryPostalCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobileNumber() {
        return receiverMobileNumber;
    }

    public void setReceiverMobileNumber(String receiverMobileNumber) {
        this.receiverMobileNumber = receiverMobileNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryNearestCity() {
        return deliveryNearestCity;
    }

    public void setDeliveryNearestCity(String deliveryNearestCity) {
        this.deliveryNearestCity = deliveryNearestCity;
    }

    public String getDeliveryPostalCode() {
        return deliveryPostalCode;
    }

    public void setDeliveryPostalCode(String deliveryPostalCode) {
        this.deliveryPostalCode = deliveryPostalCode;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

}
