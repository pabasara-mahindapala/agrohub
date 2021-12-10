package lk.agrohub.market.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "node")
public class Node {
    @Transient
    public static final String SEQUENCE_NAME = "nodes_sequence";

    @Id
    private long id;

    private long journeyId;
    private long locationId;
    private long orderId;

    public Node(long journeyId, long locationId, long orderId) {
        this.journeyId = journeyId;
        this.locationId = locationId;
        this.orderId = orderId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
