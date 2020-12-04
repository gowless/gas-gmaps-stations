package com.example.myapplication.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;



@Database(entities = {Post.class}, version = 3 ,exportSchema = false)
public abstract class PostsDatabase extends RoomDatabase {

    private static PostsDatabase INSTANCE;

    private final static List<Post> POSTS = Arrays.asList(
           new Post("test", "test", "test", "test")
    );

    public abstract PostDao postDao();

    private static final Object sLock = new Object();

    public static PostsDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PostsDatabase.class, "Posts.db")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Executors.newSingleThreadExecutor().execute(
                                        () -> getInstance(context).postDao().saveAll(POSTS));
                            }
                        })
                        .build();
            }
            return INSTANCE;
        }
    }
}
