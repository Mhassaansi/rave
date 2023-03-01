package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName

data class ChatRecieveModel(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("sender_id") var senderId: SenderIddd? = SenderIddd(),
    @SerializedName("receiver_id") var receiverId: ReceiverId? = ReceiverId(),
    @SerializedName("message") var message: String? = null,
    @SerializedName("is_read") var isRead: String? = null,
    @SerializedName("is_blocked") var isBlocked: String? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("__v") var _v: Int? = null
)


data class ReceiverId(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("user_image") var userImage: String? = null
)

data class SenderIddd(
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("user_image") var userImage: String? = null
)