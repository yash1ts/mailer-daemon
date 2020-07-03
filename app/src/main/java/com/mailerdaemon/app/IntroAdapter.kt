package com.mailerdaemon.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter

class IntroAdapter : PagerAdapter() {
    private val xml = intArrayOf(R.layout.intro_1, R.layout.intro_2,
            R.layout.intro_3, R.layout.intro_4,
            R.layout.intro_5, R.layout.intro_6)

    override fun isViewFromObject(view: View, `object`: Any) = (view === `object`)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(xml[position], container, false)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) = container
            .removeView(`object` as LinearLayout)

    override fun getCount() = xml.size
}
