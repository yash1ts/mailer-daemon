package com.mailerdaemon.app.notices

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mailerdaemon.app.ApplicationClass
import com.mailerdaemon.app.R
import com.mailerdaemon.app.toast
import kotlinx.android.synthetic.main.activity_notices.*
import kotlinx.android.synthetic.main.shimmer_layout_posts.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticesActivity : AppCompatActivity() {
    private var access: Boolean = false
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notices)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = "Notices"
        }

        access = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getBoolean("Access", false)
        rv_notices.adapter = NoticeAdapter

        if (NoticeAdapter.list.isEmpty())
            getDatabase()
        refresh.setOnRefreshListener {
            getDatabase()
            refresh.isRefreshing = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("data", PostsList(NoticeAdapter.list))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        NoticeAdapter.list =
            savedInstanceState?.getParcelable<PostsList>("data")?.posts ?: emptyList()
    }

    fun getDatabase() {
        shimmer_view_container.visibility = View.VISIBLE
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
                        shimmer_view_container.visibility = View.GONE
                        NoticeAdapter.setData(result)
                        NoticeAdapter.notifyDataSetChanged()
                    } else
                        baseContext.toast("Error")
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }
}
