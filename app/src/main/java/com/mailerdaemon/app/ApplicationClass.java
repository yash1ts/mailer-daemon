package com.mailerdaemon.app;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.mailerdaemon.app.Attendance.Database.AppDatabase;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class ApplicationClass extends Application {
    private RefWatcher refWatcher;
    private AppDatabase db;

    public static RefWatcher getRefWatcher(Context context) {
        ApplicationClass application = (ApplicationClass) context.getApplicationContext();
        return application.refWatcher;
    }

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
        //refWatcher = LeakCanary.install(this);
    }
}
