package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.models.RequestModel
import com.fictivestudios.ravebae.presentation.models.SendRequestModel
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class SendRequestViewModel(override var activity: Activity) : BaseViewModel(activity) {
    var sendRequestModelMutableLiveData: SingleLiveEvent<SendRequestModel?> = SingleLiveEvent()
    val sendRequestModelData: SingleLiveEvent<SendRequestModel?>
        get() = sendRequestModelMutableLiveData

    private fun setSendRequestMainModelData(mainModel: SendRequestModel?) {
        setIsLoading(false)
        sendRequestModelMutableLiveData.postValue(mainModel)
    }

    fun loadSendRequestData() {
        setIsLoading(true)
        instance?.apis?.getSendRequestData(""+ConfigurationPref(activity).token)
            ?.enqueue(object : Callback<SendRequestModel> {
                override fun onResponse(
                    call: Call<SendRequestModel>,
                    response: Response<SendRequestModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setSendRequestMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<SendRequestModel>() {}.type
                            val errorResponse: SendRequestModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            // errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setSendRequestMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = SendRequestModel()
                        model.message = "Internal Server Error"
                        // model.error = "Yes"
                        setSendRequestMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<SendRequestModel>, t: Throwable) {
                    val model = SendRequestModel()
                    // model.error = "Yes"
                    setSendRequestMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
// ================SendRequestViewModel Main =========================

}






