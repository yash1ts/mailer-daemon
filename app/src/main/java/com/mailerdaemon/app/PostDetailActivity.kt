package com.mailerdaemon.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mailerdaemon.app.notices.NoticeViewPagerAdapter
import com.mailerdaemon.app.notices.PostModel
import com.mailerdaemon.app.placement.PlacementModel
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.rv_notices.view.*

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        bt_back.setOnClickListener { onBackPressed() }
        val bundle = intent.extras
        if (intent.getStringExtra("type").equals("Placement")) {
            val postModel: PlacementModel = bundle?.getParcelable("post")!!
            post_message.text = postModel?.message
            post_tag.text = postModel?.message_tags[0].name
            view_post_fb_layout.visibility = View.GONE
            share_post.visibility = View.GONE
            post_image_layout.visibility = View.GONE
        } else {
            val postModel: PostModel = bundle?.getParcelable("post")!!
            post_message.text = postModel?.message
            post_tag.text = postModel?.message_tags[0]

            if (!postModel.permalink_url.isNullOrEmpty()) {
                fb_post_button.setOnClickListener {
                    val facebookIntent = Intent(Intent.ACTION_VIEW)
                    facebookIntent.data = Uri.parse(postModel.permalink_url)
                    startActivity(facebookIntent)
                }
                share_post.setOnClickListener {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, postModel.permalink_url)
                    startActivity(Intent.createChooser(shareIntent, "Share..."))
                }
            }

            if (postModel.full_picture.isNullOrBlank() || postModel.photo.isNotEmpty()) {
                post_full_image.visibility = View.GONE
            } else {
                post_full_image.visibility = View.VISIBLE
                post_full_image.setImageURI(postModel.full_picture)
            }
            if (postModel.photo.isNotEmpty()) {
                val adapter = NoticeViewPagerAdapter()
                post_viewpager.visibility = View.VISIBLE
                adapter.list = postModel.photo
                post_viewpager.adapter = adapter
            } else {
                post_viewpager.visibility = View.GONE
            }
        }
    }
}
