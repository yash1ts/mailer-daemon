package com.mailerdaemon.app.clubs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_BUTTON_PRESS
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mailerdaemon.app.BuildConfig
import com.mailerdaemon.app.R
import com.mailerdaemon.app.club_id
import com.mailerdaemon.app.utils.ChromeTab
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_des
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_fb
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_icon
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_insta
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_members
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_name
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_web
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_youtube
import kotlinx.android.synthetic.main.fragment_club_detail.view.web
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets

@Suppress("DEPRECATION")
class ClubDetailBottomSheet : BottomSheetDialogFragment() {

    private var selectedclub = 0
    private var selectedtag = 0
    private lateinit var chromeTab: ChromeTab
    private lateinit var ob: JSONObject

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_club_detail, container, false)
        chromeTab = ChromeTab(context)
        view.web.isHorizontalScrollBarEnabled = true
        view.club_icon.hierarchy.setProgressBarImage(CircularProgressDrawable((context)!!))
        if (BuildConfig.DEBUG && arguments == null) {
            error("Assertion failed")
        }
        selectedclub = requireArguments().getInt(club_id)
        getJson()
        setView(view)
        return view
    }

    private fun setView(view: View) {
        view.club_des.text = ob.getString("description")
        view.club_name.text = ob.getString("name")
        view.club_icon.setImageResource(selectedtag)
        view.club_members.text = ob.getString("members")
        view.club_fb.setOnClickListener { chromeTab.openTab(ob.getString("fb")) }
        if (ob.getString("insta").isNotEmpty()) {
            view.club_insta.visibility = View.VISIBLE
            view.club_insta.setOnClickListener { chromeTab.openTab(ob.getString("insta")) }
        }
        if (ob.getString("youtube").isNotEmpty()) {
            view.club_youtube.visibility = View.VISIBLE
            view.club_youtube.setOnClickListener { chromeTab.openTab(ob.getString("youtube")) }
        }
        if (ob.getString("web").isNotEmpty()) {
            view.club_web.visibility = View.VISIBLE
            view.club_web.setOnClickListener { chromeTab.openTab(ob.getString("web")) }
        }
        setposts(page = ob.getString("fb"), view = view)
    }

    private fun getJson() {
        val json: String
        try {
            val inputStream = (context)!!.assets.open("clubs.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
            val jsonObject = JSONObject(json)
            ob = jsonObject.getJSONArray("modelList").getJSONObject(selectedclub - 1)
            val logo = resources.obtainTypedArray(R.array.clubs_logo)
            selectedtag = logo.getResourceId(ob.getInt("tag"), 0)
            logo.recycle()
        } catch (jsonException: JSONException) {
            jsonException.printStackTrace()
        } catch (jsonException: IOException) {
            jsonException.printStackTrace()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setposts(page: String?, view: View) {
        if (page!!.isEmpty()) return
        var site = page
        if (page.length > 25) site = site.substring(25)
        val s = "<iframe src=\"https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2F" +
            "$site&tabs=timeline&width=350&height=700&small_header=true&adapt_container_width=true&hide_cover=true&" +
            "show_facepile=false&appId=384900825472866\" width=\"100%\" height=\"100%\" style=\"border:none;" +
            "overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\"" +
            " allow=\"encrypted-media\"></iframe>"
        view.web.loadHtml(s)
        view.web.setOnTouchListener { _: View?, event: MotionEvent ->
                event.action == ACTION_BUTTON_PRESS }
    }
}
