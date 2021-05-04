package com.mailerdaemon.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mailerdaemon.app.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: GridLayoutManager
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val list: List<String> = listOf("event", "campus", "placement")
        linearLayoutManager = GridLayoutManager(this, 2)
        binding.apply {
            rvTag.layoutManager = linearLayoutManager
            rvTag.adapter = NotificationTagAdapter(list)
            contactEmail2.setOnClickListener {
                val send = Intent(Intent.ACTION_SENDTO)
                send.data = Uri.parse("mailto:" + "mailerdism@gmail.com" +
                    "?subject=" + Uri.encode("Subject") +
                    "&body=" + Uri.encode("the body of the message"))
                startActivity(Intent.createChooser(send, "Send mail..."))
            }
            contactFb.setOnClickListener {
                val uri = Uri.parse("http://facebook.com/MDiitism/")
                val likeIng = Intent(Intent.ACTION_VIEW, uri)
                likeIng.setPackage("com.facebook.katana")
                if (likeIng.resolveActivity(packageManager) != null) startActivity(likeIng)
                else startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
            contactInsta.setOnClickListener {
                val uri = Uri.parse("http://instagram.com/md_iit_dhanbad/")
                val likeIng = Intent(Intent.ACTION_VIEW, uri)
                likeIng.setPackage("com.instagram.android")
                if (likeIng.resolveActivity(packageManager) != null) startActivity(likeIng)
                else startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // Inflate the menu; this adds items to the action bar if it is present.
        if (BuildConfig.IS_ADMIN)
            menuInflater.inflate(R.menu.settings, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        if (item.itemId == R.id.admin) {
            startActivity(Intent(this, classLoader.loadClass("com.mailerdaemon.app.admin.AdminActivity")))
        }
        return true
    }
}
