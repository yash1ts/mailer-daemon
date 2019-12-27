package com.mailerdaemon.app.Attendance.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Subject.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SubjectDao subjectDao();
}
