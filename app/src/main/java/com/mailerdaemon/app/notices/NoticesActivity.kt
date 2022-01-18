package com.mailerdaemon.app.notices

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mailerdaemon.app.*
import kotlinx.android.synthetic.main.activity_notices.refresh
import kotlinx.android.synthetic.main.shimmer_layout_posts.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticesActivity : AppCompatActivity() {

    var data = emptyList<PostModel>()
    private val fragmentNotices = NoticesFragment()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placement)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = resources.getString(R.string.title_activity_notices)
        }
        supportFragmentManager.beginTransaction().add(R.id.container, fragmentNotices).commit()
    }

    fun getNotices(showNotices: ShowNotices) {
        shimmer_view_container.let {
            it.startShimmer()
            it.visibility = View.VISIBLE
        }

        (application as ApplicationClass).repository.getPosts()
            ?.enqueue(object : Callback<List<PostModel>?> {
                override fun onFailure(call: Call<List<PostModel>?>, t: Throwable) {
                    baseContext.toast("Error")
                }

                override fun onResponse(
                    call: Call<List<PostModel>?>,
                    response: Response<List<PostModel>?>
                ) {
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        if (shimmer_view_container.isShimmerVisible) {
                            shimmer_view_container.let {
                                it.stopShimmer()
                                it.visibility = View.GONE
                            }
                        }
                        refresh.visibility = View.VISIBLE
                        var list = mutableListOf<PostModel>()
                        for (element in result) {
                            if (!isSomethingNull(element)) {
                                list.add(element)
                            }
                        }
                        data = list
                        showNotices.showNotices(data)
                    } else
                        baseContext.toast("Error")
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }

    companion object {
        interface ShowNotices {
            fun showNotices(list: List<PostModel>)
        }

        const val noticeData = "notices"
    }

    fun isSomethingNull(element: PostModel): Boolean {
        return element._id == null || element.created_time == null
                || element.id == null || element.message == null || element.permalink_url == null
    }
}
