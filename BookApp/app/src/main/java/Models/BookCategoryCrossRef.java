package Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"bookId", "categoryId"})
public class BookCategoryCrossRef {
    public int bookId;
    public int categoryId;

    public BookCategoryCrossRef(int categoryId, int bookId) {
        this.categoryId = categoryId;
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
