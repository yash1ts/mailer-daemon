package com.mailerdaemon.app.Attendance;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mailerdaemon.app.ApplicationClass;
import com.mailerdaemon.app.Attendance.Database.AppDatabase;
import com.mailerdaemon.app.Attendance.Database.Subject;
import com.mailerdaemon.app.R;

import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandance);


        recyclerView=findViewById(R.id.rv_attendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new AttendanceAdapter();
        recyclerView.setAdapter(adapter);
        new DataBase().execute();

    }

    private class DataBase extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            ApplicationClass applicationClass= (ApplicationClass) getApplication();
            AppDatabase database=applicationClass.getDb();
            Subject subject=new Subject();
            subject.id=123;
            subject.name="English";
            subject.present=32;
            subject.total=34;
            database.subjectDao().insertAll(subject);
            List<Subject> list=database.subjectDao().getAll();
            adapter.setData(list);
            return null;
        }

    }
}
