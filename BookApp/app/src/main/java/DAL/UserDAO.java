package DAL;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Models.Users;

@Dao
public interface UserDAO {

    @Insert
    void insertUser(Users users);

    @Update
    void updateUser(Users users);

    @Query("Select * from users")
    List<Users> getAllUsers();

    @Query("Select * from users where username = :username LIMIT 1")
    Users getUserByUsername(String username);

}
