package com.mailerdaemon.app;

import android.app.Application;

import androidx.room.Room;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.mailerdaemon.app.Attendance.Database.AppDatabase;


public class ApplicationClass extends Application {
    private AppDatabase db;
    private String BASE_URL="https://www.facebook.com/";

    public AppDatabase getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this,config);
        db = Room.databaseBuilder(this,
                AppDatabase.class, "attendance").build();




    }

}
