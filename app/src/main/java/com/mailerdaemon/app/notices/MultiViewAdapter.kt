package com.mailerdaemon.app.notices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mailerdaemon.app.R
import kotlinx.android.synthetic.main.notice_viewpager_item.view.*

class MultiViewAdapter(var list: List<Photo> = emptyList()) :
    RecyclerView.Adapter<MultiViewAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_multiview_pager, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.notice_photo.setImageURI(list[position].media.image.src)
    }
}
