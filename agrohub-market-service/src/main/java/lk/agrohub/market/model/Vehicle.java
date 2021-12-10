package lk.agrohub.market.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicle")
public class Vehicle {
    @Transient
    public static final String SEQUENCE_NAME = "vehicles_sequence";

    @Id
    private long id;

    private String regNo;
    private String type;
    private int capacity;
    private Date insertDate;
    private Date lastUpdateDate;

    private long transporterId;

    public Vehicle(String regNo, String type, int capacity, Date insertDate, Date lastUpdateDate, long transporterId) {
        this.regNo = regNo;
        this.type = type;
        this.capacity = capacity;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.transporterId = transporterId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public long getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(long transporterId) {
        this.transporterId = transporterId;
    }
}
