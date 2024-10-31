package DAL;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Models.Pages;

@Dao
public interface PageDAO {

    @Insert
     void insertPage(Pages page);


    @Query("SELECT * FROM Pages WHERE chapterId = :chapterId")
    List<Pages> getPagesByChapterId(int chapterId);
}
