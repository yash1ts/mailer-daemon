package com.mailerdaemon.app.FbPosts;

import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.os.Parcelable;
import android.se.omapi.Session;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.facebook.AccessToken;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.Retrofit.Datum;
import com.mailerdaemon.app.Retrofit.FbApi;
import com.mailerdaemon.app.Retrofit.FbPostModel;
import com.mailerdaemon.app.Retrofit.RetrofitApiProvider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FbPostsFragment extends Fragment {
    public FbPostsFragment()
    {

    }

    private FbApi fbApi;
    private FbPostModel fbPostModel;
    private RecyclerView recyclerView;
    private String string;
    List<Datum> list=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_fb_posts,container,false);

        fbApi= RetrofitApiProvider.getApi();
        recyclerView=view.findViewById(R.id.rv_fb_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        final FbPostsAdapter adapter=new FbPostsAdapter(this.getContext());
        final WebView webView = view.findViewById(R.id.web);


        if(savedInstanceState!=null)
        {
            list=savedInstanceState.getParcelableArrayList("key");
            recyclerView.setAdapter(adapter);
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
        else {
            Call<FbPostModel> call=fbApi.getPosts("me/posts?fields=link,picture,full_picture,message,id&access_token="+AccessToken.getCurrentAccessToken().getToken());
            call.enqueue(new Callback<FbPostModel>() {
                @Override
                public void onResponse(Call<FbPostModel> call, Response<FbPostModel> response) {
                    fbPostModel=response.body();
                    list=fbPostModel.getData();
                    recyclerView.setAdapter(adapter);
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();



                }

                @Override
                public void onFailure(Call<FbPostModel> call, Throwable t) {
                }
            });
        }




        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) list);
    }

}
