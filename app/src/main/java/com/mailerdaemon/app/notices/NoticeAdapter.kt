package com.mailerdaemon.app.notices

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.mailerdaemon.app.databinding.RvNoticesBinding
import kotlinx.android.synthetic.main.rv_notices.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class NoticeAdapter(
    var list: List<PostModel> = emptyList(),
    private val mListener: ((PostModel) -> Unit)? = null,
    val bind: ((RvNoticesBinding) -> Unit)? = null
) :
    RecyclerView.Adapter<NoticeAdapter.Holder>() {
    private val p = PrettyTime()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)

    class Holder(val item: RvNoticesBinding) : RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(RvNoticesBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val dateString = list[position].created_time
        val convertedDate = dateFormat.parse(dateString)
        val str = list[position]
        bind?.invoke(holder.item)
        holder.item.let {
            it.noticeDetail.text = list[position].message
            it.time.text = p.format(convertedDate)
            if (str.full_picture.isNullOrBlank() || str.photo?.isNotEmpty() == true) {
                it.fullImage.visibility = View.GONE
            } else {
                it.fullImage.visibility = View.VISIBLE
                it.fullImage.setImageURI(str.full_picture)
            }
            if (str.photo?.isNotEmpty() == true) {
                val adapter = MultiViewAdapter()
                it.multiImageList.visibility = View.VISIBLE
                adapter.list = str.photo
                it.multiImageList.adapter = adapter
            } else {
                it.multiImageList.visibility = View.GONE
            }

            it.ivFacebook.setOnClickListener {
                val facebookIntent = Intent(Intent.ACTION_VIEW)
                facebookIntent.data = Uri.parse(str.permalink_url)
                holder.itemView.context.startActivity(facebookIntent)
            }
            it.ivShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, str.permalink_url)
                holder.itemView.context.startActivity(Intent.createChooser(shareIntent, "Share..."))
            }
        }
        holder.item.noticeDetail.setOnClickListener {
            mListener?.invoke(list[position])
        }
        holder.itemView.setOnClickListener {
            mListener?.invoke(list[position])
        }
    }
}
