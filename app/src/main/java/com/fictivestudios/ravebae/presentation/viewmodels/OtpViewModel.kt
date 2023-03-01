package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.presentation.models.RegisterModel
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class OtpViewModel(override var activity: Activity) : BaseViewModel(activity) {
    var OtpModelMutableLiveData: SingleLiveEvent<LoginModel?> = SingleLiveEvent()
    val OtpModelData: SingleLiveEvent<LoginModel?>
        get() = OtpModelMutableLiveData

    var OtpResendModelMutableLiveData: MutableLiveData<LoginModel?> = MutableLiveData()
    val OtpResendModelData: MutableLiveData<LoginModel?>
        get() = OtpResendModelMutableLiveData

    private fun setOtpMainModelData(mainModel: LoginModel?) {
        setIsLoading(false)
        OtpModelMutableLiveData.postValue(mainModel)
    }

    private fun setOtpResendMainModelData(mainModel: LoginModel?) {
        setIsLoading(false)
        OtpResendModelMutableLiveData.postValue(mainModel)
    }

    fun loadOtpData(data: HashMap<String, String>) {
        setIsLoading(true)
        instance?.apis?.getOtpData(data)
            ?.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setOtpMainModelData(response.body())
                        } else {
                            val model = LoginModel()
                            model.error = "Yes"
                            model.message = "Internal Server Error"
                            setOtpMainModelData(model)
                            setIsLoading(false)
                            Log.e(
                                "else isSuccess--", " Error-" + response.errorBody()
                                        + " body-" + response.body() + " code-" + response.code() + " message-" + response.message()
                            )
                        }
                    } catch (e: Exception) {
                        val model = LoginModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setOtpMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    val model = LoginModel()
                    model.error = "Yes"
                    setOtpMainModelData(model)
                    setIsLoading(false)
                }
            })
    }

    fun loadResendOtpData(data: HashMap<String, String>) {
        setIsLoading(true)
        instance?.apis?.getResendOtpData(data)
            ?.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setOtpResendMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<RegisterModel>() {}.type
                            val errorResponse: LoginModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "no"

                            setOtpResendMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = LoginModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setOtpResendMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    val model = LoginModel()
                    model.error = "Yes"
                    setOtpResendMainModelData(model)
                    setIsLoading(false)
                }
            })
    }

// ================Otp Main =========================

}






