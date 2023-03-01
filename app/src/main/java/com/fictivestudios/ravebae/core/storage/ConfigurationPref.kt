package com.fictivestudios.ravebae.core.storage

import android.content.Context
import android.content.SharedPreferences

class ConfigurationPref(context: Context) {
    private val mPrefs: SharedPreferences =
        context.getSharedPreferences("User_Config", Context.MODE_PRIVATE)

    // User Photos URI Array
    var userPhoto1: String?
        get() = mPrefs.getString("userPhoto1", "")
        set(userPhoto1) {
            val mEditor = mPrefs.edit()
            mEditor.putString("userPhoto1", userPhoto1!!)
            mEditor.apply()
        }

    var userPhoto2: String?
        get() = mPrefs.getString("userPhoto2", "")
        set(userPhoto2) {
            val mEditor = mPrefs.edit()
            mEditor.putString("userPhoto2", userPhoto2!!)
            mEditor.apply()
        }

    var userPhoto3: String?
        get() = mPrefs.getString("userPhoto3", "")
        set(userPhoto3) {
            val mEditor = mPrefs.edit()
            mEditor.putString("userPhoto3", userPhoto3!!)
            mEditor.apply()
        }

    var userPhoto4: String?
        get() = mPrefs.getString("userPhoto4", "")
        set(userPhoto4) {
            val mEditor = mPrefs.edit()
            mEditor.putString("userPhoto4", userPhoto4!!)
            mEditor.apply()
        }

    var userPhoto5: String?
        get() = mPrefs.getString("userPhoto5", "")
        set(userPhoto5) {
            val mEditor = mPrefs.edit()
            mEditor.putString("userPhoto5", userPhoto5!!)
            mEditor.apply()
        }

    var userPhoto6: String?
        get() = mPrefs.getString("userPhoto6", "")
        set(userPhoto6) {
            val mEditor = mPrefs.edit()
            mEditor.putString("userPhoto6", userPhoto6!!)
            mEditor.apply()
        }

    var userPhoto7: String?
        get() = mPrefs.getString("userPhoto7", "")
        set(prefState) {
            val mEditor = mPrefs.edit()
            mEditor.putString("userPhoto7", userPhoto7!!)
            mEditor.apply()
        }

    var userPhoto8: String?
        get() = mPrefs.getString("userPhoto8", "")
        set(userPhoto8) {
            val mEditor = mPrefs.edit()
            mEditor.putString("userPhoto8", userPhoto8!!)
            mEditor.apply()
        }
    // User Photos URI Array End


    var preferenceToggleState: String?
        get() = mPrefs.getString("preferenceToggleState", "")
        set(prefState) {
            val mEditor = mPrefs.edit()
            mEditor.putString("preferenceToggleState", prefState!!)
            mEditor.apply()
        }

    var notificationState: Boolean?
        get() = mPrefs.getBoolean("notificationState", true)
        set(state) {
            val mEditor = mPrefs.edit()
            mEditor.putBoolean("notificationState", state!!)
            mEditor.apply()
        }

    // To indicate comming from which screen
    var from: String?
        get() = mPrefs.getString("from", "")
        set(id) {
            val mEditor = mPrefs.edit()
            mEditor.putString("from", id)
            mEditor.apply()
        }

    var spotifyAccessToken: String?
        get() = "Authorization\": 'Bearer " + mPrefs.getString("spotifyAccessToken", "")
        set(AccessToken) {
            val mEditor = mPrefs.edit()
            mEditor.putString("spotifyAccessToken", AccessToken)
            mEditor.apply()
        }

    var conversationUserId: String?
        get() = mPrefs.getString("conversationUserId", "")
        set(id) {
            val mEditor = mPrefs.edit()
            mEditor.putString("conversationUserId", id)
            mEditor.apply()
        }

    /* ==== Chat Data === */
    var isSelfChat: Boolean?
        get() = mPrefs.getBoolean("isselfchat", false)
        set(self) {
            val mEditor = mPrefs.edit()
            mEditor.putBoolean("isselfchat", self!!)
            mEditor.apply()
        }

    var chatSenderId: String?
        get() = mPrefs.getString("chatsenderid", "")
        set(id) {
            val mEditor = mPrefs.edit()
            mEditor.putString("chatsenderid", id)
            mEditor.apply()
        }

    var chatReceiverId: String?
        get() = mPrefs.getString("chatreceiverid", "")
        set(id) {
            val mEditor = mPrefs.edit()
            mEditor.putString("chatreceiverid", id)
            mEditor.apply()
        }
    var chatSenderPhotoUrl: String?
        get() = mPrefs.getString("chatsenderphotourl", "")
        set(url) {
            val mEditor = mPrefs.edit()
            mEditor.putString("chatsenderphotourl", url)
            mEditor.apply()
        }
    var chatUserName: String?
        get() = mPrefs.getString("chatusername", "")
        set(name) {
            val mEditor = mPrefs.edit()
            mEditor.putString("chatusername", name)
            mEditor.apply()
        }
    /* ==== Chat Data End=== */


    var userId: String?
        get() = mPrefs.getString("UserId", "")
        set(Token) {
            val mEditor = mPrefs.edit()
            mEditor.putString("UserId", Token)
            mEditor.apply()
        }
    var userName: String?
        get() = mPrefs.getString("UserName", "")
        set(Token) {
            val mEditor = mPrefs.edit()
            mEditor.putString("UserName", Token)
            mEditor.apply()
        }
    var userGender: String?
        get() = mPrefs.getString("UserGender", "")
        set(Token) {
            val mEditor = mPrefs.edit()
            mEditor.putString("UserGender", Token)
            mEditor.apply()
        }
    var mobileNumber: String?
        get() = mPrefs.getString("mobileNumber", "")
        set(Token) {
            val mEditor = mPrefs.edit()
            mEditor.putString("mobileNumber", Token)
            mEditor.apply()
        }
    var description: String?
        get() = mPrefs.getString("description", "")
        set(Token) {
            val mEditor = mPrefs.edit()
            mEditor.putString("description", Token)
            mEditor.apply()
        }
    var email: String?
        get() = mPrefs.getString("email", "")
        set(Token) {
            val mEditor = mPrefs.edit()
            mEditor.putString("email", Token)
            mEditor.apply()
        }
    var language: String?
        get() = mPrefs.getString("language", "en")
        set(language) {
            val mEditor = mPrefs.edit()
            mEditor.putString("language", language)
            mEditor.apply()
        }
    var token: String?
        get() = "bearer " + mPrefs.getString("Token", "")
        set(Token) {
            val mEditor = mPrefs.edit()
            mEditor.putString("Token", Token)
            mEditor.apply()
        }
    var fCMToken: String?
        get() = mPrefs.getString("FCMToken", "")
        set(Token) {
            val mEditor = mPrefs.edit()
            mEditor.putString("FCMToken", Token)
            mEditor.apply()
        }
    var user: String?
        get() = mPrefs.getString("UserData", "")
        set(data) {
            val mEditor = mPrefs.edit()
            mEditor.putString("UserData", data)
            mEditor.apply()
        }
    var profilePic: String?
        get() = mPrefs.getString("pic", null)
        set(pic) {
            val mEditor = mPrefs.edit()
            mEditor.putString("pic", pic)
            mEditor.apply()
        }

    var coverPic: String?
        get() = mPrefs.getString("coverpic", null)
        set(pic) {
            val mEditor = mPrefs.edit()
            mEditor.putString("coverpic", pic)
            mEditor.apply()
        }

    fun clear() {
        val mEditor = mPrefs.edit()
        mEditor.clear()
        mEditor.apply()
    }

    var latitude: Float
        get() = mPrefs.getFloat("Latitude", 0.0f)
        set(data) {
            val mEditor = mPrefs.edit()
            mEditor.putFloat("Latitude", data)
            mEditor.apply()
        }
    var longitude: Float
        get() = mPrefs.getFloat("Longitude", 0.0f)
        set(data) {
            val mEditor = mPrefs.edit()
            mEditor.putFloat("Longitude", data)
            mEditor.apply()
        }

}