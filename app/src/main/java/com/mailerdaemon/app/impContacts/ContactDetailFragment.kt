package com.mailerdaemon.app.impContacts

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import com.mailerdaemon.app.R
import com.mailerdaemon.app.toast
import com.mailerdaemon.app.utils.ContactFunction
import kotlinx.android.synthetic.main.fragment_contact_detail.*
import kotlinx.android.synthetic.main.fragment_contact_detail.view.*
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.ArrayList

class ContactDetailFragment : Fragment(), ContactFunction {
    private var contactList = ArrayList<Contact>()
    private val adapter: ContactRecyclerAdapter = ContactRecyclerAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contactList = (Gson().fromJson(loadJSONFromAsset(arguments
                ?.getString("type")), FacultyModel::class.java)
                .contact as? ArrayList<Contact> ?: ArrayList())
        val view = inflater.inflate(R.layout.fragment_contact_detail, container, false)
        view.rv_contact_detail.adapter = adapter
        adapter.data = contactList
        adapter.notifyDataSetChanged()
        setHasOptionsMenu(true)
        return view
    }

    private fun updateRV(result: List<Contact>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallBack(adapter.data, result))
        adapter.data = result
        diffResult.dispatchUpdatesTo(adapter)
    }

    private fun loadJSONFromAsset(type: String?): String? {
        val json: String
        try {
            val inputStream = (activity)?.assets?.open("$type.json")
            val size = inputStream?.available()
            val buffer = size?.let { ByteArray(it) }
            inputStream?.let {
                it.read(buffer)
                it.close()
            }
            json = buffer?.let { String(it, StandardCharsets.UTF_8) }.toString()
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    @SuppressLint("CheckResult")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.imp_contact_search, menu)
        val searchManager = (activity)?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.let {
            it.queryHint = "Search Contacts"
            it.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    rv_contact_detail.scrollToPosition(0)
                    searchView.clearFocus()
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText == "") {
                        updateRV(contactList)
                    } else {
                        val list = ArrayList<Contact>()
                        for (c in contactList) {
                            if (c.name?.contains(newText.toLowerCase()) == true)
                                list.add(c)
                        }
                        updateRV(list)
                    }
                    return true
                }
            })
            it.setOnFocusChangeListener { v, hasFocus ->
                val manager = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (hasFocus) manager.showSoftInput(v.findFocus(), InputMethodManager.RESULT_SHOWN) }
            it.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            it.setIconifiedByDefault(false)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun makeCall(num: String) {
        if (num.trim() != "0") {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num.trim()))
            startActivity(intent)
        } else context?.toast(resources.getString(R.string.number_not_available))
    }

    override fun sendMail(s: String) {
        if (s.trim().isNotEmpty()) {
            val send = Intent(Intent.ACTION_SENDTO)
            val uriText = "mailto:${Uri.encode(s.trim())}" +
                    "?subject=${Uri.encode("Subject")}" +
                    "&body=${Uri.encode("the body of the message")}"
            send.data = Uri.parse(uriText)
            startActivity(Intent.createChooser(send, "Send mail..."))
        } else context?.toast(resources.getString(R.string.email_not_available))
    }
}
