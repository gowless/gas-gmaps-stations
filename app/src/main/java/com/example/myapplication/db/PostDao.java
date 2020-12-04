package com.example.myapplication.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;



@Dao
public interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAll(List<Post> posts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Post post);

    @Update
    void update(Post post);

    @Query("DELETE FROM stations_db")
    public void nukeTable();

    @Delete
    void delete(Post post);

    @Query("SELECT * FROM stations_db")
    List<Post> getAllData();

    @Query("SELECT * FROM stations_db")
    LiveData<List<Post>> findAll();

    @Query("SELECT * FROM stations_db WHERE station_title LIKE '%' || :query || '%' OR fuel_type LIKE '%' || :query || '%'")
    LiveData<List<Post>> findSearchValue(String query);
}
