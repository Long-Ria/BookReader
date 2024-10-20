package DAL;



import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import Models.Books;
import Models.Categories;
import Models.Chapters;
import Models.Comments;
import Models.Pages;
import Models.Users;

import Converters.Converters;
@Database(entities = {Users.class, Books.class, Categories.class, Chapters.class, Comments.class, Pages.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class BookDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "BookApp.db";
    private static BookDatabase instance;

    public static synchronized BookDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BookDatabase.class,
                    DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract UserDAO userDAO();
}
