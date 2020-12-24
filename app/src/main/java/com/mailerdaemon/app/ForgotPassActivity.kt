package com.mailerdaemon.app

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_pass.email
import kotlinx.android.synthetic.main.fragment_add_placement.send

class ForgotPassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        val auth = FirebaseAuth.getInstance()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        send.setOnClickListener {
            val s = email.toString().trim()
            if (s.isNotEmpty())
                auth.sendPasswordResetEmail(s).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        this.toast(getString(R.string.EmailSent))
                        onBackPressed()
                    } else
                        this.toast(task.exception.toString())
                }
            else email.error = getString(R.string.LoginMailError)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) onBackPressed()
        return true
    }
}
