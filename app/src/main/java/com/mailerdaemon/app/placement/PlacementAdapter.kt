package com.mailerdaemon.app.placement

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mailerdaemon.app.R
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.item_posts.view.*
import org.ocpsoft.prettytime.PrettyTime

object PlacementAdapter : RecyclerView.Adapter<PlacementAdapter.Holder>() {
    var list = emptyList<PlacementModel>()

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_posts, parent, false))

    override fun getItemCount() = list.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val p = PrettyTime()
        val datestring = list[position].created_time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        val convertedDate = dateFormat.parse(datestring)

        holder.itemView.let {
            it.post_message.text = list[position].message.substringBefore("\nCongrat")
            it.time_created.text = p.format(convertedDate)
            it.daemon.text = "Placement Daemon"
        }
    }
}
