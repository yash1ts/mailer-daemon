package com.mailerdaemon.app.notices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostsList(var posts: List<PostModel>) : Parcelable

@Parcelize
data class PostModel(
    val _id: String,
    val attachment: List<Photo>,
    val created_time: String,
    val full_picture: String?,
    val id: String,
    val message: String,
    val message_tags: List<String>,
    val permalink_url: String,
    val photo: List<Photo>,
    val video: List<Photo>
) :Parcelable

@Parcelize
data class Photo(
    val media: Media,
    val type: String
) :Parcelable

@Parcelize
data class Media(
    val image: Image
) :Parcelable

@Parcelize
data class Image(
    val height: Int,
    val src: String,
    val width: Int
) :Parcelable

/*@Parcelize
data class PostModel(
    val _id : String?,
    val id : String?,
    val message : String?,
    val created_time : String?,
    val message_tags : List<String>?,
    val permalink_url : String?,
    val photo : List<Photo>?,
    val video : List<String>?,
    val attachment : List<String>?,
    val full_picture : String?
) : Parcelable

@Parcelize
data class Photo (val media : Media, val type : String) : Parcelable

@Parcelize
data class Media (val image : Image) : Parcelable

@Parcelize
data class Image (val height : Int, val src : String, val width : Int) : Parcelable

@Parcelize
data class Tags(val name: String) : Parcelable*/

