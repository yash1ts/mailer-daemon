package com.mailerdaemon.app.FbPosts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.mailerdaemon.app.R;
import com.mailerdaemon.app.Retrofit.Datum;

import java.util.ArrayList;
import java.util.List;

public class FbPostsAdapter extends RecyclerView.Adapter<FbPostsAdapter.ViewHolder> {
    List<Datum> data=new ArrayList<>();
    Context context;
    private int fl=0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.rv_fb_post,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    public FbPostsAdapter(Context context){
        this.context=context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.webView.loadUrl(data.get(2).getLink());
        /*viewHolder.message.setText(data.get(i).getMessage());
        Picasso.get().load(data.get(i).getFull_picture()).fit().into(viewHolder.image);*/


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Datum> data) {
        this.data=data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        ImageView image;
        WebView webView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fl=0;
            webView = itemView.findViewById(R.id.web);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebClient());
            // message=itemView.findViewById(R.id.tv_fb_post_message);
            //image=itemView.findViewById(R.id.fb_image);

        }

        public class WebClient extends WebViewClient {

            @Override
            public void onPageFinished(final WebView view, String url) {

                view.loadUrl("javascript:(function(){"+"document.getElementById('mobile_login_bar').style.display = 'none';"+
                        "document.getElementById('header').style.display = 'none';"+"})()");

                view.setVisibility(View.VISIBLE);
                super.onPageFinished(view,url);
            }


        }

    }



}

