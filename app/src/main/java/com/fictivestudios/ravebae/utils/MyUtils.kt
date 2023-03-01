package com.fictivestudios.ravebae.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.text.TextUtils
import android.util.Patterns


object MyUtils {

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun isNullOrEmpty(str: String?): Boolean {
        return when {
            str == null -> true
            str.trim { it <= ' ' }
                .equals("", ignoreCase = true) -> true
            str.trim { it <= ' ' }
                .equals("Null", ignoreCase = true) -> true
            else -> str.isEmpty()
        }
    }

    fun printAsString(obj: Any?): String {
        return when {
            // "N/A"
            obj == null -> ""
            // "N/A"
            obj.toString().trim { it <= ' ' }
                .equals("", ignoreCase = true) -> ""
            // N/A
            obj.toString()
                .trim { it <= ' ' }
                .equals("Null", ignoreCase = true)
            -> ""
            // N/A
            else -> if (obj.toString().isEmpty()) "" else obj.toString()
        }
    }


    fun isValidMobileNo(str: String?): Boolean {
        val mobNO: Pattern = Pattern.compile("^((\\+92)?(0092)?(92)?(0)?)(3)([0-9]{9})$")
        val matcher: Matcher = mobNO.matcher(str)
        return matcher.find()
    }

    fun isValidEmail(target: String?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
}