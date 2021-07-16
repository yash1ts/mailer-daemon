package com.mailerdaemon.app

data class NotificationModel(
    var android: Android,
    var data: Map<String, String>?,
    var topic: String
) {
    data class Android(
        var notification: Notification
    ) {
        data class Notification(
            var title: String,
            var body: String,
            var click_action: String,
            var image: String? = null
        )
    }
}
