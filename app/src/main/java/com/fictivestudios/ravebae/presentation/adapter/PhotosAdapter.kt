package com.fictivestudios.ravebae.presentation.adapter

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.utils.Constants
import com.fictivestudios.ravebae.utils.ImageLoadUtil
import kotlinx.android.synthetic.main.item_photos.view.*
import kotlinx.android.synthetic.main.item_swipe_card.view.*

class PhotosAdapter(userPhotos: List<String>?)  : RecyclerView.Adapter<PhotosAdapter.ProfileViewHolder>() {

    private var users: List<String>? = userPhotos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ProfileViewHolder{


            val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photos, parent, false)

        return ProfileViewHolder(view)
    }




    override fun getItemCount() = users?.size ?: 0

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        val photoURL: String =
            "" + Constants.imageURL + "" + ""+ users?.get(position)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("photo",""+photoURL)
            MainActivity.getMainActivity
                ?.navController?.navigate(R.id.userProfileDetailFragment, bundle)
        }

        photoURL.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(it, holder.itemView.iv_user_photo, R.drawable.user_dp_placeholder)
            }
        }

    }

    fun setProfiles(profiles: List<ContactsContract.Profile>) {
       /* this.profiles = profiles
        notifyDataSetChanged()*/
    }

     class ProfileViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

    }
}