package com.mailerdaemon.app

data class UserModel(
    var userId: String? = null,
    var name: String? = null,
    var email: String? = null,
    var rejectedPost: Boolean? = null
)
