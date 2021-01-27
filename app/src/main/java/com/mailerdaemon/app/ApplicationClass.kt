package com.mailerdaemon.app

import android.app.Application
import android.webkit.WebView
import androidx.room.Room
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.mailerdaemon.app.attendance.database.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://md-app-server.herokuapp.com"
class ApplicationClass : Application() {
    lateinit var database: AppDatabase
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        val config = ImagePipelineConfig.newBuilder(this)
            .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
            .setResizeAndRotateEnabledForNetwork(true)
            .setDownsampleEnabled(true)
            .build()
        Fresco.initialize(this, config)

        database = Room.databaseBuilder(this, AppDatabase::class.java, "attendance").build()

        WebView.setWebContentsDebuggingEnabled(false)

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        repository = retrofit.create(Repository::class.java)
    }
}
