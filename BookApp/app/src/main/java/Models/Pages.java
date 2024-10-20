package Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Pages {
    @PrimaryKey(autoGenerate = true)
    private int pageId;
    private String content;
    private int pageNumber;

    private int chapterId;

    public Pages() {
    }

    public Pages(int pageId, String content, int pageNumber, int chapterId) {
        this.pageId = pageId;
        this.content = content;
        this.pageNumber = pageNumber;
        this.chapterId = chapterId;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }
}
