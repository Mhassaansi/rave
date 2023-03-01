package com.fictivestudios.ravebae.presentation.adapter

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
import com.fictivestudios.ravebae.presentation.models.Chat
import com.fictivestudios.ravebae.presentation.models.ChatRecieveModel
import com.fictivestudios.ravebae.presentation.models.Datam
import com.fictivestudios.ravebae.utils.Constants
import com.fictivestudios.ravebae.utils.ImageLoadUtil
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import kotlinx.android.synthetic.main.item_message_received.view.*
import kotlinx.android.synthetic.main.item_message_sent.view.*
import kotlinx.android.synthetic.main.item_messages.view.*

import kotlinx.android.synthetic.main.item_received.view.iv_user

// users: List<Chat>
class ConversationAdapter(users: List<ChatRecieveModel>, val activity: Activity) :
    RecyclerView.Adapter<ConversationAdapter.ProfileViewHolder>() {

    // users
    private var users: List<ChatRecieveModel>? = users
    private var recieverId: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {

        val view: View

        if (viewType == Constants.VIEW_TYPE_MESSAGE_RECEIVED)
        {
             view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)

//            view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.item_message_sent, parent, false)

        }
        else
        {
             view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)

//            view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.item_message_received, parent, false)
        }

        return ProfileViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        recieverId = users?.get(position)?.receiverId?.Id.toString()

        if (recieverId != ConfigurationPref(activity).userId) {
            // return 0
            // return Constants.VIEW_TYPE_MESSAGE_SENT
            return Constants.VIEW_TYPE_MESSAGE_RECEIVED
        } else {
            // return 1
            // return Constants.VIEW_TYPE_MESSAGE_RECEIVED
            return Constants.VIEW_TYPE_MESSAGE_SENT
        }
       // return 0
    }

    override fun getItemCount() = users?.size ?: 0

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        // recieverId = users?.get(position)?.receiverId?.Id.toString()
        recieverId = users?.get(position)?.receiverId?.Id.toString()

        // Causing issues has to check // 0
        val recieverImageURL: String = ""+ Constants.imageURL +""+users?.get(0)?.receiverId?.userImage
        val senderImageURL: String = ""+ Constants.imageURL +""+users?.get(0)?.senderId?.userImage
        // val senderImageURL: String = ""+ Constants.imageURL +""+users?.get(position)?.receiverId?.userImage
        Log.d("url_sender",""+recieverImageURL)
        Log.d("url_receiver",""+senderImageURL)


        Log.d("conversation_id",""+recieverId)

//        holder.itemView.iv_user.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("from","conversation")
//
//            MainActivity.getMainActivity
//                ?.navController?.navigate(R.id.userProfileFragment, bundle)
//        }

        if (recieverId != ConfigurationPref(activity).userId) {
            // sender case, opposite name in UI
             holder.itemView.tv_text_received.text = users?.get(position)?.message
             // holder.itemView.iv_user // maybe senderImageURL should be here..
            recieverImageURL.let {
                if (it != Constants.imageURL) {
                    // ImageLoadUtil.loadImageNotFit(it, holder.itemView.iv_user_profile_receiver,R.drawable.user_dp_placeholder)
                    // Additional Condition to Fix the Issue..
                    if(ConfigurationPref(activity).chatSenderPhotoUrl == it){
                         ImageLoadUtil.loadImageNotFit(senderImageURL, holder.itemView.iv_user_profile_receiver,R.drawable.user_dp_placeholder)
                    } else {
                        ImageLoadUtil.loadImageNotFit(recieverImageURL, holder.itemView.iv_user_profile_receiver,R.drawable.user_dp_placeholder)
                    }
                }
            }
        } else {
            // Reciever Case
            holder.itemView.tv_text_sent.text = users?.get(position)?.message
            senderImageURL.let {
                if (it != Constants.imageURL) {
                    // ImageLoadUtil.loadImageNotFit(it, holder.itemView.iv_user_profile_sender,R.drawable.user_dp_placeholder)
                    if(ConfigurationPref(activity).chatSenderPhotoUrl == it){
                        ImageLoadUtil.loadImageNotFit(senderImageURL, holder.itemView.iv_user_profile_sender,R.drawable.user_dp_placeholder)
                    } else {
                        ImageLoadUtil.loadImageNotFit(recieverImageURL, holder.itemView.iv_user_profile_sender,R.drawable.user_dp_placeholder)
                    }
                }
            }
        }

    }


    fun setProfiles(profiles: List<ContactsContract.Profile>) {
        /* this.profiles = profiles
         notifyDataSetChanged()*/
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


}