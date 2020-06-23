package com.mailerdaemon.app

import android.animation.ArgbEvaluator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    private lateinit var indicators: Array<ImageView>
    public override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        // View view=getWindow().getDecorView();
        indicators = arrayOf(intro_indicator_1, intro_indicator_2, intro_indicator_3, intro_indicator_4, intro_indicator_5, intro_indicator_6)
        val color1 = ContextCompat.getColor(this, R.color.intro_1)
        val color2 = ContextCompat.getColor(this, R.color.intro_2)
        val color3 = ContextCompat.getColor(this, R.color.intro_3)
        val evaluator = ArgbEvaluator()
        val colorList = intArrayOf(color1, color2, color3, color1, color2, color3)
        view_pager.adapter = IntroAdapter()
        updateIndicators(0)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val colorUpdate = evaluator.evaluate(positionOffset, colorList[position], colorList[if (position == colorList.size - 1) position else position + 1]) as Int
                view_pager.setBackgroundColor(colorUpdate)
            }
            override fun onPageSelected(position: Int) {
                updateIndicators(position)
                when (position) {
                    0 -> view_pager.setBackgroundColor(color1)
                    1 -> view_pager.setBackgroundColor(color2)
                    2 -> view_pager.setBackgroundColor(color3)
                    3 -> view_pager.setBackgroundColor(color1)
                    4 -> view_pager.setBackgroundColor(color2)
                    5 -> view_pager.setBackgroundColor(color3)
                }
                intro_btn_next.visibility = if (position == colorList.size - 1) View.GONE else View.VISIBLE
                intro_btn_finish.visibility = if (position == colorList.size - 1) View.VISIBLE else View.GONE
            }
            override fun onPageScrollStateChanged(state: Int) {
                return
            }
        })
        intro_btn_next.setOnClickListener { view_pager.currentItem = view_pager.currentItem + 1 }
        intro_btn_skip.setOnClickListener {
            getSharedPreferences("MAIN", MODE_PRIVATE).edit().putBoolean("intro", false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish() }
        intro_btn_finish.setOnClickListener {
            getSharedPreferences("MAIN", MODE_PRIVATE).edit().putBoolean("intro", false).apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish() }
    }
    internal fun updateIndicators(position: Int) {
        for (i in indicators.indices) {
            indicators[i].setBackgroundResource(
                    if (i == position) R.drawable.indicator_selected else R.drawable.indicator_unselected
            )
        }
    }
}
