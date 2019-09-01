package com.mailerdaemon.app.Attendance.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Subject {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "total")
    public int total;

    @ColumnInfo(name = "present")
    public int present;

}
