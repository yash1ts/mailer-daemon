package com.mailerdaemon.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.mailerdaemon.app.R.layout
import com.mailerdaemon.app.databinding.RvNoticesBinding
import com.mailerdaemon.app.notices.NoticeAdapter
import com.mailerdaemon.app.notices.PostModel
import com.mailerdaemon.app.placement.DiffUtilCallback
import com.mailerdaemon.app.placement.PlacementActivity
import kotlinx.android.synthetic.main.fragment_placement.*
import kotlinx.android.synthetic.main.fragment_placement.view.*

class PlacementFragment : Fragment(), PlacementActivity.Companion.ShowData {
    var data = emptyList<PostModel>()
    private lateinit var adapter: NoticeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout.fragment_placement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bind: (RvNoticesBinding) -> Unit = {
            it.noticeDetail.maxLines = 30
        }
        adapter = NoticeAdapter(data, null, bind)
        (activity as PlacementActivity).getData(this)
        view.refresh.setOnRefreshListener {
                refresh.visibility = View.GONE
                    (activity as PlacementActivity).getData(this)
        }
    }

    override fun showData(list: List<PostModel>) {
        val n = DiffUtilCallback(data, list)
        val diffResult = DiffUtil.calculateDiff(n)
        data = list
        Log.d("update_list", "okok")
        adapter.list = data
        adapter.notifyDataSetChanged()
        diffResult.dispatchUpdatesTo(adapter)
        rv_placement.adapter = adapter
        refresh.isRefreshing = false
    }
}
