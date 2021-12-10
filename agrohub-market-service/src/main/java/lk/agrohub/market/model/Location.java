package lk.agrohub.market.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "location")
public class Location {
    @Transient
    public static final String SEQUENCE_NAME = "locations_sequence";

    @Id
    private long id;

    private Float longitude;
    private Float latitude;

    public Location(Float longitude, Float latitude) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
