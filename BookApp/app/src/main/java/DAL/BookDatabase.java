package DAL;



import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import Models.BookCategoryCrossRef;
import Models.Books;
import Models.Categories;
import Models.Chapters;
import Models.Comments;
import Models.Pages;
import Models.Users;

import Converters.Converters;
@Database(entities = {Users.class, Books.class, Categories.class, Chapters.class, Comments.class, Pages.class, BookCategoryCrossRef.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class BookDatabase extends RoomDatabase {

    static Migration upgrade_v1_to_v2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("Alter table books Add Column image text");
            database.execSQL("Alter table books Add Column views INTEGER ");
        }
    };

    private static final String DATABASE_NAME = "BookApp.db";
    private static BookDatabase instance;

    public static synchronized BookDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            BookDatabase.class,
                            DATABASE_NAME)
                    .allowMainThreadQueries()
                    .addMigrations(upgrade_v1_to_v2)
                    .build();
        }
        return instance;
    }
    public abstract UserDAO userDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract BookDAO bookDAO();
    public abstract BookCategoryCrossRefDAO bookCategoryCrossRefDAO();
}
