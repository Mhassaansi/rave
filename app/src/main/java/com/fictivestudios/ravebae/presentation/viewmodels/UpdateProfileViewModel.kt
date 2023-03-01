package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.models.RegisterModel
import com.fictivestudios.ravebae.utils.LoginModel
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


class UpdateProfileViewModel(override var activity: Activity) : BaseViewModel(activity) {
    private var updateProfileModelMutableLiveData: SingleLiveEvent<LoginModel?> = SingleLiveEvent()
    val updateProfileModelData: SingleLiveEvent<LoginModel?>
        get() = updateProfileModelMutableLiveData

    private fun setUpdateProfileMainModelData(mainModel: LoginModel?) {
        setIsLoading(false)
        updateProfileModelMutableLiveData.postValue(mainModel)
    }

    fun loadProfileData(
        username: String?,
        user_image: MultipartBody.Part?,
        cover_image: MultipartBody.Part?,
        user_photo1: MultipartBody.Part?,
        user_photo2: MultipartBody.Part?,
        user_photo3: MultipartBody.Part?,
        user_photo4: MultipartBody.Part?,
        user_photo5: MultipartBody.Part?,
        user_photo6: MultipartBody.Part?,
        user_photo7: MultipartBody.Part?,
        user_photo8: MultipartBody.Part?,
        phone_number: String?,
        user_gender: String?,
        user_description: String?
    ) {
        setIsLoading(true)

        val name = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            username!!
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

        Log.i("Register", "Image " + user_image)


//        var imagenPerfil: MultipartBody.Part? = null
//        if (user_image != null) {
//            val file = File(user_image)
//            Log.i("Register", "Nombre del archivo " + file.name)
//            // create RequestBody instance from file
//            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//            // MultipartBody.Part is used to send also the actual file name
//            imagenPerfil =
//                MultipartBody.Part.createFormData("user_image", file.name, requestFile)
//        }

        var imagenPerfil1: MultipartBody.Part? = null
//        if (cover_image != null) {
//            val file = File(cover_image)
//            Log.i("Register", "Nombre del archivo " + file.name)
//            // create RequestBody instance from file
//            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//            // MultipartBody.Part is used to send also the actual file name
//            imagenPerfil1 =
//                MultipartBody.Part.createFormData("cover_image", file.name, requestFile)
//        }

        instance?.apis?.getUpdateProfileData(
            ""+ConfigurationPref(activity).token,
            ""+ConfigurationPref(activity).userId,
            name,
            phone_numberr,
            user_genderr,
            user_descriptionn,
            user_image,
            cover_image,
            user_photo1,
            user_photo2,
            user_photo3,
            user_photo4,
            user_photo5,
            user_photo6,
            user_photo7,
            user_photo8
            )
            ?.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    setIsLoading(false)
                    try {
                        if (response.isSuccessful) {
                            setUpdateProfileMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<LoginModel>() {}.type
                            val errorResponse: LoginModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            errorResponse?.error = "no"

                            setUpdateProfileMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }

                    } catch (e: Exception) {
                        val model = LoginModel()
                        model.message = "Internal Server Error"
                        model.error = "Yes"
                        setUpdateProfileMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    val model = LoginModel()
                    model.error = "Yes"
                    model.message = "Update Failed"
                    setUpdateProfileMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
// ================ UpdateProfile Main =========================

}






