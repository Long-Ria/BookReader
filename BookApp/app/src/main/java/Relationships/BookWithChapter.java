package Relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import Models.Books;
import Models.Chapters;


public class BookWithChapter {

    @Embedded
    public Books books;
    @Relation(
            parentColumn = "bookId",
            entityColumn = "bookId"
    )
    public List<Chapters> chapters;

    public BookWithChapter(Books books, List<Chapters> chapters) {
        this.books = books;
        this.chapters = chapters;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public List<Chapters> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapters> chapters) {
        this.chapters = chapters;
    }
}
