package lk.agrohub.market.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "review")
public class Review {
    @Transient
    public static final String SEQUENCE_NAME = "products_sequence";

    @Id
    private long id;

    private long reviewerId;
    private String reviewBody;

    private Date insertDate;
    private Integer rating;

    public Review(long reviewerId, String reviewBody, Date insertDate, Integer rating) {
        super();
        this.reviewerId = reviewerId;
        this.reviewBody = reviewBody;
        this.insertDate = insertDate;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }


}
