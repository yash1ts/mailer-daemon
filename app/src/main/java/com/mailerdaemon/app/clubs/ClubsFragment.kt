package com.mailerdaemon.app.clubs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.mailerdaemon.app.R
import com.mailerdaemon.app.events.PostModel
import com.mailerdaemon.app.utils.DialogOptions
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets

class ClubsFragment : Fragment(), DialogOptions {
    private var iconModel: MutableList<ClubIconModel>? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clubs, container, false)
        val adapter = ClubAdapter(this)
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_clubs)
        recyclerView.adapter = adapter
        iconModel = mutableListOf()
        getJson()
        adapter.setData(iconModel!!)
        adapter.notifyDataSetChanged()
        return view
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

            for (i in 0 until jsonObject.getJSONArray("modelList").length()) {
                val `object` = jsonObject.getJSONArray("modelList").getJSONObject(i)
                val tag = `object`.getInt("tag")
                val url = `object`.getString("club")
                val icon = ClubIconModel()
                icon.url = url
                icon.tag = tag
                iconModel!!.add(icon)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun showOptions(model: PostModel, path: String) {}
    override fun showDialog(path: String) {
        val bundle = Bundle()
        bundle.putInt("club_id", path.toInt())
        val fragment = ClubDetailBottomSheet()
        fragment.arguments = bundle
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.bottomSheetTransparent)
        fragment.show(childFragmentManager, null)
    }
}
