package Relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;


import Models.Chapters;
import Models.Pages;


public class ChapterWithPage {

    @Embedded
    public Chapters chapters;
    @Relation(
            parentColumn = "chapterId",
            entityColumn = "chapterId"
    )
    public List<Pages> pages;

    public Chapters getChapters() {
        return chapters;
    }

    public void setChapters(Chapters chapters) {
        this.chapters = chapters;
    }

    public List<Pages> getPages() {
        return pages;
    }

    public void setPages(List<Pages> pages) {
        this.pages = pages;
    }

    public ChapterWithPage(Chapters chapters, List<Pages> pages) {
        this.chapters = chapters;
        this.pages = pages;
    }
}
