package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName

data class SendRequestModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Datab> = arrayListOf()
)

data class Datab(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("sender_Id") var senderId: String? = null,
    @SerializedName("reciever_Id") var recieverId: RecieverId? = RecieverId(),
    @SerializedName("request_status") var requestStatus: Int? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("__v") var _v: Int? = null,
    @SerializedName("is_heart") var isHeart: Boolean? = null
)

data class RecieverId(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("user_image") var userImage: String? = null,
    @SerializedName("user_description") var userDescription: String? = null,
    @SerializedName("username") var username: String? = null
)