package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName

data class BlockModel(@SerializedName("status") var status: Int? = null,
                      @SerializedName("message") var message: String? = null){
    var error: String = "no"

}