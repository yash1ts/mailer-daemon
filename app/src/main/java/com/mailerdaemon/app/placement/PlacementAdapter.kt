package com.mailerdaemon.app.placement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mailerdaemon.app.R
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.item_posts.view.*
import org.ocpsoft.prettytime.PrettyTime

class PlacementAdapter(val list: List<PlacementModel>) : RecyclerView.Adapter<PlacementAdapter.Holder>() {
    val p = PrettyTime()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_posts, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val datestring = list[position].created_time
        val convertedDate = dateFormat.parse(datestring)

        holder.itemView.let {
            it.post_message.text = list[position].message.substringBefore("\nCongrat")
            it.time_created.text = p.format(convertedDate)
            it.daemon.text = "Placement Daemon"
            it.congrats.visibility = View.VISIBLE
        }
    }
}
