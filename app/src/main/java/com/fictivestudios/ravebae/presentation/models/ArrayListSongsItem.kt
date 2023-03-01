package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ArrayListSongsItem(
    // SongItem
    // HashMap<String,String>
    // @field:Expose @field:SerializedName("spotify_list") private val songsList: ArrayList<HashMap<String, String>>
    @field:Expose @field:SerializedName("spotify_list") private val songsList: ArrayList<String> ,
    @Expose @SerializedName("interest") private val gender: String
)
