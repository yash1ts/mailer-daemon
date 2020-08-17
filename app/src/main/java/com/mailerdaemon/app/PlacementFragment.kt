package com.mailerdaemon.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.mailerdaemon.app.R.layout
import com.mailerdaemon.app.placement.DiffUtilCallback
import com.mailerdaemon.app.placement.PlacementActivity
import com.mailerdaemon.app.placement.PlacementAdapter
import com.mailerdaemon.app.placement.PlacementModel
import kotlinx.android.synthetic.main.activity_placement.*
import kotlinx.android.synthetic.main.fragment_placement.*
import kotlinx.android.synthetic.main.fragment_placement.view.*

class PlacementFragment : Fragment(), ShowData {
    var data = emptyList<PlacementModel>()
    private lateinit var adapter: PlacementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout.fragment_placement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as PlacementActivity).getData(this)
           view.refresh.setOnRefreshListener {
            (activity as PlacementActivity).getData(this)
        }
    }

    override fun showData(list: MutableList<PlacementModel>?) {
        val n = DiffUtilCallback(data, list as List<PlacementModel>)
        val diffResult = DiffUtil.calculateDiff(n)
        data = list
        adapter = PlacementAdapter(list as List<PlacementModel>)
        adapter.notifyDataSetChanged()
        diffResult.dispatchUpdatesTo(adapter)
        rv_placement.adapter = adapter
        refresh.isRefreshing = false
    }
}
