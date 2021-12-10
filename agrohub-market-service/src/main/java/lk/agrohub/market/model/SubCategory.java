package lk.agrohub.market.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subCategory")
public class SubCategory {
    @Transient
    public static final String SEQUENCE_NAME = "subcategories_sequence";

    @Id
    private long id;

    private Date insertDate;
    private Date lastUpdateDate;
    private long categoryId;
    private String subCategoryName;


    public SubCategory(Date insertDate, Date lastUpdateDate, long categoryId, String subCategoryName) {
        this.insertDate = insertDate;
        this.lastUpdateDate = lastUpdateDate;
        this.categoryId = categoryId;
        this.subCategoryName = subCategoryName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
}
