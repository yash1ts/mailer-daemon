package com.mailerdaemon.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.IOException

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        contact_email2.setOnClickListener {
            val send = Intent(Intent.ACTION_SENDTO)
            val uriText = "mailto:" + "mailerdism@gmail.com" +
                "?subject=" + Uri.encode("Subject") +
                "&body=" + Uri.encode("the body of the message")
            val uri = Uri.parse(uriText)
            send.data = uri
            startActivity(Intent.createChooser(send, "Send mail..."))
        }
        contact_fb.setOnClickListener {
            val uri = Uri.parse("http://facebook.com/MDiitism/")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)
            likeIng.setPackage("com.facebook.katana")
            try {
                startActivity(likeIng)
            } catch (e: IOException) {
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
        contact_insta.setOnClickListener {
            val uri = Uri.parse("http://instagram.com/md_iit_dhanbad/")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)
            likeIng.setPackage("com.instagram.android")
            try {
                startActivity(likeIng)
            } catch (e: IOException) {
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
        if (frag_container != null) {
            if (savedInstanceState != null) {
                return
            }
            supportFragmentManager.beginTransaction().add(R.id.frag_container, SettingsFragment()).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }
}
