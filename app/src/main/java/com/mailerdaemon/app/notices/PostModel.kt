package com.mailerdaemon.app.notices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostsList(var posts: List<PostModel>) : Parcelable

@Parcelize
data class PostModel(
    val message: String,
    val permalink_url: String,
    val id: String,
    val message_tags: List<Tags>,
    val created_time: String,
    val full_picture: String,
    val attachments: Attachments
) : Parcelable

@Parcelize
data class Attachments(val data: List<SubAttachments>) : Parcelable

@Parcelize
data class SubAttachments(val subAttachments: Item) : Parcelable

@Parcelize
data class Item(val data: List<Data>) : Parcelable

@Parcelize
data class Data(val media: Media, val type: String) : Parcelable

@Parcelize
data class Media(val image: Image) : Parcelable

@Parcelize
data class Image(val src: String, val height: Int, val width: Int) : Parcelable

@Parcelize
data class Tags(val name: String) : Parcelable
