package lk.agrohub.market.dtos;

import lk.agrohub.market.model.Order;

public class CreateOrderInput {
    private Order order;
    private Float longitude;
    private Float latitude;

    public CreateOrderInput(Order order, Float longitude, Float latitude) {
        super();
        this.order = order;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

}
