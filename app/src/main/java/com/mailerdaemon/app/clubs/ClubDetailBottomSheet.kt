package com.mailerdaemon.app.clubs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mailerdaemon.app.R
import com.mailerdaemon.app.club_id
import com.mailerdaemon.app.utils.ChromeTab
import kotlinx.android.synthetic.admin.fragment_club_details.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets

class ClubDetailBottomSheet : BottomSheetDialogFragment() {

    private var selectedclub = 0
    private var selectedtag = 0
    private lateinit var chromeTab: ChromeTab
    private lateinit var ob: JSONObject

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_club_details, container, false)
        chromeTab = ChromeTab(context)
        selectedclub = requireArguments().getInt(club_id)
        getJson()
        return view
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        web.isHorizontalScrollBarEnabled = true
        club_icon.hierarchy.setProgressBarImage(CircularProgressDrawable((context)!!))
        club_name.text = ob.getString("name")
        club_icon.setActualImageResource(selectedtag)
        club_members.text = ob.getString("members")
        club_fb.setOnClickListener { chromeTab.openTab(ob.getString("fb")) }
        club_description.setOnClickListener {
            if (ic_open.visibility == VISIBLE) {
                ic_open.visibility = GONE
                ic_close.visibility = VISIBLE
                club_des.visibility = VISIBLE
                club_des.text = ob.getString("description")
            } else {
                ic_open.visibility = VISIBLE
                ic_close.visibility = GONE
                club_des.visibility = GONE
            }
        }
        ic_open.setOnClickListener {
            ic_open.visibility = GONE
            ic_close.visibility = VISIBLE
            club_des.visibility = VISIBLE
            club_des.text = ob.getString("description")
        }
        ic_close.setOnClickListener {
            ic_open.visibility = VISIBLE
            ic_close.visibility = GONE
            club_des.visibility = GONE
        }

        if (ob.getString("insta").isNotEmpty()) {
            club_insta.visibility = VISIBLE
            club_insta.setOnClickListener { chromeTab.openTab(ob.getString("insta")) }
        }
        if (ob.getString("youtube").isNotEmpty()) {
            club_youtube.visibility = VISIBLE
            club_youtube.setOnClickListener { chromeTab.openTab(ob.getString("youtube")) }
        }
        if (ob.getString("web").isNotEmpty()) {
            club_web.visibility = VISIBLE
            club_web.setOnClickListener { chromeTab.openTab(ob.getString("web")) }
        }
        setposts(ob.getString("fb"))
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
            selectedtag = logo.getResourceId(ob.getInt("tag") - 1, 0)
            logo.recycle()
        } catch (jsonException: JSONException) {
            jsonException.printStackTrace()
        } catch (jsonException: IOException) {
            jsonException.printStackTrace()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setposts(page: String?) {
        if (page!!.isEmpty()) return
        var site = page
        if (page.length > 25) site = site.substring(25)
        Log.d("Width", Resources.getSystem().displayMetrics.toString())
        val s = "<iframe src=\"https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2F" +
            "$site&tabs=timeline&width=${Resources.getSystem().displayMetrics.xdpi.toInt() - 20}" +
            "&height=1024&small_header=true&adapt_container_width=true&hide_cover=true&" +
            "show_facepile=false&appId=384900825472866\" width=\"100%\" height=\"100%\" style=\"border:none;" +
            "overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\"" +
            " allow=\"encrypted-media\"></iframe>"
        web.loadHtml(s)
    }
}
