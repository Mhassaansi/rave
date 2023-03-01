package com.fictivestudios.ravebae.utils

import com.fictivestudios.ravebae.presentation.models.ErrorModel
import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = Data(),

    // My defined error
    @SerializedName("myerror")
    var error: String? = "no",
    // Case for handeling error
    @SerializedName("error") var errorModel: ErrorModel? = null
    )

data class Data(
    @SerializedName("cover_image") var coverImage: String? = null,
    @SerializedName("user_image") var userImage: String? = null,
    @SerializedName("_id") var Id: String? = null,
    // Duplicate ID for someAPI's
    @SerializedName("user_id") var user_id: String? = null,
    @SerializedName("user_gender") var userGender: String? = null,
    @SerializedName("user_description") var userDescription: String? = null,
    @SerializedName("verification_code") var verificationCode: Int? = null,
    @SerializedName("is_verified") var isVerified: Int? = null,
    @SerializedName("is_blocked") var isBlocked: Int? = null,
    @SerializedName("user_authentication") var userAuthentication: String? = null,
    @SerializedName("user_social_token") var userSocialToken: String? = null,
    @SerializedName("user_social_type") var userSocialType: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("phone_number") var phoneNumber: String? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("__v") var _v: Int? = null,
    @SerializedName("user_photos") var userPhotos: ArrayList<String> = arrayListOf()
)