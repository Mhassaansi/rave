package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName

data class ProfileModel(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Datap? = Datap(),

    var error: String? = "no"
)

data class Datap(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("user_image") var userImage: String? = null,
    @SerializedName("cover_image") var coverImage: String? = null,
    @SerializedName("user_gender") var userGender: String? = null,
    @SerializedName("user_description") var userDescription: String? = null,
    @SerializedName("phone_number") var phoneNumber: String? = null,
    @SerializedName("user_photos") var userPhotos: ArrayList<String> = arrayListOf(),
    @SerializedName("preferences") var preferences: Preferences? = Preferences()

)

data class Preferences(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("interest") var interest: String? = null,
    @SerializedName("spotify_list") var spotifyList: ArrayList<String> = arrayListOf()

)


// Old Model
//data class ProfileModel(
//    @SerializedName("status") var status: Int? = null,
//    @SerializedName("data") var user: User? = User(),
//    // prefences, preferences
//    @SerializedName("preferences") var prefences: Prefences? = Prefences(),
//    // No message provided in this model
//    @SerializedName("message") var message: String? = null,
//    // @SerializedName("message" ) var mMessage : String? = null,
//
//
//    var error: String? = "no"
//)
//
//data class Prefences(
//    @SerializedName("_id") var Id: String? = null,
//    @SerializedName("interest") var interest: String? = null,
//    @SerializedName("spotify_list") var spotifyList: ArrayList<String> = arrayListOf()
//)
//
//data class User(
//    @SerializedName("_id") var Id: String? = null,
//    @SerializedName("user_image") var userImage: String? = null,
//    @SerializedName("cover_image") var coverImage: String? = null,
//    @SerializedName("user_gender") var userGender: String? = null,
//    @SerializedName("user_description") var userDescription: String? = null,
//    @SerializedName("verification_code") var verificationCode: Int? = null,
//    @SerializedName("is_verified") var isVerified: Int? = null,
//    @SerializedName("is_blocked") var isBlocked: Int? = null,
//    @SerializedName("user_authentication") var userAuthentication: String? = null,
//    @SerializedName("user_social_token") var userSocialToken: String? = null,
//    @SerializedName("user_social_type") var userSocialType: String? = null,
//    @SerializedName("username") var username: String? = null,
//    @SerializedName("email") var email: String? = null,
//    @SerializedName("password") var password: String? = null,
//    @SerializedName("phone_number") var phoneNumber: String? = null,
//    @SerializedName("createdAt") var createdAt: String? = null,
//    @SerializedName("updatedAt") var updatedAt: String? = null,
//    @SerializedName("__v") var _v: Int? = null,
//    @SerializedName("user_photos") var userPhotos: ArrayList<String> = arrayListOf()
//)