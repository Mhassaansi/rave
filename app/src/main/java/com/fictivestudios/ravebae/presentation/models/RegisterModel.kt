package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName


data class RegisterModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data(),

    var error: String = "no"
)

data class Data (
    @SerializedName("user_id" ) var userId : String? = null
)