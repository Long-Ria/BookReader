package Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"bookId", "categoryId"})
public class BookCategoryCrossRef {
    public int bookId;
    public int categoryId;
}
