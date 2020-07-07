package com.mailerdaemon.app.impContacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.mailerdaemon.app.R
import kotlinx.android.synthetic.main.fragment_contact_viewpager.view.*

class ContactFragmentViewPager : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_viewpager, container, false)
        view.tab_layout.let {
            it.setupWithViewPager(view.contact_viewpager)
            it.tabMode = TabLayout.MODE_SCROLLABLE
        }
        val tabs = arguments?.getStringArray("tabs")
        view.contact_viewpager.adapter = object: FragmentStatePagerAdapter(childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            var s = arguments?.getStringArray("pages")

            override fun getItem(position: Int) = getFragment(s!![position])

            override fun getCount() = s!!.size

            override fun getPageTitle(position: Int) = tabs!![position]
        }
        return view
    }
    private fun getFragment(s: String): Fragment {
        val fragment = ContactDetailFragment()
        val bundle = Bundle()
        bundle.putString("type", s)
        fragment.arguments = bundle
        return fragment
    }
}
