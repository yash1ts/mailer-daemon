package com.mailerdaemon.app.Attendance.Database;

import androidx.room.TypeConverter;

import java.util.Calendar;

class DateConverter {

    @TypeConverter
    public static Calendar toCalendar(long date){
        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal;
    }

    @TypeConverter
    public static long fromCalendar(Calendar calendar){
        return calendar.getTimeInMillis();
    }
}