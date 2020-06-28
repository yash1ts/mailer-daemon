package com.mailerdaemon.app.impContacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.mailerdaemon.app.R
import kotlinx.android.synthetic.main.fragment_web_view.view.*

class HtmlFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        view.web_view.let {
            it.settings.javaScriptEnabled = true
            it.webViewClient = WebViewClient()
            it.settings.loadWithOverviewMode = true
            it.settings.useWideViewPort = true
            it.settings.setSupportZoom(true)
            it.settings.builtInZoomControls = true
            it.settings.displayZoomControls = false
            it.loadUrl(arguments!!.getString("url"))
        }
        return view
    }
}
