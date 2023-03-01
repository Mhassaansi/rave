package com.fictivestudios.ravebae.presentation.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fictivestudios.ravebae.core.networking.ApiClassRetrofit.Companion.instance
import com.fictivestudios.ravebae.core.networking.SingleLiveEvent
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.models.FriendListModel
import com.fictivestudios.ravebae.presentation.models.RegisterModel
import com.fictivestudios.ravebae.utils.LoginModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class MessagesViewModel(override var activity: Activity) : BaseViewModel(activity) {
    var messagesModelMutableLiveData: SingleLiveEvent<FriendListModel?> = SingleLiveEvent()
    val messagesModelData: SingleLiveEvent<FriendListModel?>
        get() = messagesModelMutableLiveData

    private fun setMessagesMainModelData(mainModel: FriendListModel?) {
        setIsLoading(false)
        messagesModelMutableLiveData.postValue(mainModel)
    }

    fun loadFriendsListData() {
        setIsLoading(true)
        instance?.apis?.getFriendsListData(""+ConfigurationPref(activity).token)
            ?.enqueue(object : Callback<FriendListModel> {
                override fun onResponse(
                    call: Call<FriendListModel>,
                    response: Response<FriendListModel>
                ) {
                    setIsLoading(false)
                    try {

                        if (response.isSuccessful) {
                            setMessagesMainModelData(response.body())
                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<FriendListModel>() {}.type
                            val errorResponse: FriendListModel? =
                                gson.fromJson(response.errorBody()!!.charStream(), type)

                            // errorResponse?.error = "Yes"
                          //  errorResponse?.message = "Internal Server Error"

                            setMessagesMainModelData(errorResponse)
                            // Toast.makeText(activity, "" + errorResponse?.message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        val model = FriendListModel()
                        model.message = "Internal Server Error"
                        // model.error = "Yes"
                        setMessagesMainModelData(model)
                        setIsLoading(false)
                    }
                }

                override fun onFailure(call: Call<FriendListModel>, t: Throwable) {
                    val model = FriendListModel()
                    // model.error = "Yes"
                    model.message = "Failure"
                    setMessagesMainModelData(model)
                    setIsLoading(false)
                }
            })
    }
// ================FriendList Main =========================

}






