package com.mailerdaemon.app.notices

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.mailerdaemon.app.notices.NoticeViewPagerAdapter
//import com.mailerdaemon.app.Notices.NoticeViewPagerAdapter

import com.mailerdaemon.app.R
import com.mailerdaemon.app.placement.PlacementModel
import kotlinx.android.synthetic.main.item_posts.view.*
import kotlinx.android.synthetic.main.rv_notices.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class NoticeAdapter(var list: List<PostModel>) : RecyclerView.Adapter<NoticeAdapter.Holder>() {
    val p = PrettyTime()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.rv_notices, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val datestring = list[position].created_time
        val convertedDate = dateFormat.parse(datestring)
        val str = list[position]


        holder.itemView.let {

            it.notice_detail.text = list[position].message
            it.time.text = p.format(convertedDate)
            if(str.attachments!=null && str.attachments.data.size!=0)
            {
                val adapter = NoticeViewPagerAdapter()
                Log.d("error1",str.attachments.data.toString())
                //adapter.list = str.attachments.data[0].subAttachments.data
                it.notice_viewpager.adapter = adapter
            }

        }

    }

    /*fun setData(data: List<PostModel>) {
        list = data
    }*/
}
