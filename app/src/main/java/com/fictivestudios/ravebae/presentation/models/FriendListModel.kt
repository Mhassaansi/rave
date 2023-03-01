package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName

data class FriendListModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Datac> = arrayListOf()
)

data class Datac(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("sender_Id") var senderId: SenderIdd? = SenderIdd(),
    @SerializedName("reciever_Id") var recieverId: RecieverIdd? = RecieverIdd(),
    @SerializedName("request_status") var requestStatus: Int? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("__v") var _v: Int? = null
)

data class RecieverIdd(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("user_image") var userImage: String? = null,
    @SerializedName("user_description") var userDescription: String? = null,
    @SerializedName("username") var username: String? = null

)

data class SenderIdd(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("user_image") var userImage: String? = null,
    @SerializedName("user_description") var userDescription: String? = null,
    @SerializedName("username") var username: String? = null

)