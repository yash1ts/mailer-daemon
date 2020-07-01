package com.mailerdaemon.app.impContacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

import com.mailerdaemon.app.R
import com.mailerdaemon.app.utils.ChromeTab
import com.mailerdaemon.app.utils.ContactFunction
import com.mailerdaemon.app.WARDEN_LINK

import kotlinx.android.synthetic.main.activity_imp_contacts.*

class ImpContactActivity : AppCompatActivity(), ContactFunction {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imp_contacts)
        setSupportActionBar(toolbar)
        (supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Contacts"
        bt_faculty.setOnClickListener {
        val tabs = arrayOf("All", "ACH", "AGL", "AGP", "MnC",
        "AP", "CHE", "CIV", "CSE", "ECE",
        "EE", "ESE", "FME", "HSS", "ME",
        "MECH", "MME", "MS", "PE")
        val pages = arrayOf("faculty_all", "ACH", "AGL", "AGP", "AM",
        "AP", "CHE", "CIV", "CSE", "ECE",
        "EE", "ESE", "FME", "HSS", "ME",
        "MECH", "MME", "MS", "PE")
        openDetail(tabs, pages) }
        bt_admin.setOnClickListener {
        val tabs = arrayOf("Deans", "Associate Deans", "HOD", "HOC")
        val pages = arrayOf("deans", "associate_deans", "hod", "hoc")
        openDetail(tabs, pages) }
        bt_hostel.setOnClickListener {
        val tab = ChromeTab(this)
        tab.openTab(WARDEN_LINK) }
        }

private fun openDetail(tabs: Array<String>, pages: Array<String>) {
        val fragment = ContactFragmentViewPager()
        val bundle = Bundle()
        bundle.putStringArray("tabs", tabs)
        bundle.putStringArray("pages", pages)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment, null).commit()
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) onBackPressed()
        return true
        }

        override fun onBackPressed() {
        if (supportFragmentManager.fragments.isEmpty()) super.onBackPressed()
        else supportFragmentManager.beginTransaction().remove(supportFragmentManager.fragments[0]).commit()
        }

        fun openWebView(url: String) {
        val fragment = HtmlFragment()
        val bundle = Bundle()
        bundle.putString("url", url)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment, null)
        .addToBackStack(null).commit()
        }

        override fun makeCall(num: String) {
        if (num.trim { it <= ' ' } != "0") {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num.trim { it <= ' ' }))
        startActivity(intent)
        } else Toast.makeText(this, "Sorry number not available", Toast.LENGTH_LONG).show()
        }

        override fun sendMail(s: String) {
        if (s.trim { it <= ' ' }.isNotEmpty()) {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = ("mailto:" + Uri.encode(s.trim { it <= ' ' }) +
        "?subject=" + Uri.encode("Subject") +
        "&body=" + Uri.encode("the body of the message"))
        val uri = Uri.parse(uriText)
        send.data = uri
        startActivity(Intent.createChooser(send, "Send mail..."))
        } else Toast.makeText(this, "Sorry email not available", Toast.LENGTH_LONG).show()
        }
}
