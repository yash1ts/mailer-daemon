package com.mailerdaemon.app.lostAndFound

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class LostAndFoundModel(
    var heading: String? = null,
    var details: String? = null,
    var photo: String? = null,
    @ServerTimestamp
    var date: Date? = null,
    var verified: Boolean? = null,
    var uid: String? = null
)
