package com.fictivestudios.ravebae.presentation.viewmodels


import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

open class BaseViewModel(activity: Activity) : ViewModel() {
    var api: ApiClassRetrofit? = instance
    open var activity: Activity = activity
    var loadingStatus: MutableLiveData<Boolean> = MutableLiveData()

//    var loginModel: MutableLiveData<LoginModel?> = MutableLiveData()
//    var userData: MutableLiveData<LoginModel?> = MutableLiveData()

    // ===============================================================================
    // var OTP: MutableLiveData<GetOTPModel> = MutableLiveData()

    //===============================================================================
    // var verifyOtp: MutableLiveData<GetOTPModel> = MutableLiveData()

    //=======================================================
    fun setIsLoading(loading: Boolean) {
        loadingStatus.postValue(loading)
    }

//    private fun setLoginModel(model: LoginModel) {
//        setIsLoading(false)
//        loginModel.postValue(model)
//    }

//    private fun setUserModel(model: LoginModel) {
//        setIsLoading(false)
//        userData.postValue(model)
//    }

//    private fun setOTP(model: GetOTPModel) {
//        setIsLoading(false)
//        OTP.postValue(model)
//    }

//    fun loadGetOtp(map: HashMap<String, String>) {
//        setIsLoading(true)
//        instance?.apis?.getOTP(ConfigurationPref(activity).token, map)
//            ?.enqueue(object : Callback<EncryptModel> {
//                override fun onResponse(
//                    call: Call<EncryptModel>,
//                    response: Response<EncryptModel>
//                ) {
//                    try {
//                        setIsLoading(false)
//                        if (response.isSuccessful) {
//                            val homeSliderResponse = JSONObject(
//                                FrontEngine.instance?.newCheckEncrypt(
//                                    response.body()?.geteData()
//                                )
//                            )
//                            val model = Gson().fromJson(
//                                homeSliderResponse.toString(),
//                                GetOTPModel::class.java
//                            )
//                            LoggerSSGC.printObjectLog("loadGetOtp==", model.data)
//                            setOTP(model)
//                            //                        loadHomeFavProgramData();
//                        } else {
//                            val model = GetOTPModel()
//                            model.code = response.code()
//                            model.error = "Yes"
//                            model.code = response.code()
//                            setOTP(model)
//                            Log.e(
//                                "else isSuccess--", " Error-" + response.errorBody()
//                                        + " body-" + response.body() + " code-" + response.code() + " message-" + response.message()
//                            )
//                        }
//                    } catch (e: Exception) {
//                        val model = GetOTPModel()
//                        model.code = 500
//                        model.error = "Yes"
//                        model.code = response.code()
//                        setOTP(model)
//                    }
//                }
//
//                override fun onFailure(call: Call<EncryptModel>, t: Throwable) {
//                    LoggerSSGC.printErrorLog("loadGetOtp==", t.toString())
//                    val model = GetOTPModel()
//                    model.code = 500
//                    model.error = "Yes"
//                    setOTP(model)
//                    setIsLoading(false)
//                }
//            })
//    }

//    private fun setVerifyOTP(model: GetOTPModel) {
//        setIsLoading(false)
//        verifyOtp.postValue(model)
//    }
//
//    fun verifyOTP(map: HashMap<String, String>) {
//        setIsLoading(true)
//        instance?.apis?.verifyOTP(ConfigurationPref(activity).token, map)
//            ?.enqueue(object : Callback<EncryptModel> {
//                override fun onResponse(
//                    call: Call<EncryptModel>,
//                    response: Response<EncryptModel>
//                ) {
//                    try {
//                        setIsLoading(false)
//                        if (response.isSuccessful) {
//                            val homeSliderResponse = JSONObject(
//                                FrontEngine.instance?.newCheckEncrypt(
//                                    response.body()?.geteData()
//                                )
//                            )
//                            val model = Gson().fromJson(
//                                homeSliderResponse.toString(),
//                                GetOTPModel::class.java
//                            )
//                            LoggerSSGC.printObjectLog("verifyOTP==", model.data)
//                            setVerifyOTP(model)
//                            //                        loadHomeFavProgramData();
//                        } else {
//                            val model = GetOTPModel()
//                            model.code = response.code()
//                            model.error = "Yes"
//                            model.code = response.code()
//                            setVerifyOTP(model)
//                            Log.e(
//                                "else isSuccess--", " Error-" + response.errorBody()
//                                        + " body-" + response.body() + " code-" + response.code() + " message-" + response.message()
//                            )
//                        }
//                    } catch (e: Exception) {
//                        val model = GetOTPModel()
//                        model.code = response.code()
//                        model.error = "Yes"
//                        model.code = response.code()
//                        setVerifyOTP(model)
//                    }
//                }
//
//                override fun onFailure(call: Call<EncryptModel>, t: Throwable) {
//                    LoggerSSGC.printErrorLog("verifyOTP==", t.toString())
//                    val model = GetOTPModel()
//                    model.code = 500
//                    model.error = "Yes"
//                    setVerifyOTP(model)
//                    setIsLoading(false)
//                }
//            })
//    }

//    fun deleteAccount(data: HashMap<String, String>) {
//        setIsLoading(true)
//        instance?.apis?.deleteAccount(ConfigurationPref(activity).token, data)
//            ?.enqueue(object : Callback<EncryptModel>{
//                override fun onResponse(
//                    call: Call<EncryptModel>,
//                    response: Response<EncryptModel>
//                ) {
//                    try {
//                        setIsLoading(false)
//                        if (response.isSuccessful) {
//                            val homeSliderResponse = JSONObject(
//                                FrontEngine.instance?.newCheckEncrypt(
//                                    response.body()?.geteData()
//                                )
//                            )
//                            val model =
//                                Gson().fromJson(homeSliderResponse.toString(), LoginModel::class.java)
//                            if (model.status!!) {
//                                LoggerSSGC.printObjectLog("Account==", model.data)
//                                if (model.code == 200) model.encryptedData =
//                                    response.body()?.geteData()
//                            }
//                            setLoginModel(model)
//                            //                        loadHomeFavProgramData();
//                        } else {
//                            val model = LoginModel()
//                            model.code = response.code()
//                            model.error = "Yes"
//                            model.code = response.code()
//                            setLoginModel(model)
//                            Log.e(
//                                "else isSuccess--", " Error-" + response.errorBody()
//                                        + " body-" + response.body() + " code-" + response.code() + " message-" + response.message()
//                            )
//                        }
//                    } catch (e: Exception) {
//                        LoggerSSGC.printObjectLog(
//                            "Account==", FrontEngine.instance?.newCheckEncrypt(
//                                response.body()?.geteData().toString()
//                            )
//                        )
//                        val model = LoginModel()
//                        model.code = response.code()
//                        model.error = "Yes"
//                        model.message = "something went wrong"
//                        model.code = response.code()
//                        setLoginModel(model)
//                        Log.e(
//                            "else isSuccess--", " Error-" + response.errorBody()
//                                    + " body-" + response.body() + " code-" + response.code() + " message-" + response.message()
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<EncryptModel>, t: Throwable) {
//                    LoggerSSGC.printErrorLog("MyBill==", t.toString())
//                    val model = LoginModel()
//                    model.code = 500
//                    model.error = "Yes"
//                    setLoginModel(model)
//                    setIsLoading(false)
//                }
//
//            })
//    }

//    fun getUser(data: HashMap<String, String>) {
//        setIsLoading(true)
//        instance?.apis?.getUser(ConfigurationPref(activity).token, data)
//            ?.enqueue(object : Callback<EncryptModel>{
//                override fun onResponse(
//                    call: Call<EncryptModel>,
//                    response: Response<EncryptModel>
//                ) {
//                    try {
//                        setIsLoading(false)
//                        if (response.isSuccessful) {
//                            val homeSliderResponse = JSONObject(
//                                FrontEngine.instance?.newCheckEncrypt(
//                                    response.body()?.geteData()
//                                )
//                            )
//                            val model =
//                                Gson().fromJson(homeSliderResponse.toString(), LoginModel::class.java)
//                            if (model.status == true) {
//                                LoggerSSGC.printObjectLog("Account==", model.code)
//                                LoggerSSGC.printObjectLog("Account==", model.status)
//                                LoggerSSGC.printObjectLog("Account==", model.data)
//                                if (model.code == 200) model.encryptedData =
//                                    response.body()?.geteData()
//                            }
//                            setUserModel(model)
//                            //                        loadHomeFavProgramData();
//                        } else {
//                            val model = LoginModel()
//                            model.code = response.code()
//                            model.error = "Yes"
//                            model.code = response.code()
//                            setUserModel(model)
//                            Log.e(
//                                "else isSuccess--", " Error-" + response.errorBody()
//                                        + " body-" + response.body() + " code-" + response.code() + " message-" + response.message()
//                            )
//                        }
//                    } catch (e: Exception) {
//                        LoggerSSGC.printObjectLog(
//                            "Account==", FrontEngine.instance?.newCheckEncrypt(
//                                response.body()?.geteData().toString()
//                            )
//                        )
//                        val model = LoginModel()
//                        model.code = response.code()
//                        model.error = "Yes"
//                        model.message = "something went wrong"
//                        model.code = response.code()
//                        setUserModel(model)
//                        Log.e(
//                            "else isSuccess--", " Error-" + response.errorBody()
//                                    + " body-" + response.body() + " code-" + response.code() + " message-" + response.message()
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<EncryptModel>, t: Throwable) {
//                    LoggerSSGC.printErrorLog("MyBill==", t.toString())
//                    val model = LoginModel()
//                    model.code = 500
//                    model.error = "Yes"
//                    setUserModel(model)
//                    setIsLoading(false)
//                }
//
//            })
//    }
}