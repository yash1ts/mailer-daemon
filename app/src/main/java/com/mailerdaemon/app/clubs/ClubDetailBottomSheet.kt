package com.mailerdaemon.app.clubs

import android.annotation.SuppressLint
import android.os.Build
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
import com.mailerdaemon.app.utils.ChromeTab
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_icon
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_des
import kotlinx.android.synthetic.main.fragment_club_detail.view.club_fb
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

    private var i = 0
    private var it = 0
    private var chromeTab: ChromeTab? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_club_detail, container, false)
        chromeTab = ChromeTab(context)
        view.web.isHorizontalScrollBarEnabled = true
        view.club_icon.hierarchy.setProgressBarImage(CircularProgressDrawable((context)!!))
        if (BuildConfig.DEBUG && arguments == null) {
            error("Assertion failed")
        }
        i = arguments!!.getInt("club_id")
        getJson(view)
        return view
    }

    private fun getJson(view: View) {
        val json: String
        try {
            val inputStream = (context)!!.assets.open("clubs.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
            val jsonObject = JSONObject(json)
            val `object` = jsonObject.getJSONArray("modelList").getJSONObject(i - 1)
            when (`object`.getInt("tag")) {
                1 -> it = R.drawable.md_logo
                2 -> it = R.drawable.litc_logo
                3 -> it = R.drawable.mechismu_logo
                4 -> it = R.drawable.cyberlabs_logo
                5 -> it = R.drawable.chayanika_logo
                6 -> it = R.drawable.ecell_logo
                7 -> it = R.drawable.arka_logo
                8 -> it = R.drawable.quiz_logo
                9 -> it = R.drawable.udaan_logo
                10 -> it = R.drawable.toastmaster_logo
                11 -> it = R.drawable.ffi_logo
                12 -> it = R.drawable.wtc_logo
                13 -> it = R.drawable.kartavya_logo
                14 -> it = R.drawable.rythm_logo
                15 -> it = R.drawable.litm_logo
                16 -> it = R.drawable.lci_logo
                17 -> it = R.drawable.ff_logo
                18 -> it = R.drawable.roboism_logo
                19 -> it = R.drawable.manthan_logo
                20 -> it = R.drawable.adc_logo
                21 -> it = R.drawable.art_logo
            }
            view.club_des.text = `object`.getString("description")
            view.club_name.text = `object`.getString("name")
            view.club_icon.setImageResource(it)
            view.club_members.text = `object`.getString("members")
            view.club_fb.setOnClickListener { chromeTab!!.openTab(`object`.getString("fb")) }
            if (`object`.getString("insta").isNotEmpty()) {
                view.club_insta.visibility = View.VISIBLE
                view.club_insta.setOnClickListener { chromeTab!!.openTab(`object`.getString("insta")) }
            }
            if (`object`.getString("youtube").isNotEmpty()) {
                view.club_youtube.visibility = View.VISIBLE
                view.club_youtube.setOnClickListener { chromeTab!!.openTab(`object`.getString("youtube")) }
            }
            if (`object`.getString("web").isNotEmpty()) {
                view.club_web.visibility = View.VISIBLE
                view.club_web.setOnClickListener { chromeTab!!.openTab(`object`.getString("web")) }
            }
            setposts(page =`object`.getString("fb"),view = view)
        } catch (jsonException: JSONException) {
            jsonException.printStackTrace()
        } catch (jsonException: IOException) {
            jsonException.printStackTrace()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setposts(page: String?,view: View) {
        if (page!!.isEmpty()) return
        //webView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//        @Override
//        public void onGlobalLayout() {
        var site = page
        if (page.length > 25) site = site.substring(25)
        //          int x=webView.getMeasuredWidth()-657;
//          int y=webView.getMeasuredHeight();
        //webView.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);

//          String s="<!DOCTYPE html>\n" +
//                  "<html>\n" +
//                  "<head>\n" +
//                  "\t<title></title>\n" +
//                  "</head>\n" +
//                  "<body style=\"height: 1000px;text-align: center\">\n" +
//                  "\t<div id='yeep' style='width: 100%;height:100%;text-align:center' >\n" +
//                  "\t</div>\n" +
//                  "</body>\n" +
//                  "\n" +
//                  "<script type=\"text/javascript\">\n" +
//                  "\tvar s = document.getElementById(\"yeep\");\n" +
//                  "\ts.innerHTML+=\"<iframe src='https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2F"+site+"&tabs=timeline&width=\"+screen.width+\"&height=\"+screen.height+\"&small_header=true&adapt_container_width=true&hide_cover=true&show_facepile=false&appId=384900825472866'  style='border:none;height:100%;width: 100%; ' scrolling='yes' frameborder='0' allowTransparency='true' allow='encrypted-media'></iframe>\";\n" +
//                  "\t//document.write(s.innerHTML);\n" +
//                  "</script>\n" +
//                  "</html>\n";
        val s = "<iframe src=\"https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2F$site&tabs=timeline&width=350&height=700&small_header=true&adapt_container_width=true&hide_cover=true&show_facepile=false&appId=384900825472866\" width=\"100%\" height=\"100%\" style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\" allow=\"encrypted-media\"></iframe>"
        view.web.loadHtml(s)
        //}
        //});
        view.web.setOnTouchListener { _: View?, event: MotionEvent -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) event.action == ACTION_BUTTON_PRESS else {
            TODO("VERSION.SDK_INT < M")
        }
        }
    }
}
