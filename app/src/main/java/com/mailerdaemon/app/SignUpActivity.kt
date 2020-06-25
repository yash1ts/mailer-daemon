package com.mailerdaemon.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class SignUpActivity: AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String

    private fun startMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mAuth = FirebaseAuth.getInstance()
        (supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        signup.setOnClickListener {
            email = (login_email.text).toString().trim { it <= ' ' }
            password = (login_password.text).toString()
            if (email.isNotEmpty()) {
                if (password.isNotEmpty())
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful)
                        saveUser((mAuth.currentUser))
                    else
                        this.toast( "Error:" + task.exception)
                } else login_password.error = "Password cannot be empty"
            } else {
                login_email.error = "Email cannot be empty"
            }
        }
    }

    private fun saveUser(user: FirebaseUser?) {
        val model = UserModel(user?.uid,user?.displayName,user?.email,false)
        FirebaseFirestore.getInstance().collection(FB_USER).document(user!!.uid).set(model)
        getSharedPreferences(MAIN, Context.MODE_PRIVATE).edit().putString(U_ID, user.uid).apply()
        createNotificationChannel()
        val editor = getSharedPreferences(GENERAL, Context.MODE_PRIVATE).edit()
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 17
        calendar[Calendar.MINUTE] = 30
        editor.putLong(TIME_NOTI, calendar.timeInMillis)
        editor.putString("Name", user.displayName).apply()
        if (user.uid == ADMIN_ID) editor.putBoolean(ACCESS, true).apply()
        else editor.putBoolean(ACCESS, false).apply()
        startMain()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MailerDaemon"
            val description = "Remider of Attendance Manager"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) onBackPressed()
        return true
    }
}

