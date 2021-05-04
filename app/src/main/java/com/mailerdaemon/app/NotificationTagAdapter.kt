package com.mailerdaemon.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationTagAdapter(var list: List<String>) : RecyclerView.Adapter<NotificationTagAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val title = list[position]
        val context = holder.itemView.context
        holder.itemView.let {
            it.bt_notification_tag.text = title
            if (context.getSharedPreferences(MAIN, Context.MODE_PRIVATE).getBoolean(title, false)) {
                it.bt_notification_tag.setCompoundDrawablesWithIntrinsicBounds(
                    context.getDrawableCompat(R.drawable.ic_check_white_24), null, null, null
                )
                it.bt_notification_tag.setBackgroundColor(context.getColorCompat(R.color.colorPrimary))
            } else {
                it.bt_notification_tag.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, context.getDrawableCompat(R.drawable.ic_close_white_24dp), null
                )
                it.bt_notification_tag.setBackgroundColor(context.getColorCompat(R.color.colorPlacementText))
            }
            it.bt_notification_tag.setOnClickListener {
                if (context.getSharedPreferences(MAIN, Context.MODE_PRIVATE).getBoolean(title, false)) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(title).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            context.getSharedPreferences(MAIN, Context.MODE_PRIVATE)
                                .edit().putBoolean(title, false).apply()
                            context.toast("Unsubscribed Successfully")
                        } else context.toast("Error")
                    }
                    it.bt_notification_tag.setBackgroundColor(context.getColorCompat(R.color.colorPlacementText))
                    it.bt_notification_tag.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, context.getDrawableCompat(R.drawable.ic_close_white_24dp), null
                    )
                } else {
                    FirebaseMessaging.getInstance().subscribeToTopic(title).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            context.getSharedPreferences(MAIN, Context.MODE_PRIVATE)
                                .edit().putBoolean(title, true).apply()
                            context.toast("Subscribed Successfully")
                        } else context.toast("Error")
                    }
                    it.bt_notification_tag.setCompoundDrawablesWithIntrinsicBounds(context.getDrawableCompat(
                        R.drawable.ic_check_white_24), null, null, null
                    )
                    it.bt_notification_tag.setBackgroundColor(context.getColorCompat(R.color.colorPrimary))
                }
            }
        }
    }
}
