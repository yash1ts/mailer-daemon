package com.mailerdaemon.app.Attendance;

import com.mailerdaemon.app.Attendance.Database.Subject;

public interface UpdateDatabse {
    void delete(Subject subject);
    void update(Subject subject);
}
