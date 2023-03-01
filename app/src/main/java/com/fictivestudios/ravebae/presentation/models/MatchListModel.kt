package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName


data class MatchListModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Datam> = arrayListOf(),


    var error: String = "no"
)


data class Datam(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("user_id") var userId: UserIdm? = UserIdm(),
    @SerializedName("interest") var interest: String? = null,
    // String, SpotifyTestModel
    @SerializedName("spotify_list") var spotifyList: ArrayList<String> = arrayListOf(),
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("__v") var _v: Int? = null
)


data class UserIdm(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("user_gender") var userGender: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("user_image") var userImage: String? = null,
    // Added field
    @SerializedName("user_description") var userDescription: String? = null,
    @SerializedName("user_photos") var userPhotos: ArrayList<String> = arrayListOf()
)

