package com.mailerdaemon.app.Attendance.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SubjectDao {
    @Query("SELECT * FROM subject")
    List<Subject> getAll();

    @Insert
    void insertAll(Subject... subject);

    @Delete
    void delete(Subject subject);
}
