package com.mailerdaemon.app.impContacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.mailerdaemon.app.R
import com.mailerdaemon.app.utils.ContactFunction
import kotlinx.android.synthetic.main.item_contacts_home.view.contact_call
import kotlinx.android.synthetic.main.item_contacts_home.view.contact_dept
import kotlinx.android.synthetic.main.item_contacts_home.view.contact_email
import kotlinx.android.synthetic.main.item_contacts_home.view.contact_img
import kotlinx.android.synthetic.main.item_contacts_home.view.contact_name
import kotlinx.android.synthetic.main.item_contacts_home.view.contact_phone
import kotlinx.android.synthetic.main.item_contacts_home.view.contact_send_mail
import java.util.*

class ContactRecyclerAdapter(private var `fun`: ContactFunction) : RecyclerView.Adapter<ContactRecyclerAdapter.Holder>() {
    var data:List<Contact> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.rv_contact_detail, viewGroup, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, i: Int) {
        val c = data[i]
        holder.itemView.let{
            it.contact_name.text = c.name
            it.contact_img.setImageURI(c.image)
            it.contact_phone.text = c.phone
            it.contact_dept.text = c.dept
            it.contact_email.text = c.email
            it.contact_call.setOnClickListener { `fun`.makeCall(c.phone) }
            it.contact_send_mail.setOnClickListener {  `fun`.sendMail(c.email) }
        }
    }

    override fun getItemCount() = data.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.contact_img.hierarchy.setProgressBarImage(CircularProgressDrawable(itemView.contact_name.context))
        }
    }
}
