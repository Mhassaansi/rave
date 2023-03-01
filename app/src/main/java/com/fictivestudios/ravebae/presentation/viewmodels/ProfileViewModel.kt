package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.models.BlockModel
import com.fictivestudios.ravebae.presentation.models.PlayListItemsModel
import com.fictivestudios.ravebae.presentation.models.ProfileModel
import com.fictivestudios.ravebae.presentation.models.RegisterModel
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ProfileViewModel(override var activity: Activity) : BaseViewModel(activity) {
    private var getProfileModelMutableLiveData: SingleLiveEvent<ProfileModel?> = SingleLiveEvent()
    val getProfileModelData: SingleLiveEvent<ProfileModel?>
        get() = getProfileModelMutableLiveData


    private fun setProfileModelMutableLiveDataMainModelData(mainModel: ProfileModel?) {
        setIsLoading(false)
        getProfileModelMutableLiveData.postValue(mainModel)
    }


    private var getUserBlockEvent: SingleLiveEvent<BlockModel?> = SingleLiveEvent()
    val getUserBlockEventData: SingleLiveEvent<BlockModel?>
        get() = getUserBlockEvent

    private fun setUserBlock(mainModel: BlockModel?) {
        setIsLoading(false)
        getUserBlockEventData.postValue(mainModel)
    }

    fun loadProfileData(userId: String) {
        setIsLoading(true)

        instance?.apis?.getProfileData(
            "" + ConfigurationPref(activity).token,
            ""+ userId
        )
            ?.enqueue(object : Callback<ProfileModel> {
                override fun onResponse(
                    call: Call<ProfileModel>,
                    response: Response<ProfileModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setProfileModelMutableLiveDataMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<ProfileModel>() {}.type
                            val errorResponse: ProfileModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "no"

                            setProfileModelMutableLiveDataMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }

                    } catch (e: Exception) {
                        val model = ProfileModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setProfileModelMutableLiveDataMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                    val model = ProfileModel()
                    model.error = "Yes"
                    model.message = "Update Failed"
                    setProfileModelMutableLiveDataMainModelData(model)
                    setIsLoading(false)
                }
            })
    }


    fun userBlockData(userId: String,type:String) {
        setIsLoading(true)

        instance?.apis?.blockandUnblockUser(
            "" + ConfigurationPref(activity).token,
            ""+ userId,
            ""+ type
        )
            ?.enqueue(object : Callback<BlockModel> {
                override fun onResponse(
                    call: Call<BlockModel>,
                    response: Response<BlockModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setUserBlock(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<BlockModel>() {}.type
                            val errorResponse: BlockModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)
                            errorResponse?.error = "no"
                            setUserBlock(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }

                    } catch (e: Exception) {
                        val model = BlockModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setUserBlock(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<BlockModel>, t: Throwable) {
                    val model = BlockModel()
                    model.error = "Yes"
                    model.message = "Update Failed"
                    setUserBlock(model)
                    setIsLoading(false)
                }
            })
    }

// ================ Profile Main =========================


    var playListItemsMutableLiveData: SingleLiveEvent<PlayListItemsModel?> = SingleLiveEvent()
    val playListItemsModelData: SingleLiveEvent<PlayListItemsModel?>
        get() = playListItemsMutableLiveData

    private fun setPlayListItemsMainModelData(mainModel: PlayListItemsModel?) {
        setIsLoading(false)
        playListItemsMutableLiveData.postValue(mainModel)
    }

    fun loadPlayListItemsData(accessToken: String, playListId: String) {
        setIsLoading(true)
        instance?.spotifyApis?.getPlayListItemsData(""+ConfigurationPref(activity).spotifyAccessToken, ""+playListId)
            ?.enqueue(object : Callback<PlayListItemsModel> {
                override fun onResponse(
                    call: Call<PlayListItemsModel>,
                    response: Response<PlayListItemsModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setPlayListItemsMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<PlayListItemsModel>() {}.type
                            val errorResponse: PlayListItemsModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                           // errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setPlayListItemsMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                         val model = PlayListItemsModel()
                         model.errorModel?.message = "Internal Server Error"
                        // model.message = "Internal Server Error"
                        // model.error = "Yes"

                        setPlayListItemsMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<PlayListItemsModel>, t: Throwable) {
                    val model = PlayListItemsModel()
                    model.errorModel?.message = "Failure"
                    // model.message = "Failure"
                    // model.error = "Yes"
                    setPlayListItemsMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
    // ================PlayListItems Main =========================


}






