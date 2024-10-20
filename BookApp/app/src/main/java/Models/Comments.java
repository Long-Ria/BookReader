package Models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comments {
    @PrimaryKey(autoGenerate = true)
    private int commentId;
    private String content;
    private int userId;
    private int bookId;

    public Comments() {
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Comments(int commentId, String content, int userId, int bookId) {
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
        this.bookId = bookId;
    }
}
