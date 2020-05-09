package com.mailerdaemon.app.clubs

import java.io.Serializable

data class ClubIconModel(
    var url: String? = null,
    var tag: Int? = null
) : Serializable
