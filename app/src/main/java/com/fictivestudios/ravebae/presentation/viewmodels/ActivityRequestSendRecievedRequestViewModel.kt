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

class ActivityRequestSendRecievedRequestViewModel(override var activity: Activity) :
    BaseViewModel(activity) {
    var sendRequestModelMutableLiveData: SingleLiveEvent<SendRequestModel?> = SingleLiveEvent()
    val sendRequestModelData: SingleLiveEvent<SendRequestModel?>
        get() = sendRequestModelMutableLiveData

    var requestRecievedViewModelMutableLiveData: SingleLiveEvent<RequestModel?> = SingleLiveEvent()
    val requestRecievedViewModelData: SingleLiveEvent<RequestModel?>
        get() = requestRecievedViewModelMutableLiveData

    var acceptRejectModelMutableLiveData: SingleLiveEvent<LoginModel?> = SingleLiveEvent()
    val acceptRejectModelData: SingleLiveEvent<LoginModel?>
        get() = acceptRejectModelMutableLiveData


    private fun setSendRequestMainModelData(mainModel: SendRequestModel?) {
        setIsLoading(false)
        sendRequestModelMutableLiveData.postValue(mainModel)
    }

    fun loadSendRequestData() {
        setIsLoading(true)
        instance?.apis?.getSendRequestData("" + ConfigurationPref(activity).token)
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

    private fun setRequestRecievedMainModelData(mainModel: RequestModel?) {
        setIsLoading(false)
        requestRecievedViewModelMutableLiveData.postValue(mainModel)
    }

    fun loadRequestRecievedData() {
        setIsLoading(true)
        instance?.apis?.getRecievedRequestData("" + ConfigurationPref(activity).token)
            ?.enqueue(object : Callback<RequestModel> {
                override fun onResponse(
                    call: Call<RequestModel>,
                    response: Response<RequestModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setRequestRecievedMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<RequestModel>() {}.type
                            val errorResponse: RequestModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setRequestRecievedMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = RequestModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setRequestRecievedMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<RequestModel>, t: Throwable) {
                    val model = RequestModel()
                    model.error = "Yes"
                    setRequestRecievedMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
// ================RecievedRequestViewModel Main =========================

    private fun setAcceptRejectMainModelData(mainModel: LoginModel?) {
        setIsLoading(false)
        acceptRejectModelMutableLiveData.postValue(mainModel)
    }

    fun loadAcceptRejectData(data: HashMap<String, String>) {
        setIsLoading(true)
        instance?.apis?.getAcceptRejectFriendRequestData(""+ConfigurationPref(activity).token,data)
            ?.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setAcceptRejectMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<LoginModel>() {}.type
                            val errorResponse: LoginModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setAcceptRejectMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = LoginModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setAcceptRejectMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    val model = LoginModel()
                    model.error = "Yes"
                    setAcceptRejectMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
// ================AcceptRejectViewModel Main =========================

}






