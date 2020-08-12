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
import androidx.recyclerview.widget.DiffUtil
import com.mailerdaemon.app.ApplicationClass
import com.mailerdaemon.app.PlacementFragment
import com.mailerdaemon.app.R
import com.mailerdaemon.app.ShowData
import com.mailerdaemon.app.toast
import com.mailerdaemon.app.utils.AccessDatabase
import kotlinx.android.synthetic.main.activity_placement.*
import kotlinx.android.synthetic.main.fragment_placement.*
import kotlinx.android.synthetic.main.item_posts.*
import kotlinx.android.synthetic.main.shimmer_layout_posts.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlacementActivity : AppCompatActivity(), AccessDatabase {

    var data = emptyList<PlacementModel>()
    val fragment = PlacementFragment()
    private lateinit var adapter: PlacementAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placement)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = resources.getString(R.string.title_activity_placement_daemon)
        }

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

   override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("data", PlacementList(data))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        data =
            savedInstanceState.getParcelable<PlacementList>("data")?.list ?: emptyList()
    }

    override fun getDatabase() { }

    override fun getData(showData: ShowData) {
        shimmer_view_container.visibility = View.VISIBLE
        (application as ApplicationClass).repository.getPlacementPosts()
            ?.enqueue(object : Callback<List<PlacementModel>?> {
                override fun onFailure(call: Call<List<PlacementModel>?>, t: Throwable) {
                    baseContext.toast("Error")
                }

                override fun onResponse(
                    call: Call<List<PlacementModel>?>,
                    response: Response<List<PlacementModel>?>
                ) {
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        shimmer_view_container.visibility = View.GONE
                        data = result
                        adapter = PlacementAdapter(data)
                        adapter.notifyDataSetChanged()
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
                if (newText == "") {
                    updateRV(data ?: emptyList())
                } else {
                    val list = data.filter {
                        it.message.contains(requireNotNull(newText), true)
                    } ?: emptyList()
                    updateRV(list)
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

    private fun updateRV(result: List<PlacementModel>) {
        val n = DiffUtilCallback(data, result)
        val diffResult = DiffUtil.calculateDiff(n)
        fragment.showData(result as MutableList<PlacementModel>)
        adapter = PlacementAdapter(data)
        diffResult.dispatchUpdatesTo(adapter)
    }
}
