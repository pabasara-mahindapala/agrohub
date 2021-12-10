package lk.agrohub.market.dtos;

public class TransportPoint {

    private long orderId;
    private Float latitude;
    private Float longitude;
    private int quantity;

    public TransportPoint(long orderId, Float latitude, Float longitude, int quantity) {
        this.orderId = orderId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.quantity = quantity;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
