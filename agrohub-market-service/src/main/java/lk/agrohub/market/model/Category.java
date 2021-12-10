package lk.agrohub.market.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "category")
public class Category {
	@Transient
    public static final String SEQUENCE_NAME = "categories_sequence";

    @Id
    private long id;

	private Date insertDate;
	private Date lastUpdateDate;
	private String categoryName;

    public Category() {
    }

    public Category(Date insertDate, Date lastUpdateDate, String categoryName) {
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.categoryName = categoryName;
    }

	public Date getInsertDate() {
		return insertDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
