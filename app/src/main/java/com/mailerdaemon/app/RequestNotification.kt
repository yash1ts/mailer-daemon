package com.mailerdaemon.app

data class RequestNotification(
    var android: Android,
    var topic: String
){
    data class Android(
        var notification: Notification
    ){
        data class Notification(
            var title: String,
            var body: String,
            var click_action: String,
            var image: String? = null
        )
    }
}
