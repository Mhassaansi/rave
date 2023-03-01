package com.fictivestudios.ravebae.utils

class Constants {
    companion object {

        const val IS_USER = "is_user"

        const val imageURL = "https://webservices.ravebae.org:3010/" // https://server.appsstaging.com:3010/

        var isAccountLogin = false
        var loginType: String? = null
        const val EMAIL = "email"
        const val PHONE = "phone"
        const val VIEW_TYPE_MESSAGE_SENT = 12
        const val VIEW_TYPE_MESSAGE_RECEIVED = 15

        fun isValidEmail(str: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
        }
    }
}