package lk.agrohub.market.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransportServiceInputDto implements Serializable {

    private int vehicleCapacity;
    private List<TransportPoint> points;
    private String secret;


    public TransportServiceInputDto(int vehicleCapacity, String secret, String depotLatitude, String depotLongitude) {
        this.vehicleCapacity = vehicleCapacity;
        this.points = new ArrayList<>();
        this.points.add(new TransportPoint(
                0,
                new Float(depotLatitude),
                new Float(depotLongitude),
                0
        ));
        this.secret = secret;
    }

    public int getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(int vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    public List<TransportPoint> getPoints() {
        return points;
    }

    public void setPoints(List<TransportPoint> points) {
        this.points = points;
    }

    public void addPoint(TransportPoint transportPoint) {
        this.points.add(transportPoint);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
