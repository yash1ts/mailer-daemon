package com.mailerdaemon.app.placement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mailerdaemon.app.R
import kotlinx.android.synthetic.main.item_posts.view.*

object PlacementAdapter : RecyclerView.Adapter<PlacementAdapter.Holder>() {
    var list = emptyList<PlacementModel>()

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_posts, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.let {
            it.post_message.text = list[position].message.substringBefore("\nCongrat")
        }
    }
}
