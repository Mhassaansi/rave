package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName

data class RequestModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Datan> = arrayListOf(),

    var error: String = "no"
)

data class Datan(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("sender_Id") var senderId: SenderId? = SenderId(),
    @SerializedName("reciever_Id") var recieverId: String? = null,
    @SerializedName("request_status") var requestStatus: Int? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("__v") var _v: Int? = null,
    @SerializedName("is_heart") var isHeart: Boolean? = null
)

data class SenderId(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("user_image") var userImage: String? = null,
    @SerializedName("user_description") var userDescription: String? = null,
    @SerializedName("username") var username: String? = null
)