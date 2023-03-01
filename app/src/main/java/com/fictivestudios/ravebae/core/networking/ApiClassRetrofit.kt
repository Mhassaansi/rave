package com.fictivestudios.ravebae.core.networking

import com.fictivestudios.ravebae.presentation.models.*
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory


class ApiClassRetrofit private constructor() {
    val apis: Apis
    val spotifyApis: SpotifyApis

    interface Apis {
        @FormUrlEncoded
        @POST("api/login")
        fun getLoginData(
            @FieldMap params: HashMap<String, String>
        ): Call<LoginModel>

        @Multipart
        @POST("api/register")
        fun getRegisterData(
            @Part("username") name: RequestBody?,
            @Part("email") email: RequestBody?,
            @Part("password") password: RequestBody?,
            @Part("phone_number") phoneNumber: RequestBody?,
            @Part("user_gender") userGender: RequestBody?,
            @Part("user_description") userDescription: RequestBody?,
            @Part user_image: MultipartBody.Part?,
            @Part("user_device_type") deviceType: RequestBody?,
            @Part("user_device_token") token: RequestBody?,
        ): Call<RegisterModel>

        @FormUrlEncoded
        @POST("api/verifyOtp")
        fun getOtpData(
            @FieldMap params: HashMap<String, String>
        ): Call<LoginModel>

        @FormUrlEncoded
        @POST("api/resend-code")
        fun getResendOtpData(
            @FieldMap params: HashMap<String, String>
        ): Call<LoginModel>

        @FormUrlEncoded
        @POST("api/logout")
        fun getLogoutData(
            @Header("Authorization") Authorization: String,
            @FieldMap params: HashMap<String, String>
        ): Call<LoginModel>

        @FormUrlEncoded
        @POST("api/change-password")
        fun getChangePasswordData(
            @Header("Authorization") Authorization: String,
            @FieldMap params: HashMap<String, String>
        ): Call<LoginModel>

        @FormUrlEncoded
        @POST("api/forgetpassword")
        fun getForgotPasswordData(
            @FieldMap params: HashMap<String, String>
        ): Call<LoginModel>

        @FormUrlEncoded
        @POST("api/updatePassword")
        fun getUpdatePasswordData(
            @FieldMap params: HashMap<String, String>
        ): Call<LoginModel>

        // @Path("user_id") userId: String, {user_id} // 62cd45e83507d4179bc6668b
        @Multipart
        @POST("api/update-profile/{user_id}")
        fun getUpdateProfileData(
            @Header("Authorization") Authorization: String,
            @Path("user_id") userId: String,
            @Part("username") name: RequestBody?,
            @Part("phone_number") phoneNumber: RequestBody?,
            @Part("user_gender") userGender: RequestBody?,
            @Part("user_description") userDescription: RequestBody?,
            @Part user_image: MultipartBody.Part?,
            @Part cover_image: MultipartBody.Part?,
            @Part user_photo1: MultipartBody.Part?,
            @Part user_photo2: MultipartBody.Part?,
            @Part user_photo3: MultipartBody.Part?,
            @Part user_photo4: MultipartBody.Part?,
            @Part user_photo5: MultipartBody.Part?,
            @Part user_photo6: MultipartBody.Part?,
            @Part user_photo7: MultipartBody.Part?,
            @Part user_photo8: MultipartBody.Part?,
            ): Call<LoginModel>

        // api/add-friend
        // api/request-status
        @FormUrlEncoded
        @POST("api/socialLogin")
        fun getSocialLoginData(
            @FieldMap params: HashMap<String, String>
        ): Call<LoginModel>

        @GET("api/request-send")
        fun getSendRequestData(@Header("Authorization") Authorization: String?): Call<SendRequestModel>

        @GET("api/request-received")
        fun getRecievedRequestData(@Header("Authorization") Authorization: String?): Call<RequestModel>

        @GET("api/profile/{user_id}")
        fun getProfileData(
            @Header("Authorization") Authorization: String,
            @Path("user_id") userId: String
        ): Call<ProfileModel>


        // @FieldMap params: HashMap<String, String>
        // @Field("spotify_list")
        // @Field("spotify_list") spotifyList: List<SongItem>
        // @Body spotifyList: List<SongItem>

//        ,
//        @Field("interest") interest: String
        // @Multipart
        // @FormUrlEncoded
        @POST("api/add-prefrences")
        fun getPreferenceData(
            @Header("Authorization") Authorization: String,
            @Body spotifyList: ArrayListSongsItem
        ): Call<LoginModel>

        @GET("api/content/terms_and_conditions")
        fun getContentTermsAndConditionData(): Call<ContentModel>

        @GET("api/content/privacy_policy")
        fun getContentPrivacyPolicyData(): Call<ContentModel>

        @GET("api/matchList")
        fun getMatchListData(@Header("Authorization") Authorization: String): Call<MatchListModel>

        @FormUrlEncoded
        @POST("api/add-friend")
        fun getAddFriendData(
            @Header("Authorization") Authorization: String,
            @Field("reciever_Id") receiverId: String?,
            @Field("request_status") requestStatus: Int?,
            @Field("is_heart") isHeart: Int?
        ): Call<LoginModel>

        @FormUrlEncoded
        @POST("api/request-status")
        fun getAcceptRejectFriendRequestData(
            @Header("Authorization") Authorization: String,
            @FieldMap params: HashMap<String, String>
        ): Call<LoginModel>

        @GET("api/friendList")
        fun getFriendsListData(@Header("Authorization") Authorization: String): Call<FriendListModel>

        @FormUrlEncoded
        @POST("api/notification")
        fun getNotificationData(
            @Header("Authorization") Authorization: String,
            @FieldMap params: HashMap<String, String>
        ): Call<LoginModel>

        @POST("api/delete-profile/{id}")
        fun deleteUserProfile(
            @Header("Authorization") Authorization: String,
            @Path("id") id: String
        ) : Call<LoginModel>

        @POST("api/block-unblock")
        @FormUrlEncoded
        fun blockandUnblockUser(
            @Header("Authorization") Authorization: String,
            @Field("reported_user_id") reported_user_id : String,
            @Field("type") type : String
        ) : Call<BlockModel>
    }

    interface SpotifyApis {
        @GET("me/playlists")
        fun getPlayListData(@Header("Authorization") Authorization: String): Call<PlayListModel>

        @GET("playlists/{playlist_id}/tracks")
        fun getPlayListItemsData(@Header("Authorization") Authorization: String,
                                 @Path("playlist_id") playListId: String): Call<PlayListItemsModel>
    }

    companion object {
        // 4500
        private val ApiURL = "https://webservices.ravebae.org:3010/" // https://server.appsstaging.com:3010/
        private val spotifyURL = "https://api.spotify.com/v1/"


        @JvmStatic
        var instance: ApiClassRetrofit? = null
            get() {
                if (field == null) {
                    field = ApiClassRetrofit()
                }
                return field
            }
            private set
    }

    init {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val mRetrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ApiURL)
            .client(okHttpClient)
            .build()
        apis = mRetrofit.create(Apis::class.java)

        val mRetrofitt = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(spotifyURL)
            .client(okHttpClient)
            .build()
        spotifyApis = mRetrofitt.create(SpotifyApis::class.java)
    }

}