package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.models.RegisterModel
import com.fictivestudios.ravebae.presentation.models.UserId
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class SettingsViewModel(override var activity: Activity) : BaseViewModel(activity) {
    var logoutModelMutableLiveData: SingleLiveEvent<LoginModel?> = SingleLiveEvent()
    val logoutModelData: SingleLiveEvent<LoginModel?>
        get() = logoutModelMutableLiveData

    private fun setLogoutMainModelData(mainModel: LoginModel?) {
        setIsLoading(false)
        logoutModelMutableLiveData.postValue(mainModel)
    }

    fun loadLogoutData(data: HashMap<String, String>) {
        setIsLoading(true)
        instance?.apis?.getLogoutData(""+ConfigurationPref(activity).token,data)
            ?.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    setIsLoading(false)
                    try {

                        if (response.isSuccessful) {
                            setLogoutMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<LoginModel>() {}.type
                            val errorResponse: LoginModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setLogoutMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = LoginModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setLogoutMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    val model = LoginModel()
                    model.error = "Yes"
                    setLogoutMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
// ================Login Main =========================

    var deleteProfileModelMutableLiveData: SingleLiveEvent<LoginModel?> = SingleLiveEvent()
    val deleteProfileModelData: SingleLiveEvent<LoginModel?>
        get() = deleteProfileModelMutableLiveData

    private fun setDeleteMainModelData(mainModel: LoginModel?) {
        setIsLoading(false)
        deleteProfileModelMutableLiveData.postValue(mainModel)
    }

    fun loadDeleteProfileData(userId: String) {
        setIsLoading(true)
        instance?.apis?.deleteUserProfile(""+ConfigurationPref(activity).token, userId)
            ?.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    setIsLoading(false)
                    try {

                        if (response.isSuccessful) {
                            setDeleteMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<LoginModel>() {}.type
                            val errorResponse: LoginModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setDeleteMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = LoginModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setDeleteMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    val model = LoginModel()
                    model.error = "Yes"
                    setDeleteMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
// ================Delete Profile Main =========================



    // ================ Notification Main =========================
var notificationModelMutableLiveData: SingleLiveEvent<LoginModel?> = SingleLiveEvent()
    val notificationModelData: SingleLiveEvent<LoginModel?>
        get() = notificationModelMutableLiveData

    private fun setNotificationMainModelData(mainModel: LoginModel?) {
        setIsLoading(false)
        notificationModelMutableLiveData.postValue(mainModel)
    }

    fun loadNotificationData(data: HashMap<String, String>) {
        setIsLoading(true)
        instance?.apis?.getNotificationData(""+ConfigurationPref(activity).token,data)
            ?.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    setIsLoading(false)
                    try {

                        if (response.isSuccessful) {
                            setNotificationMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<LoginModel>() {}.type
                            val errorResponse: LoginModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setNotificationMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = LoginModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setNotificationMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    val model = LoginModel()
                    model.error = "Yes"
                    setNotificationMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
}






