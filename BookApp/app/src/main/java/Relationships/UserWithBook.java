package Relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import Models.Books;
import Models.Users;

public class UserWithBook {
    @Embedded public Users user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "userId"
    )
    public List<Books> books;

    public UserWithBook(Users user, List<Books> books) {
        this.user = user;
        this.books = books;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Books> getBooks() {
        return books;
    }

    public void setBooks(List<Books> books) {
        this.books = books;
    }
}
