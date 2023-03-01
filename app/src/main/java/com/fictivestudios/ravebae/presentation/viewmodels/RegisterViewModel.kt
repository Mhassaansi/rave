package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.presentation.models.RegisterModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RegisterViewModel(override var activity: Activity) : BaseViewModel(activity) {
    private var registerModelMutableLiveData: SingleLiveEvent<RegisterModel?> = SingleLiveEvent()
    val registerModelData: SingleLiveEvent<RegisterModel?>
        get() = registerModelMutableLiveData

    private fun setRegisterMainModelData(mainModel: RegisterModel?) {
        setIsLoading(false)
        registerModelMutableLiveData.postValue(mainModel)
    }

    fun loadRegisterData(
        username: String?,
        email: String?,
        user_image: MultipartBody.Part?,
        password: String?,
        phone_number: String?,
        user_gender: String?,
        user_description: String?,
        device_type: String?,
        token: String?
    ) {
        setIsLoading(true)

        val name = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            username!!
        )
        val emailAddress = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            email!!
        )
        val passwordd = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            password!!
        )
        val phone_numberr = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            phone_number!!
        )
        val user_genderr = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            user_gender!!
        )
        val user_descriptionn = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            user_description!!
        )

        val device_typeee = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            device_type!!
        )

        val tokenn = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            token!!
        )

        instance?.apis?.getRegisterData(
            name,
            emailAddress,
            passwordd,
            phone_numberr,
            user_genderr,
            user_descriptionn,
            user_image,
            device_typeee,
            tokenn
        )
            ?.enqueue(object : Callback<RegisterModel> {
                override fun onResponse(
                    call: Call<RegisterModel>,
                    response: Response<RegisterModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setRegisterMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<RegisterModel>() {}.type
                            val errorResponse: RegisterModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "no"

                            setRegisterMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }

                    } catch (e: Exception) {
                        val model = RegisterModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setRegisterMainModelData(model)
                        setIsLoading(false)
                        Log.e("RegisterCatch",e.printStackTrace().toString())
                    }
                }

                override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                    val model = RegisterModel()
                    model.error = "Yes"
                    model.message = "Failure"
                    setRegisterMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
// ================Register Main =========================

}






