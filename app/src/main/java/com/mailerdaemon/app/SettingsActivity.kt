package com.mailerdaemon.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        contact_email2.setOnClickListener {
            val send = Intent(Intent.ACTION_SENDTO)
            send.data = Uri.parse("mailto:" + "mailerdism@gmail.com" +
                "?subject=" + Uri.encode("Subject") +
                "&body=" + Uri.encode("the body of the message"))
            startActivity(Intent.createChooser(send, "Send mail..."))
        }
        contact_fb.setOnClickListener {
            val uri = Uri.parse("http://facebook.com/MDiitism/")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)
            likeIng.setPackage("com.facebook.katana")
            if (likeIng.resolveActivity(packageManager) != null) startActivity(likeIng)
            else startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
        contact_insta.setOnClickListener {
            val uri = Uri.parse("http://instagram.com/md_iit_dhanbad/")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)
            likeIng.setPackage("com.instagram.android")
            if (likeIng.resolveActivity(packageManager) != null) startActivity(likeIng)
            else startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }
}
