package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName


data class PlayListModel(
    @SerializedName("href") var href: String? = null,
    @SerializedName("items") var items: ArrayList<Items> = arrayListOf(),
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("next") var next: String? = null,
    @SerializedName("offset") var offset: Int? = null,
    @SerializedName("previous") var previous: String? = null,
    @SerializedName("total") var total: Int? = null,

    // Added
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: Int? = null,

    @SerializedName("my_error")
    var error: String = "no",
    // Case for handeling error
    @SerializedName("error") var errorModel: ErrorModel? = null,
    )

data class Items(
    @SerializedName("collaborative") var collaborative: Boolean? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("external_urls") var externalUrls: ExternalUrls? = ExternalUrls(),
    @SerializedName("href") var href: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("images") var images: ArrayList<Images> = arrayListOf(),
    @SerializedName("name") var name: String? = null,
    @SerializedName("owner") var owner: Owner? = Owner(),
    @SerializedName("primary_color") var primaryColor: String? = null,
    @SerializedName("public") var public: Boolean? = null,
    @SerializedName("snapshot_id") var snapshotId: String? = null,
    @SerializedName("tracks") var tracks: Tracks? = Tracks(),
    @SerializedName("type") var type: String? = null,
    @SerializedName("uri") var uri: String? = null
)

data class Owner(
    @SerializedName("display_name") var displayName: String? = null,
    @SerializedName("external_urls") var externalUrls: ExternalUrls? = ExternalUrls(),
    @SerializedName("href") var href: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("uri") var uri: String? = null
)

data class Tracks(
    @SerializedName("href") var href: String? = null,
    @SerializedName("total") var total: Int? = null
)

data class Images(
    @SerializedName("height") var height: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("width") var width: String? = null
)

data class ExternalUrls(
    @SerializedName("spotify") var spotify: String? = null
)