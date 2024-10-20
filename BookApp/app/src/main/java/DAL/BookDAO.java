package DAL;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import Relationships.BookWithCategory;

@Dao
public interface BookDAO {


    @Transaction
    @Query("SELECT * FROM Books")
    public default List<BookWithCategory> getBooksWithCategory() {
        return null;
    }
}
