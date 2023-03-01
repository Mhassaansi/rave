package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName


data class PlayListItemsModel(

    @SerializedName("href") var href: String? = null,
    @SerializedName("items") var items: ArrayList<Itemss> = arrayListOf(),
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("next") var next: String? = null,
    @SerializedName("offset") var offset: Int? = null,
    @SerializedName("previous") var previous: String? = null,
    @SerializedName("total") var total: Int? = null,

    // Case for handeling error
    @SerializedName("error") var errorModel: ErrorModel? = null,

   //  var error : String = "no"
)

//data class Error (
//    @SerializedName("error") var error : ErrorModel? = ErrorModel()
//)

data class ErrorModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null
)

data class Itemss(

    @SerializedName("added_at") var addedAt: String? = null,
    @SerializedName("added_by") var addedBy: AddedBy? = AddedBy(),
    @SerializedName("is_local") var isLocal: Boolean? = null,
    @SerializedName("primary_color") var primaryColor: String? = null,
    @SerializedName("track") var track: Track? = Track(),
    @SerializedName("video_thumbnail") var videoThumbnail: VideoThumbnail? = VideoThumbnail()

)

data class Track(
    @SerializedName("album") var album: Album? = Album(),
    @SerializedName("artists") var artists: ArrayList<Artists> = arrayListOf(),
    @SerializedName("available_markets") var availableMarkets: ArrayList<String> = arrayListOf(),
    @SerializedName("disc_number") var discNumber: Int? = null,
    @SerializedName("duration_ms") var durationMs: Int? = null,
    @SerializedName("episode") var episode: Boolean? = null,
    @SerializedName("explicit") var explicit: Boolean? = null,
    @SerializedName("external_ids") var externalIds: ExternalIds? = ExternalIds(),
    @SerializedName("external_urls") var externalUrls: ExternalUrls? = ExternalUrls(),
    @SerializedName("href") var href: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("is_local") var isLocal: Boolean? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("popularity") var popularity: Int? = null,
    @SerializedName("preview_url") var previewUrl: String? = null,
    @SerializedName("track") var track: Boolean? = null,
    @SerializedName("track_number") var trackNumber: Int? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("uri") var uri: String? = null
)

data class VideoThumbnail(
    @SerializedName("url") var url: String? = null
)

data class ExternalIds(

    @SerializedName("isrc") var isrc: String? = null

)

data class Artists(

    @SerializedName("external_urls") var externalUrls: ExternalUrls? = ExternalUrls(),
    @SerializedName("href") var href: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("uri") var uri: String? = null

)

data class Album(
    @SerializedName("album_type") var albumType: String? = null,
    @SerializedName("artists") var artists: ArrayList<Artists> = arrayListOf(),
    @SerializedName("available_markets") var availableMarkets: ArrayList<String> = arrayListOf(),
    @SerializedName("external_urls") var externalUrls: ExternalUrls? = ExternalUrls(),
    @SerializedName("href") var href: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("images") var images: ArrayList<Images> = arrayListOf(),
    @SerializedName("name") var name: String? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("release_date_precision") var releaseDatePrecision: String? = null,
    @SerializedName("total_tracks") var totalTracks: Int? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("uri") var uri: String? = null
)

data class AddedBy(
    @SerializedName("external_urls") var externalUrls: ExternalUrls? = ExternalUrls(),
    @SerializedName("href") var href: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("uri") var uri: String? = null
)


