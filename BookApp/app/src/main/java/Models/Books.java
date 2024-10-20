package Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Books {
    @PrimaryKey(autoGenerate = true)
    private int bookId;
    private String bookName;
    private String bookAuthor;
    private int status;
    private String bookDescription;
    private int userId;

    public Books() {
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Books(int bookId, String bookName, String bookAuthor, int status, String bookDescription, int userId) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.status = status;
        this.bookDescription = bookDescription;
        this.userId = userId;
    }
}
