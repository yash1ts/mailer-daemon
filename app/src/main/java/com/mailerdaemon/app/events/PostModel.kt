package com.mailerdaemon.app.events

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

class PostModel(
    var heading: String? = null,
    var details: String? = null,
    var photo: String? = null,
    @ServerTimestamp
    var date: Date? = null
)
