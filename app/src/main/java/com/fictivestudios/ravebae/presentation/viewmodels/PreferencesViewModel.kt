package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.presentation.models.PlayListItemsModel
import com.fictivestudios.ravebae.presentation.models.PlayListModel
import com.fictivestudios.ravebae.presentation.models.RegisterModel
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

// Preference Model Already Exists transfering data to it..
class PreferencesViewModel(override var activity: Activity) : BaseViewModel(activity) {
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
        instance?.spotifyApis?.getPlayListData("bearer " + accessToken)
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
                            val type = object : TypeToken<LoginModel>() {}.type
                            val errorResponse: PlayListModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setPlayListMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = PlayListModel()
                        // model.message = "Internal Server Error"
                        model.error = "Yes"
                        setPlayListMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<PlayListModel>, t: Throwable) {
                    val model = PlayListModel()
                    // model.message = "Failure"
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






