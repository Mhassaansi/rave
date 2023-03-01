package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.models.*
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class PreferenceViewModel(override var activity: Activity) : BaseViewModel(activity) {
    var preferenceModelMutableLiveData: SingleLiveEvent<LoginModel?> = SingleLiveEvent()
    val preferenceModelData: SingleLiveEvent<LoginModel?>
        get() = preferenceModelMutableLiveData

    private fun setPreferenceMainModelData(mainModel: LoginModel?) {
        setIsLoading(false)
        preferenceModelMutableLiveData.postValue(mainModel)
    }

    // spotifyList: List<SongItem>
    fun loadPreferenceData(preference: String, spotifyList: ArrayListSongsItem)  {
        Log.d("spotifyList",""+spotifyList.toString())
        setIsLoading(true)
        // preference
        instance?.apis?.getPreferenceData(""+ConfigurationPref(activity).token, spotifyList)
            ?.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {


                    setIsLoading(false)
                    try {

                        if (response.isSuccessful) {
                            setPreferenceMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<LoginModel>() {}.type
                            val errorResponse: LoginModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setPreferenceMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = LoginModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setPreferenceMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    val model = LoginModel()
                    model.error = "Yes"
                    setPreferenceMainModelData(model)
                    setIsLoading(false)
                }
            })
    }

    // ================ PlayList Main =========================
    var playListModelMutableLiveData: SingleLiveEvent<PlayListModel?> = SingleLiveEvent()
    val playListModelData: SingleLiveEvent<PlayListModel?>
        get() = playListModelMutableLiveData

    private fun setPlayListMainModelData(mainModel: PlayListModel?) {
        setIsLoading(false)
        playListModelMutableLiveData.postValue(mainModel)
    }

    fun loadPlayListData(accessToken: String) {
        setIsLoading(true)
        // Authorization": 'Bearer
        instance?.spotifyApis?.getPlayListData("" + accessToken)
            ?.enqueue(object : Callback<PlayListModel> {
                override fun onResponse(
                    call: Call<PlayListModel>,
                    response: Response<PlayListModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setPlayListMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<PlayListModel>() {}.type
                            val errorResponse: PlayListModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setPlayListMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = PlayListModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setPlayListMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<PlayListModel>, t: Throwable) {
                    val model = PlayListModel()
                    model.message = "Connect Spotify First"
                    model.error = "Yes"
                    setPlayListMainModelData(model)
                    setIsLoading(false)
                }
            })
    }

    // ================PlayListItems Main =========================
    var playListItemsMutableLiveData: SingleLiveEvent<PlayListItemsModel?> = SingleLiveEvent()
    val playListItemsModelData: SingleLiveEvent<PlayListItemsModel?>
        get() = playListItemsMutableLiveData

    private fun setPlayListItemsMainModelData(mainModel: PlayListItemsModel?) {
        setIsLoading(false)
        playListItemsMutableLiveData.postValue(mainModel)
    }

    fun loadPlayListItemsData(accessToken: String, playListId: String) {
        setIsLoading(true)
        // Authorization": 'Bearer
        instance?.spotifyApis?.getPlayListItemsData(""+accessToken, ""+playListId)
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
                        // model.message = "Internal Server Error"
                        // model.error = "Yes"
                        setPlayListItemsMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<PlayListItemsModel>, t: Throwable) {
                    val model = PlayListItemsModel()
                    // model.message = "Failure"
                    // model.error = "Yes"
                    setPlayListItemsMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
// ================Preferences Main =========================

}






