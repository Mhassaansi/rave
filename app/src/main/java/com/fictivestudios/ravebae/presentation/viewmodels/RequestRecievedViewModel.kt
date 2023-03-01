package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.models.RequestModel
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class RequestRecievedViewModel(override var activity: Activity) : BaseViewModel(activity) {
    var requestRecievedViewModelMutableLiveData: MutableLiveData<RequestModel?> = MutableLiveData()
    val requestRecievedViewModelData: MutableLiveData<RequestModel?>
        get() = requestRecievedViewModelMutableLiveData

    private fun setRequestRecievedMainModelData(mainModel: RequestModel?) {
        setIsLoading(false)
        requestRecievedViewModelMutableLiveData.postValue(mainModel)
    }

    fun loadRequestRecievedData() {
        setIsLoading(true)
        instance?.apis?.getRecievedRequestData(""+ConfigurationPref(activity).token)
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
// ================SendRequestViewModel Main =========================

}






