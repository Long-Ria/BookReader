package Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Categories {
    @PrimaryKey(autoGenerate = true)
    private int categoryId;
    private String categoryName;

    public Categories() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Categories(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
