package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.models.ContentModel
import com.fictivestudios.ravebae.presentation.models.MatchListModel
import com.fictivestudios.ravebae.presentation.models.RegisterModel
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class DiscoverViewModel(override var activity: Activity) : BaseViewModel(activity) {
    var discoverModelMutableLiveData: SingleLiveEvent<MatchListModel?> = SingleLiveEvent()
    val DiscoverModelData: SingleLiveEvent<MatchListModel?>
        get() = discoverModelMutableLiveData

    var addFriendModelMutableLiveData: SingleLiveEvent<LoginModel?> = SingleLiveEvent()
    val addFriendModelData: SingleLiveEvent<LoginModel?>
        get() = addFriendModelMutableLiveData

    private fun setDiscoverMainModelData(mainModel: MatchListModel?) {
        setIsLoading(false)
        discoverModelMutableLiveData.postValue(mainModel)
    }

    fun loadDiscoverData() {
        setIsLoading(true)
        instance?.apis?.getMatchListData("" + ConfigurationPref(activity).token)
            ?.enqueue(object : Callback<MatchListModel> {
                override fun onResponse(
                    call: Call<MatchListModel>,
                    response: Response<MatchListModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setDiscoverMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<MatchListModel>() {}.type
                            val errorResponse: MatchListModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setDiscoverMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = MatchListModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setDiscoverMainModelData(model)
                        Toast.makeText(activity, "Internal Server Error", Toast.LENGTH_LONG).show()
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<MatchListModel>, t: Throwable) {
                    val model = MatchListModel()
                    model.error = "Yes"
                    model.message = "Failure"
                    setDiscoverMainModelData(model)
                    Toast.makeText(activity, "Failure", Toast.LENGTH_LONG).show()
                    setIsLoading(false)
                }
            })
    }

// ================Discover Main =========================

// ================ Add Friend =========================

    private fun setAddFriendMainModelData(mainModel: LoginModel?) {
        setIsLoading(false)
        addFriendModelMutableLiveData.postValue(mainModel)
    }

    fun loadAddFriendData(receiverId: String?, requestStatus: Int?, isHeart: Int?) {
        setIsLoading(true)
        instance?.apis?.getAddFriendData("" + ConfigurationPref(activity).token,
                                            receiverId, requestStatus, isHeart)
            ?.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setAddFriendMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<LoginModel>() {}.type
                            val errorResponse: LoginModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setAddFriendMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = LoginModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setAddFriendMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    val model = LoginModel()
                    model.error = "Yes"
                    setAddFriendMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
}






