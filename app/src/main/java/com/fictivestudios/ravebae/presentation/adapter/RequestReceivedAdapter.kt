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
import com.fictivestudios.ravebae.presentation.models.Datam
import com.fictivestudios.ravebae.presentation.models.Datan
import com.fictivestudios.ravebae.utils.Constants
import com.fictivestudios.ravebae.utils.ImageLoadUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_received.view.*
import kotlinx.android.synthetic.main.item_received.view.iv_user
import kotlinx.android.synthetic.main.item_sent.view.*

class RequestReceivedAdapter(dataList: List<Datan>?) :
    RecyclerView.Adapter<RequestReceivedAdapter.ProfileViewHolder>() {

    private var users: List<Datan>? = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_received, parent, false)

        return ProfileViewHolder(view)
    }


    override fun getItemCount() = users?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.itemView.tv_username.text = ""+users?.get(position)?.senderId?.username

        val senderImageURL: String = ""+ Constants.imageURL +""+users?.get(position)?.senderId?.userImage

        senderImageURL.let {
            if (it != Constants.imageURL) {
                ImageLoadUtil.loadImageNotFit(it, holder.itemView.iv_user,R.drawable.user_dp_placeholder)
            }
        }


        holder.itemView.btn_accept.setOnClickListener {
            if (onButtonClickListener != null) onButtonClickListener!!.onButtonCLick(
                position,
                users?.get(position),
                "addfriend"
            )
        }

        holder.itemView.btn_reject.setOnClickListener {
            if (onButtonClickListener != null) onButtonClickListener!!.onButtonCLick(
                position,
                users?.get(position),
                "removefriend"
            )
        }

        holder.itemView.iv_user.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("from","reqreceived")
            bundle.putString("userId",users?.get(position)?.senderId?.Id)
            val gson = Gson()
            val json: String = gson.toJson(users?.get(position))
            bundle.putString("userprofile",json)
            MainActivity.getMainActivity
                ?.navController?.navigate(R.id.userProfileFragment, bundle)
        }
    }

    fun setProfiles(profiles: List<ContactsContract.Profile>) {
        /* this.profiles = profiles
         notifyDataSetChanged()*/
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    var onButtonClickListener: OnButtonClickListener? = null

    @JvmName("setOnButtonClickListener1")
    fun setOnButtonClickListener(onButtonClickListener: OnButtonClickListener?) {
        this.onButtonClickListener = onButtonClickListener
    }

    interface OnButtonClickListener {
        fun onButtonCLick(
            position: Int,
            model: Datan?,
            action: String?
        )
    }
}