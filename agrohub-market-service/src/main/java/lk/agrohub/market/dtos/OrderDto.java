package lk.agrohub.market.dtos;

import lk.agrohub.market.model.Order;

import java.util.Date;

public class OrderDto {
    private Order order;

    private String producerName;
    private String productName;
    private String customerName;
    private Date availableDate;
    private Float latitude;
    private Float longitude;

    public OrderDto(Order order, String producerName, String productName, String customerName, Date availableDate, Float latitude, Float longitude) {
        super();
        this.order = order;
        this.producerName = producerName;
        this.productName = productName;
        this.customerName = customerName;
        this.availableDate = availableDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = availableDate;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}
