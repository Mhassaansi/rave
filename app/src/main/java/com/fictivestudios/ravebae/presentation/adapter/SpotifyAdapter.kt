package com.fictivestudios.ravebae.presentation.adapter

import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.presentation.models.SongItem
import com.fictivestudios.ravebae.presentation.models.SpotifyTestModel
import com.fictivestudios.ravebae.utils.ImageLoadUtil
import kotlinx.android.synthetic.main.item_received.view.*
import kotlinx.android.synthetic.main.item_spotify.view.*
import org.json.JSONObject

// SongItem
class SpotifyAdapter(spotifyList: ArrayList<String>?) :
    RecyclerView.Adapter<SpotifyAdapter.ProfileViewHolder>() {

    private var users: List<String>? = spotifyList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spotify, parent, false)

        return ProfileViewHolder(view)


    }


    override fun getItemCount() = users?.size ?: 10

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {



//        try {
//            val gson = Gson()
//            val type = object : TypeToken<SongItem>() {}.type
//            val song: SongItem? =
//                gson.fromJson(users?.get(position).toString(), type)
//
//            song?.imageUrl?.let {
//                ImageLoadUtil.loadImageNotFit(it, holder.itemView.iv_song_img, R.drawable.user_dp)
//            }
//
//            holder.itemView.tv_song_name.text = "" + song?.songName
//            holder.itemView.tv_song_desc.text = "" + song?.artistName
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

//        users?.get(position)?.imageUrl?.let {
//                ImageLoadUtil.loadImageNotFit(it, holder.itemView.iv_song_img,R.drawable.user_dp)
//        }

       // holder.itemView.tv_song_name.text = "" + users?.get(position)?.songName
       // holder.itemView.tv_song_desc.text = "" + users?.get(position)?.artistName

       // Log.d("userList", "" + users?.get(position)?.songName)

       holder.itemView.tv_song_name.text = "" + users?.get(position)
    }

    fun setProfiles(profiles: List<ContactsContract.Profile>) {
        /* this.profiles = profiles
         notifyDataSetChanged()*/
    }

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}