package com.mailerdaemon.app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mailerdaemon.app.databinding.RvNoticesBinding
import com.mailerdaemon.app.notices.NoticeAdapter
import com.mailerdaemon.app.notices.NoticesActivity
import com.mailerdaemon.app.notices.PostModel
import kotlinx.android.synthetic.main.fragment_placement.*
import kotlinx.android.synthetic.main.fragment_placement.refresh
import kotlinx.android.synthetic.main.fragment_placement.view.refresh

class NoticesFragment : Fragment(), NoticesActivity.Companion.ShowNotices {
    var data = emptyList<PostModel>()
    private lateinit var adapter: NoticeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_placement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listener: (PostModel) -> Unit = { it ->
            val intent = Intent(activity, PostDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("post", it)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        val bind: (RvNoticesBinding) -> Unit = {
            it.noticeDetail.maxLines = 10
        }
        activity?.let {
            adapter = NoticeAdapter(data, listener, bind)
        }
        (activity as NoticesActivity).getNotices(this)
        view.refresh.setOnRefreshListener {
            refresh.visibility = View.GONE
            (activity as NoticesActivity).getNotices(this)
        }
    }

    override fun showNotices(list: List<PostModel>) {
        data = list
        adapter.list = data
        adapter.notifyDataSetChanged()
        rv_placement.adapter = adapter
        refresh.isRefreshing = false
    }
}
