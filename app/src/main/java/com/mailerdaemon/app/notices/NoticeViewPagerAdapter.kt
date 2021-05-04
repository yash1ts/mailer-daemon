package com.mailerdaemon.app.notices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mailerdaemon.app.R
import com.mailerdaemon.app.notices.NoticeAdapter
import com.mailerdaemon.app.notices.PostModel
import kotlinx.android.synthetic.main.notice_viewpager_item.view.*

public class NoticeViewPagerAdapter (var list: List<Photo> = emptyList()) : RecyclerView.Adapter<NoticeViewPagerAdapter.Holder>()  {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       return Holder(LayoutInflater.from(parent.context).inflate(R.layout.notice_viewpager_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.notice_photo.setImageURI(list[position].media.image.src)
    }
}
