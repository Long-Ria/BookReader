package Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Chapters {
    @PrimaryKey(autoGenerate = true)
    private int chapterId;
    private String chapterName;
    private Date createdDate;
    private int bookId;

    public Chapters() {
    }

    public Chapters(int chapterId, String chapterName, Date createdDate, int bookId) {
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.createdDate = createdDate;
        this.bookId = bookId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
