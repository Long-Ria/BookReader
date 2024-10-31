package DAL;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import Models.BookCategoryCrossRef;
import Models.Books;
import Models.Categories;

@Dao
public interface BookCategoryCrossRefDAO {

    @Insert
    void insertBookCategoryCrossRef(BookCategoryCrossRef crossRef);

    @Delete
    void deleteBookCategoryCrossRef(BookCategoryCrossRef crossRef);

    @Query("SELECT * FROM BookCategoryCrossRef WHERE bookId = :bookId AND categoryId = :categoryId LIMIT 1")
    BookCategoryCrossRef getCrossRefByBookAndCategory(int bookId, int categoryId);

    @Query("SELECT * FROM BookCategoryCrossRef")
    List<BookCategoryCrossRef> getAllBookCategory();

    @Query("SELECT * FROM Categories")
    List<Categories> getAllCategories();

    // Fetch all books by category using cross-reference
    @Transaction
    @Query("SELECT * FROM Books INNER JOIN BookCategoryCrossRef ON Books.bookId = BookCategoryCrossRef.bookId WHERE BookCategoryCrossRef.categoryId = :categoryId")
    List<Books> getBooksByCategory(int categoryId);

    @Query("SELECT * FROM BookCategoryCrossRef WHERE bookId = :bookId")
    List<BookCategoryCrossRef> getBookCategoryByBookId(int bookId);

    @Query("Delete FROM BookCategoryCrossRef WHERE categoryId = :categoryId")
    void deleteBookCategoryByCategoryId(int categoryId);
}
