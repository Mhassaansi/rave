package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName

data class ChatSendModel(
    @SerializedName("sender_id")
    val currentUserId: String?,
    @SerializedName("receiver_id")
    val receiverUserId: String?,
    @SerializedName("message")
    val message: String?
)
