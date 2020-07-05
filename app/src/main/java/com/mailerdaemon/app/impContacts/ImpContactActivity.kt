package com.mailerdaemon.app.impContacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mailerdaemon.app.R
import com.mailerdaemon.app.WARDEN_LINK
import com.mailerdaemon.app.toast
import com.mailerdaemon.app.utils.ChromeTab
import com.mailerdaemon.app.utils.ContactFunction
import kotlinx.android.synthetic.main.activity_imp_contacts.*

class ImpContactActivity : AppCompatActivity(), ContactFunction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imp_contacts)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = resources.getString(R.string.title_activity_imp_contacts)
        }
        bt_faculty.setOnClickListener { openDetail(resources.getStringArray(R.array.tabs),
                resources.getStringArray(R.array.pages)) }
        bt_admin.setOnClickListener {
            val tabs = arrayOf("Deans", "Associate Deans", "HOD", "HOC")
            val pages = arrayOf("deans", "associate_deans", "hod", "hoc")
            openDetail(tabs, pages)
        }
        bt_hostel.setOnClickListener {
            val tab = ChromeTab(this)
            tab.openTab(WARDEN_LINK)
        }
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
        if (num.trim() != "0") {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num.trim()))
            startActivity(intent)
        } else this.toast(resources.getString(R.string.number_not_available))
    }

    override fun sendMail(s: String) {
        if (s.trim().isNotEmpty()) {
            val send = Intent(Intent.ACTION_SENDTO)
            val uriText = "mailto:${Uri.encode(s.trim())}" +
                    "?subject=${Uri.encode("Subject")}" +
                    "&body=${Uri.encode("the body of the message")}"
            send.data = Uri.parse(uriText)
            startActivity(Intent.createChooser(send, "Send mail..."))
        } else this.toast(resources.getString(R.string.email_not_available))
    }
}
