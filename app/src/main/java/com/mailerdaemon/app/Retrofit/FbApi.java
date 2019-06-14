package com.mailerdaemon.app.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface FbApi {

    String BASE_URL="https://graph.facebook.com/";

    @GET
    Call<FbPostModel> getPosts(@Url String url);
}
