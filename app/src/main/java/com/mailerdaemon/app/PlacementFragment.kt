package com.mailerdaemon.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mailerdaemon.app.placement.PlacementActivity
import com.mailerdaemon.app.placement.PlacementAdapter
import com.mailerdaemon.app.placement.PlacementModel
import kotlinx.android.synthetic.main.activity_placement.*
import kotlinx.android.synthetic.main.fragment_placement.*
import kotlinx.android.synthetic.main.fragment_placement.view.*

class PlacementFragment : Fragment() {

    var data: List<PlacementModel>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_placement, container, false)

        data = arguments?.getParcelableArrayList("data")
        view.rv_placement?.adapter = PlacementAdapter
        view.refresh.setOnRefreshListener {
            (activity as PlacementActivity).getDatabase()
            refresh.isRefreshing = false
        }
        return view
    }
}
