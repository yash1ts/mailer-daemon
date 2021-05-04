package com.mailerdaemon.app.placement

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlacementList(var list: List<PlacementModel>) : Parcelable

@Parcelize
data class PlacementModel(
    val message: String,
    val message_tags: List<Tags>,
    val created_time: String,
    val id: String
) : Parcelable

@Parcelize
data class Tags(
    val name: String
) : Parcelable
