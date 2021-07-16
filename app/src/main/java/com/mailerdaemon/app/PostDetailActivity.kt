package com.mailerdaemon.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mailerdaemon.app.databinding.ActivityPostDetailBinding
import com.mailerdaemon.app.notices.PostModel
import com.mailerdaemon.app.notices.SingleViewAdapter

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.let {
            it.btBack.setOnClickListener { onBackPressed() }
            val postModel: PostModel = intent.extras?.getParcelable("post") ?: return
            it.postMessage.text = postModel.message
                val tag = postModel.message_tags?.get(0)
                if (tag != null) {
                    it.postTag.text = tag
                }

                if (postModel.permalink_url.isNotEmpty()) {
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

                if (postModel.full_picture.isNullOrBlank() || postModel.photo?.isNotEmpty() == true) {
                    it.postFullImage.visibility = View.GONE
                } else {
                    it.postFullImage.visibility = View.VISIBLE
                    it.postFullImage.setImageURI(postModel.full_picture)
                }
                if (postModel.photo?.isNotEmpty() == true) {
                    val adapter = SingleViewAdapter()
                    it.postViewpager.visibility = View.VISIBLE
                    adapter.list = postModel.photo
                    it.postViewpager.adapter = adapter
                } else {
                    it.postViewpager.visibility = View.GONE
                }
        }
    }
}
