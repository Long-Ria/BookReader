package DAL;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Models.Users;

@Dao
public interface UserDAO {

    @Insert
    void insertUser(Users users);

    @Delete
    void deleteUser(Users users);

    @Query("Select * from users")
    List<Users> getAllUsers();

    @Query("Select * from users where username = :username")
    Users getUserByUsername(String username);

}
