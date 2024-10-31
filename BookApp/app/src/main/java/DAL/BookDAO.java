package DAL;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Models.Books;

@Dao
public interface BookDAO {

    @Insert
    void insertBook(Books book);

    @Query("SELECT * FROM Books WHERE bookName = :bookName AND bookAuthor = :bookAuthor LIMIT 1")
    Books getBookByNameAndAuthor(String bookName, String bookAuthor);

    @Query("SELECT * FROM Books")
    List<Books> getAllBooks();

    @Query("SELECT * FROM Books ORDER BY views DESC")
    List<Books> getBooksByTopViews();

    @Query("SELECT * FROM Books WHERE bookName = :bookName LIMIT 1")
    Books getBookByName(String bookName);

    @Query("SELECT * FROM Books WHERE bookId = :bookId LIMIT 1")
    Books getBookById(int bookId);

    @Query("UPDATE Books SET views = views + 1 WHERE bookId = :bookId")
    void updateViews(int bookId);
}
