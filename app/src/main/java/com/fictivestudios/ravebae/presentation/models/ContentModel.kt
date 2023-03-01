package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName

//data class ContentModel(
//    val message: List<Message?>?,
//    val status: Int?,
//
//    var error: String = "no"
//)

data class ContentModel(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Datacc> = arrayListOf(),

    var error: String = "no"
)

data class Datacc(

    @SerializedName("_id") var Id: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("content") var content: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("__v") var _v: Int? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null

)