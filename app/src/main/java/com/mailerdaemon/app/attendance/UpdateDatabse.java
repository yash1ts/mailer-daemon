package com.mailerdaemon.app.attendance;

import com.mailerdaemon.app.attendance.database.Subject;

public interface UpdateDatabse {
    void delete(Subject subject);
    void update(Subject subject);
}
