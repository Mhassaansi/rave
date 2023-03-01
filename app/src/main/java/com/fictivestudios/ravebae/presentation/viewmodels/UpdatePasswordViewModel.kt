package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.models.RegisterModel
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class UpdatePasswordViewModel(override var activity: Activity) : BaseViewModel(activity) {
    var updatePasswordModelMutableLiveData: SingleLiveEvent<LoginModel?> = SingleLiveEvent()
    val updatePasswordModelData: SingleLiveEvent<LoginModel?>
        get() = updatePasswordModelMutableLiveData

    private fun setUpdatePasswordMainModelData(mainModel: LoginModel?) {
        setIsLoading(false)
        updatePasswordModelMutableLiveData.postValue(mainModel)
    }

    fun loadChangePasswordData(data: HashMap<String, String>) {
        setIsLoading(true)
        instance?.apis?.getUpdatePasswordData(data)
            ?.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setUpdatePasswordMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<LoginModel>() {}.type
                            val errorResponse: LoginModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "no"
                            // errorResponse?.message = "Internal Server Error"

                            setUpdatePasswordMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = LoginModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setUpdatePasswordMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    val model = LoginModel()
                    model.error = "Yes"
                    setUpdatePasswordMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
// ================UpdatePassword Main =========================

}






