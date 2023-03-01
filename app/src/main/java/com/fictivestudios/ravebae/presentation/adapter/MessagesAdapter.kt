package com.fictivestudios.ravebae.presentation.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.models.Datac
import com.fictivestudios.ravebae.utils.Constants
import com.fictivestudios.ravebae.utils.ImageLoadUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_messages.view.*
import kotlinx.android.synthetic.main.item_received.view.iv_user

class MessagesAdapter(dataList: List<Datac>?, val activity: Activity) :
    RecyclerView.Adapter<MessagesAdapter.ProfileViewHolder>() {

    private var users: List<Datac>? = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_messages, parent, false)

        return ProfileViewHolder(view)


    }


    override fun getItemCount() = users?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val recieverId: String = users?.get(position)?.recieverId?.Id.toString()

        val recieverImageURL: String =
            "" + Constants.imageURL + "" + users?.get(position)?.recieverId?.userImage
        val senderImageURL: String =
            "" + Constants.imageURL + "" + users?.get(position)?.senderId?.userImage


        if (recieverId != ConfigurationPref(activity).userId) {
            holder.itemView.tv_name.text = "" + users?.get(position)?.recieverId?.username
            holder.itemView.tv_message.text = "" + users?.get(position)?.recieverId?.userDescription
            recieverImageURL.let {
                if (it != Constants.imageURL) {
                    ImageLoadUtil.loadImageNotFit(it, holder.itemView.iv_user, R.drawable.user_dp_placeholder)
                }
            }
        } else {
            holder.itemView.tv_name.text = "" + users?.get(position)?.senderId?.username
            holder.itemView.tv_message.text = "" + users?.get(position)?.senderId?.userDescription
            senderImageURL.let {
                if (it != Constants.imageURL) {
                    ImageLoadUtil.loadImageNotFit(it, holder.itemView.iv_user, R.drawable.user_dp_placeholder)
                }
            }
        }


        holder.itemView.iv_user.setOnClickListener {
            val gson = Gson()
            val json: String = gson.toJson(users?.get(position))
            var id=""
            if(recieverId != ConfigurationPref(activity).userId){
                id=recieverId
                Log.e("sfjjdff","rec"+id)

            }
            else{
                id= users?.get(position)!!.senderId!!.Id.toString()
                Log.e("sfjjdff","sen"+id)

            }

            val bundle = Bundle()
            bundle.putString("userprofile", "" + json)
            bundle.putString("from", "messages") // messages
            bundle.putString("userId", "" + id)    // userId
            MainActivity.getMainActivity
                ?.navController?.navigate(R.id.userProfileFragment, bundle)
        }

//        recieverId = this.requireArguments().getString("chatreceiverid")
//        userImageUrl = this.requireArguments().getString("chatsenderphotourl")
//        userName = this.requireArguments().getString("chatusername")


        holder.itemView.setOnClickListener {
            if (recieverId != ConfigurationPref(activity).userId) {
//                bundle.putString("chatreceiverid", "" + users?.get(position)?.recieverId?.Id)
//                bundle.putString("chatsenderphotourl", "" + recieverImageURL)
//                bundle.putString("chatusername", "" + users?.get(position)?.recieverId?.username)

                ConfigurationPref(activity).chatReceiverId =
                    "" + users?.get(position)?.recieverId?.Id
                ConfigurationPref(activity).chatSenderPhotoUrl = "" + recieverImageURL
                ConfigurationPref(activity).chatUserName =
                    "" + users?.get(position)?.recieverId?.username

                ConfigurationPref(activity).conversationUserId = "" + users?.get(position)?.recieverId?.Id

                Log.d("chat","chatReceiverId:" + ConfigurationPref(activity).chatReceiverId + "chatSenderId:" + ConfigurationPref(activity).userId)
            //                ConfigurationPref(activity).chatReceiverId =
//                    "" + users?.get(position)?.senderId?.Id
            } else {
//                bundle.putString("chatreceiverid", "" + users?.get(position)?.senderId?.Id)
//                bundle.putString("chatsenderphotourl", "" + senderImageURL)
//                bundle.putString("chatusername", "" + users?.get(position)?.senderId?.username)

                // Replacing else cases with senderId
                ConfigurationPref(activity).chatReceiverId =
                    "" + users?.get(position)?.senderId?.Id
                ConfigurationPref(activity).chatSenderPhotoUrl = "" + senderImageURL
                ConfigurationPref(activity).chatUserName =
                    "" + users?.get(position)?.senderId?.username

                ConfigurationPref(activity).conversationUserId = "" + users?.get(position)?.Id

//                ConfigurationPref(activity).chatReceiverId =
//                    "" + users?.get(position)?.senderId?.Id
                Log.d("chat","chatReceiverId:" + ConfigurationPref(activity).chatReceiverId + "chatSenderId:" + ConfigurationPref(activity).userId)
            }

            MainActivity.getMainActivity
                ?.navController?.navigate(R.id.conversationFragment)

        }
    }

    fun setProfiles(profiles: List<ContactsContract.Profile>) {
        /* this.profiles = profiles
         notifyDataSetChanged()*/
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}