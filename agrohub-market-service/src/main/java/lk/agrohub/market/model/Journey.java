package lk.agrohub.market.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "journey")
public class Journey {
    @Transient
    public static final String SEQUENCE_NAME = "journeys_sequence";

    @Id
    private long id;

    private String fromLocation;
    private String toLocation;
    private Date insertDate;
    private Date lastUpdateDate;
    private Date journeyDate;
    private boolean isCompleted;

    private long vehicleId;

    public Journey(String fromLocation, String toLocation, Date insertDate, Date lastUpdateDate, Date journeyDate, boolean isCompleted, long vehicleId) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.journeyDate = journeyDate;
        this.isCompleted = isCompleted;
        this.vehicleId = vehicleId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
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

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
