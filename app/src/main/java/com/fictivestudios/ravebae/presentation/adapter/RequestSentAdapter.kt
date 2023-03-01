package com.fictivestudios.ravebae.presentation.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.models.Datab
import com.fictivestudios.ravebae.utils.Constants
import com.fictivestudios.ravebae.utils.ImageLoadUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_received.view.*
import kotlinx.android.synthetic.main.item_sent.view.*
import kotlinx.android.synthetic.main.item_sent.view.iv_user

class RequestSentAdapter(dataList: List<Datab>?) :
    RecyclerView.Adapter<RequestSentAdapter.ProfileViewHolder>() {

    private var users: List<Datab>? = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sent, parent, false)

        return ProfileViewHolder(view)


    }


    override fun getItemCount() = users?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val recieverImageURL: String = ""+ Constants.imageURL +""+users?.get(position)?.recieverId?.userImage

        holder.itemView.tvUserName.text = users?.get(position)?.recieverId?.username+" Accepted Your Request"

        // Set visibility to visible if set from favourite
//        users?.get(position)?.requestStatus?.let {
//            if(it == 3){
//                holder.itemView.iv_favourite.visibility = View.VISIBLE
//            }
//        }

        users?.get(position)?.isHeart?.let {
            if(it){
                holder.itemView.iv_favourite.visibility = View.VISIBLE
            }
        }

        // iv_user
        recieverImageURL.let {
            if (it != Constants.imageURL) {
                ImageLoadUtil.loadImageNotFit(it, holder.itemView.iv_user,R.drawable.user_dp_placeholder)
            }
        }

        holder.itemView.iv_user.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("from","reqsend")
            bundle.putString("userId",users?.get(position)?.recieverId?.Id)
            val gson = Gson()
            val json: String = gson.toJson(users?.get(position))
            bundle.putString("userprofile",json)
            MainActivity.getMainActivity
                ?.navController?.navigate(R.id.userProfileFragment,bundle)
        }
    }

    fun setProfiles(profiles: List<ContactsContract.Profile>) {
        /* this.profiles = profiles
         notifyDataSetChanged()*/
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}