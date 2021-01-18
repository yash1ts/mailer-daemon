package com.mailerdaemon.app

class FlavorConstants {
    enum class Type {
        STUDENT, ADMIN
    }
    companion object {
        @JvmField
        val type = Type.STUDENT
    }
}
