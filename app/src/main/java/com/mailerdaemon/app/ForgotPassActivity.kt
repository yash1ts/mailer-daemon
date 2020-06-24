package com.mailerdaemon.app

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.util.Objects.*

class ForgotPassActivity: AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        val auth = FirebaseAuth.getInstance()
        requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val send = findViewById<Button>(R.id.send)
        val email = findViewById<TextInputEditText>(R.id.email)
        send.setOnClickListener {
        val s = requireNonNull(email.text).toString().trim { it <= ' ' }
        if (s.isNotEmpty()) auth.sendPasswordResetEmail(s).addOnCompleteListener { task ->
        if (task.isSuccessful) {
        Toast.makeText(applicationContext, "Email Sent", Toast.LENGTH_SHORT).show()
        onBackPressed()
        } else Toast.makeText(applicationContext, requireNonNull(task.exception).toString(), Toast.LENGTH_SHORT).show()
        } else email.error = "Email cannot be empty"
        }
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) onBackPressed()
        return true
        }
}
