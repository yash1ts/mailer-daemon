package com.mailerdaemon.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mailerdaemon.app.databinding.ActivityPostDetailBinding
import com.mailerdaemon.app.notices.NoticeViewPagerAdapter
import com.mailerdaemon.app.notices.PostModel
import com.mailerdaemon.app.placement.PlacementModel

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.let {
            it.btBack.setOnClickListener { onBackPressed() }
            val bundle = intent.extras
            if (intent.getStringExtra("type").equals("Placement")) {
                val postModel: PlacementModel = bundle?.getParcelable("post")!!
                it.postMessage.text = postModel?.message
                it.postTag.text = postModel?.message_tags[0].name
                it.viewPostFbLayout.visibility = View.GONE
                it.sharePost.visibility = View.GONE
                it.postImageLayout.visibility = View.GONE
            } else {
                val postModel: PostModel = bundle?.getParcelable("post")!!
                it.postMessage.text = postModel?.message
                it.postTag.text = postModel?.message_tags[0]

                if (!postModel.permalink_url.isNullOrEmpty()) {
                    it.fbPostButton.setOnClickListener {
                        val facebookIntent = Intent(Intent.ACTION_VIEW)
                        facebookIntent.data = Uri.parse(postModel.permalink_url)
                        startActivity(facebookIntent)
                    }
                    it.sharePost.setOnClickListener {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_TEXT, postModel.permalink_url)
                        startActivity(Intent.createChooser(shareIntent, "Share..."))
                    }
                }

                if (postModel.full_picture.isNullOrBlank() || postModel.photo.isNotEmpty()) {
                    it.postFullImage.visibility = View.GONE
                } else {
                    it.postFullImage.visibility = View.VISIBLE
                    it.postFullImage.setImageURI(postModel.full_picture)
                }
                if (postModel.photo.isNotEmpty()) {
                    val adapter = NoticeViewPagerAdapter()
                    it.postViewpager.visibility = View.VISIBLE
                    adapter.list = postModel.photo
                    it.postViewpager.adapter = adapter
                } else {
                    it.postViewpager.visibility = View.GONE
                }
            }
        }
    }
}
