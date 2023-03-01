package com.fictivestudios.ravebae.presentation.adapter

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.presentation.models.SongItem
import kotlinx.android.synthetic.main.item_messages.view.*
import kotlinx.android.synthetic.main.item_song.view.*

class PlaylistAdapter(songsList: ArrayList<SongItem>)  : RecyclerView.Adapter<PlaylistAdapter.ProfileViewHolder>() {

    private var users: List<SongItem>? = songsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ProfileViewHolder{


            val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)

        return ProfileViewHolder(view)


    }




    override fun getItemCount() = users?.size ?: 0

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.itemView.tv_song_name.text = ""+ users?.get(position)?.songName
    }

    fun setProfiles(profiles: List<ContactsContract.Profile>) {
       /* this.profiles = profiles
        notifyDataSetChanged()*/
    }

     class ProfileViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

    }
}