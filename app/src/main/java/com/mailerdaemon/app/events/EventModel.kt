package com.mailerdaemon.app.events

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class EventModel(
    var posts: List<PostModel>? = null,
    var name: String? = null,
    @ServerTimestamp
    var date: Date? = null
)
