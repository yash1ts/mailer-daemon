package com.mailerdaemon.app.Attendance.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Calendar;

@Entity
@TypeConverters(DateConverter.class)
public class Subject {
  @PrimaryKey(autoGenerate = true)
  public int id;

  @ColumnInfo(name = "name")
  public String name;

  @ColumnInfo(name = "total")
  public int total;

  @ColumnInfo(name = "present")
  public int present;

  @ColumnInfo(name = "date_updated")
  public Calendar date;


}
