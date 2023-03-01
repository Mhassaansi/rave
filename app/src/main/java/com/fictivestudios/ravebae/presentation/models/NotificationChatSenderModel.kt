package com.fictivestudios.ravebae.presentation.models

import com.google.gson.annotations.SerializedName

data class NotificationChatSenderModel(
    @SerializedName("user_device_token") var userDeviceToken: String? = null,
    @SerializedName("user_image") var userImage: String? = null,
    @SerializedName("user_device_type") var userDeviceType: String? = null,
    @SerializedName("user_social_token") var userSocialToken: String? = null,
    @SerializedName("user_gender") var userGender: String? = null,
    @SerializedName("is_verified") var isVerified: Int? = null,
    @SerializedName("friends") var friends: ArrayList<String> = arrayListOf(),
    @SerializedName("user_photos") var userPhotos: ArrayList<String> = arrayListOf(),
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("user_is_profile_complete") var userIsProfileComplete: Int? = null,
    @SerializedName("is_blocked") var isBlocked: Int? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("__v") var _v: Int? = null,
    @SerializedName("user_authentication") var userAuthentication: String? = null,
    @SerializedName("user_social_type") var userSocialType: String? = null,
    @SerializedName("is_notification") var isNotification: Boolean? = null,
    @SerializedName("phone_number") var phoneNumber: Int? = null,
    @SerializedName("_id") var Id: String? = null,
    @SerializedName("cover_image") var coverImage: String? = null,
    @SerializedName("verification_code") var verificationCode: Int? = null,
    @SerializedName("user_description") var userDescription: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null
)