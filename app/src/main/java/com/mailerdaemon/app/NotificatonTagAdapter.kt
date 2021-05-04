package com.mailerdaemon.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationTagAdapter(var list: List<String>, var context: Context) : RecyclerView.Adapter<NotificationTagAdapter.Holder>() {
    lateinit var title: String

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        title = list[position]
        holder.itemView.let {
            it.bt_notification_tag.text = title
            if (context.getSharedPreferences(MAIN, Context.MODE_PRIVATE).getBoolean(list[position], false)) {
                it.bt_notification_tag.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.ic_check_white_24), null, null, null)
                it.bt_notification_tag.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
            } else {
                it.bt_notification_tag.setCompoundDrawablesWithIntrinsicBounds(null , null, context.resources.getDrawable(R.drawable.ic_close_white_24dp), null)
                it.bt_notification_tag.setBackgroundColor(context.resources.getColor(R.color.colorPlacementText))
            }
            it.bt_notification_tag.setOnClickListener {
                if (context.getSharedPreferences(MAIN, Context.MODE_PRIVATE).getBoolean(list[position], false)) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(title).addOnCompleteListener {
                        context.getSharedPreferences(MAIN, Context.MODE_PRIVATE).edit().putBoolean(list[position], false).apply()
                        context.toast("Unsubscribed Successfully")
                    }
                    it.bt_notification_tag.setBackgroundColor(context.resources.getColor(R.color.colorPlacementText))
                    it.bt_notification_tag.setCompoundDrawablesWithIntrinsicBounds(null , null, context.resources.getDrawable(R.drawable.ic_close_white_24dp), null)

                } else {
                    FirebaseMessaging.getInstance().subscribeToTopic(title).addOnCompleteListener {
                        context.getSharedPreferences(MAIN, Context.MODE_PRIVATE).edit().putBoolean(list[position], true).apply()
                        context.toast("Subscribed Successfully")
                    }
                    it.bt_notification_tag.setCompoundDrawablesWithIntrinsicBounds(context.resources.getDrawable(R.drawable.ic_check_white_24), null, null, null)
                    it.bt_notification_tag.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                }
            }
        }
    }
}

