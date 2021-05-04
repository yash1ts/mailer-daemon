package com.mailerdaemon.app

import com.mailerdaemon.app.notices.PostModel
import com.mailerdaemon.app.placement.PlacementModel
import retrofit2.Call
import retrofit2.http.GET

interface Repository {
    @GET("/posts")
    fun getPosts(): Call<List<PostModel>?>?

    @GET("/place")
    fun getPlacementPosts(): Call<List<PlacementModel>?>?
}
