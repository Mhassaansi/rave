package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.presentation.models.ContentModel
import com.fictivestudios.ravebae.presentation.models.RegisterModel
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class TermsAndConditionViewModel(override var activity: Activity) : BaseViewModel(activity) {
    var termsAndConditionModelMutableLiveData: SingleLiveEvent<ContentModel?> = SingleLiveEvent()
    val termsAndConditionModelData: SingleLiveEvent<ContentModel?>
        get() = termsAndConditionModelMutableLiveData

    private fun setTermsAndConditionMainModelData(mainModel: ContentModel?) {
        setIsLoading(false)
        termsAndConditionModelMutableLiveData.postValue(mainModel)
    }

    fun loadTermsAndConditionData() {
        setIsLoading(true)
        instance?.apis?.getContentTermsAndConditionData()
            ?.enqueue(object : Callback<ContentModel> {
                override fun onResponse(
                    call: Call<ContentModel>,
                    response: Response<ContentModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setTermsAndConditionMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<ContentModel>() {}.type
                            val errorResponse: ContentModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "Yes"
                            // errorResponse?.message = "Internal Server Error"

                            setTermsAndConditionMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
//                        val model = ContentModel()
//                        model.message = "Internal Server Error"
//                        model.error = "Yes"
//                        setTermsAndConditionMainModelData(model)
                        Toast.makeText(activity, "Internal Server Error", Toast.LENGTH_LONG).show()
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<ContentModel>, t: Throwable) {
//                    val model = ContentModel()
//                    model.error = "Yes"
//                    setTermsAndConditionMainModelData(model)
                    Toast.makeText(activity, "Failure", Toast.LENGTH_LONG).show()
                    setIsLoading(false)
                }
            })
    }
// ================Terms And Condition Main =========================

}






