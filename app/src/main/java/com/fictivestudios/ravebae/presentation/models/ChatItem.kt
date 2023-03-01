package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName

data class ChatItem(
    @SerializedName("sender_id")
    val currentUserId: String?,
    @SerializedName("receiver_id")
    val receiverUserId: String?
)
