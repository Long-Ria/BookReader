package DAL;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import Models.BookCategoryCrossRef;

@Dao
public interface BookCategoryCrossRefDAO {

    @Insert
    void insertBookCategoryCrossRef(BookCategoryCrossRef crossRef);

    @Query("SELECT * FROM BookCategoryCrossRef WHERE bookId = :bookId AND categoryId = :categoryId LIMIT 1")
    BookCategoryCrossRef getCrossRefByBookAndCategory(int bookId, int categoryId);


}
