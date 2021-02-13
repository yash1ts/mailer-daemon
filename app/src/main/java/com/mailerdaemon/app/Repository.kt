package com.mailerdaemon.app

import com.mailerdaemon.app.notices.PostModel
import com.mailerdaemon.app.placement.PlacementModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface Repository {
    @GET("/data")
    fun getPosts(): Call<List<PostModel>?>?

    @GET("/place")
    fun getPlacementPosts(): Call<List<PlacementModel>?>?

    @Headers("Content-Type: application/json")
    @POST("/push")
    fun sendNotification(@Body requestNotificaton: RequestNotification): Call<ServerResponse>
}
