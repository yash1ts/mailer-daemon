package com.mailerdaemon.app.placement

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.mailerdaemon.app.ApplicationClass
import com.mailerdaemon.app.PlacementFragment
import com.mailerdaemon.app.R
import com.mailerdaemon.app.notices.PostModel
import com.mailerdaemon.app.toast
import kotlinx.android.synthetic.main.fragment_placement.*
import kotlinx.android.synthetic.main.shimmer_layout_posts.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlacementActivity : AppCompatActivity() {

    var data = emptyList<PostModel>()
    val fragment = PlacementFragment()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placement)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = resources.getString(R.string.title_activity_placement_daemon)
        }

        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
    }

    fun getData(showData: ShowData) {
        shimmer_view_container.let {
        it.startShimmer()
        it.visibility = View.VISIBLE
        }
        (application as ApplicationClass).repository.getPlacementPosts()
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
                        data = result
                        showData.showData(data)
                    } else
                        baseContext.toast("Error")
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.imp_contact_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                fragment.rv_placement.scrollToPosition(0)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "" || newText == null) {
                    fragment.showData(data as MutableList<PostModel>)
                } else {
                    val list = data.filter {
                        it.message.contains(newText, true)
                    }
                    fragment.showData(list as MutableList<PostModel>)
                }
                return true
            }
        })

        searchView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (hasFocus)
                imm.showSoftInput(v.findFocus(), InputMethodManager.RESULT_SHOWN)
        }

        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )
        searchView.setIconifiedByDefault(false)
        return true
    }

    companion object {
        interface ShowData {
            fun showData(list: List<PostModel>)
        }

        const val placementData = "placement"
    }
}
