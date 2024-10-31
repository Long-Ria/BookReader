package DAL;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Models.Chapters;

@Dao
public interface ChapterDAO {

    @Insert
    void insertChapter(Chapters chapter);


    @Query("SELECT * FROM Chapters WHERE bookId = :bookId")
    List<Chapters> getChaptersByBookId(int bookId);
}
