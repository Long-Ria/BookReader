package Relationships;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import Models.BookCategoryCrossRef;
import Models.Books;
import Models.Categories;

public class BookWithCategory {

    @Embedded
    public Books books;
    @Relation(
            parentColumn = "bookId",
            entityColumn = "bookId",
            associateBy = @Junction(BookCategoryCrossRef.class)
    )
    public List<Categories> songs;


    public BookWithCategory(Books books, List<Categories> songs) {
        this.books = books;
        this.songs = songs;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public List<Categories> getSongs() {
        return songs;
    }

    public void setSongs(List<Categories> songs) {
        this.songs = songs;
    }
}
