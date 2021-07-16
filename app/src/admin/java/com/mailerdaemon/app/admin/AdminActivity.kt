package com.mailerdaemon.app.admin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.mailerdaemon.app.*
import com.mailerdaemon.app.utils.ImageUploadCallBack
import com.mailerdaemon.app.utils.UploadData
import kotlinx.android.synthetic.admin.activity_admin.*

class AdminActivity : AppCompatActivity(), ImageUploadCallBack {
    private var path: String? = null
    private var downloadUrl: String? = null
    private val topics = arrayOf("event", "campus", "placement")
    private val mTitle = mapOf("event" to "Event", "placement" to "PlacementDeamon", "campus" to "CampusDeamon")
    private val mClickAction = mapOf(
        "event" to "EventsActivity", "placement" to "PlacementActivity", "campus" to "NoticesActivity"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val spinner = findViewById<Spinner>(R.id.spinner_tag)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, topics)
            spinner.adapter = adapter
        }

        bt_img_notification.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1000
                )
            } else {
                pickImageFromGallery()
            }
        }

        send_notification.setOnClickListener {
            val detail = detail_notification.text.toString()
            val topic = spinner.selectedItem.toString()
            val title = mTitle[topic]
            val clickAction = mClickAction[topic]
            if (title.isNullOrEmpty() || clickAction.isNullOrEmpty()) {
                this.toast("Enter Detail for notification!")
            } else {
                progress_bar.visibility = View.VISIBLE
                UploadData.upload(this, path, this)
                sendNotificationToUser(topic, title, detail, clickAction, downloadUrl)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
            image_notification.visibility = View.VISIBLE
            image_notification.setImageURI(data?.data)
            path = getPath(this, data?.data)
        }
    }

    private fun getPath(context: Context, uri: Uri?): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = uri?.let {
            context.contentResolver.query(it, proj, null, null, null)
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

    private fun sendNotificationToUser(
        topic: String,
        title: String,
        body: String,
        click_Action: String,
        image: String?
    ) {
        val notification = NotificationModel.Android.Notification(title, body, click_Action, image)
        val mUser = FirebaseAuth.getInstance().currentUser ?: return
        mUser.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result.token ?: ""
                    val data = mapOf(Pair("token", idToken))
                    val rootModel = NotificationModel(NotificationModel.Android(notification), data, topic)

                    (application as ApplicationClass).repository.sendNotification(rootModel)
                        .enqueue(object : retrofit2.Callback<ServerResponse> {
                            override fun onResponse(
                                call: retrofit2.Call<ServerResponse>,
                                response: retrofit2.Response<ServerResponse>
                            ) {
                                if (response.isSuccessful) {
                                    baseContext.toast(response.message())
                                } else {
                                    baseContext.toast(response.message())
                                }
                                progress_bar.visibility = View.GONE
                                Log.d("Send Notification", response.toString())
                                onBackPressed()
                            }

                            override fun onFailure(call: retrofit2.Call<ServerResponse>, t: Throwable) {
                                baseContext.toast("Failed")
                                progress_bar.visibility = View.GONE
                                t.message?.let { Log.d("Send Notification", it) }
                            }
                        })
                } else {
                    this.toast(getString(R.string.AuthFailed))
                }
            }
    }

    override fun onSuccess(downloadUrl: String?) {
        this.downloadUrl = downloadUrl
    }
}
