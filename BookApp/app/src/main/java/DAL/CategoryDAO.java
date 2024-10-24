package DAL;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Models.Categories;

@Dao
public interface CategoryDAO {

    @Insert
    void insertCategory(Categories category);

    @Query("SELECT * FROM Categories WHERE categoryName = :categoryName LIMIT 1")
    Categories getCategoryByName(String categoryName);

    @Query("SELECT * FROM Categories")
    List<Categories> getAllCategories();
}
