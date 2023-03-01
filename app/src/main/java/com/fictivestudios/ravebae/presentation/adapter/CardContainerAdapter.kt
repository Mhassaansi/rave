package com.fictivestudios.ravebae.presentation.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.presentation.models.Datam
import com.fictivestudios.ravebae.utils.Constants
import com.fictivestudios.ravebae.utils.ImageLoadUtil
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import kotlinx.android.synthetic.main.item_swipe_card.view.*
import kotlinx.android.synthetic.main.item_swipe_card.view.tv_name
import kotlinx.android.synthetic.main.item_swipe_card.view.tv_preferences_txt
import kotlinx.android.synthetic.main.item_swipe_card.view.tv_profession
import java.util.*
import kotlin.collections.ArrayList

class CardContainerAdapter(profileList: ArrayList<Datam>?, val activity: Activity) :
    RecyclerView.Adapter<CardContainerAdapter.ProfileViewHolder>() {

    private var profiles: ArrayList<Datam>? = profileList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_swipe_card, parent, false)

        return ProfileViewHolder(view)
    }

    override fun getItemCount() = profiles?.size ?: 0

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        // holder.itemView.iv_user_image.setBackgroundResource(R.drawable.user_dp)

        // ImageLoadUtil.loadImageNotFit(profiles?.get(position)?.userImage!!, holder.itemView.iv_user_image, R.drawable.user_dp )
        val profileURL: String =
            "" + Constants.imageURL + "" + ""+ profiles?.get(position)?.userId?.userImage
        profileURL.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(it, holder.itemView.iv_user_image, R.drawable.user_dp_placeholder)
            }
        }


        holder.itemView.tv_name.text = profiles?.get(position)?.userId?.username
        // holder.itemView.tv_profession.text = profiles?.get(position)?.userId?.userGender
        holder.itemView.tv_profession.text = profiles?.get(position)?.userId?.userDescription
        // holder.itemView.tv_preferences_txt.text = profiles?.get(position)?.interest
        holder.itemView.tv_preferences_txt.text = profiles?.get(position)?.interest?.split(' ')
            ?.joinToString(" ") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }


        // btn_reject
        holder.itemView.btn_reject.setOnClickListener {
            // Remove current item from recyclerview
            // profiles?.removeAt(position + 1)
            removeItem(position)
        }

        // btn_accept
        holder.itemView.btn_accept.setOnClickListener {
            // Hit API for add friend throught interface(fragment)
            // Toast.makeText(activity,"Add Friend",Toast.LENGTH_LONG).show()

            if (onButtonClickListener != null) onButtonClickListener!!.onButtonCLick(
                position,
                profiles?.get(position),
                "AddFriend"
            )
        }

        // btn_heart
        holder.itemView.btn_love.setOnClickListener {
            // Hit API for add friend throught interface(fragment)
            // Toast.makeText(activity,"Add Friend",Toast.LENGTH_LONG).show()

            if (onButtonClickListener != null) onButtonClickListener!!.onButtonCLick(
                position,
                profiles?.get(position),
                "AddHeart"
            )
        }


        holder.itemView.iv_user_image.setOnClickListener {
//            MainActivity.getMainActivity
//                ?.navController?.navigate(R.id.userProfileFragment)

            if (onButtonClickListener != null) onButtonClickListener!!.onButtonCLick(
                position,
                profiles?.get(position),
                "Profile"
            )
        }

//        holder.itemView.iv_user_image.setOnClickListener {
//            MainActivity.getMainActivity
//                ?.navController?.navigate(R.id.userProfileFragment)
//        }
    }

    /* fun setProfiles(profiles: List<ContactsContract.Profile>) {
        *//* this.profiles = profiles
        notifyDataSetChanged()*//*
    }*/

    private fun removeItem(position: Int) {
        profiles?.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, profiles!!.size)
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
            model: Datam?,
            action: String?
        )
    }
}