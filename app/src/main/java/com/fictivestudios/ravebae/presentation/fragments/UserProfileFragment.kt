package com.fictivestudios.ravebae.presentation.fragments

// import com.fictivestudios.ravebae.presentation.models.User
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.adapter.PhotosAdapter
import com.fictivestudios.ravebae.presentation.adapter.SpotifyAdapter
import com.fictivestudios.ravebae.presentation.models.*
import com.fictivestudios.ravebae.presentation.viewmodels.ProfileViewModel
import com.fictivestudios.ravebae.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_preferences.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.tv_name
import kotlinx.android.synthetic.main.fragment_user_profile.view.tv_profession
import kotlinx.android.synthetic.main.item_swipe_card.view.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfileFragment : BBaseFragment<ProfileViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    var userProfile: Datam? = null
    var userProfileFromMessages: Datac? = null
    var ischat=""

    //
    var userDataReqSend: Datab? = null
    var userDataReqReceived: Datan? = null


    private var SongsList: ArrayList<SongItem> = ArrayList()
    private var from: String? = null
    private val spotifyEmptyBearer = "Authorization\": 'Bearer "


    override fun createViewModel(): ProfileViewModel {
        val factory = GenericViewModelFactory(ProfileViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.showTitleBar()
        titlebar.setBtnBack("", R.color.purple)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_user_profile, container, false)

        mView.btn_chat.setOnClickListener {
            if (ischat.equals("false")) {
            from?.let {

                     if (from.equals("messages", ignoreCase = true)) {
                        if (userProfileFromMessages?.recieverId?.Id != ConfigurationPref(requireActivity()).userId) {

                            val recieverImageURL: String =
                                "" + Constants.imageURL + "" + userProfileFromMessages?.recieverId?.userImage

                            ConfigurationPref(requireActivity()).chatReceiverId =
                                "" + userProfileFromMessages?.recieverId?.Id
                            ConfigurationPref(requireActivity()).chatSenderPhotoUrl =
                                "" + recieverImageURL
                            ConfigurationPref(requireActivity()).chatUserName =
                                "" + userProfileFromMessages?.recieverId?.username
                        }
                        else{
                            val recieverImageURL: String =
                                "" + Constants.imageURL + "" + userProfileFromMessages?.senderId?.userImage

                            ConfigurationPref(requireActivity()).chatReceiverId =
                                "" + userProfileFromMessages?.senderId?.Id
                            ConfigurationPref(requireActivity()).chatSenderPhotoUrl =
                                "" + recieverImageURL
                            ConfigurationPref(requireActivity()).chatUserName =
                                "" + userProfileFromMessages?.senderId?.username
                        }

                        // Testing // Reference from message adapter
                        // ConfigurationPref(requireActivity()).conversationUserId = "" + userProfile?.userId?.Id
                    }

                    else  if (from.equals("reqsend", ignoreCase = true)) {
                        val recieverImageURL: String =
                            "" + Constants.imageURL + "" + userDataReqSend?.recieverId?.userImage

                        ConfigurationPref(requireActivity()).chatReceiverId =
                            "" + userDataReqSend?.recieverId?.Id
                        ConfigurationPref(requireActivity()).chatSenderPhotoUrl =
                            "" + recieverImageURL
                        ConfigurationPref(requireActivity()).chatUserName =
                            "" + userDataReqSend?.recieverId?.username

                        // Testing // Reference from message adapter
                        // ConfigurationPref(requireActivity()).conversationUserId = "" + userProfile?.userId?.Id
                    }

                  else  if (from.equals("reqreceived", ignoreCase = true)) {
                        val recieverImageURL: String =
                            "" + Constants.imageURL + "" + userDataReqReceived?.senderId?.userImage

                        ConfigurationPref(requireActivity()).chatReceiverId =
                            "" + userDataReqReceived?.senderId?.Id
                        ConfigurationPref(requireActivity()).chatSenderPhotoUrl =
                            "" + recieverImageURL
                        ConfigurationPref(requireActivity()).chatUserName =
                            "" + userDataReqReceived?.senderId?.username

                        // Testing // Reference from message adapter
                        // ConfigurationPref(requireActivity()).conversationUserId = "" + userProfile?.userId?.Id
                    }

                     else  {
                         val recieverImageURL: kotlin.String =
                             "" + com.fictivestudios.ravebae.utils.Constants.imageURL + "" + userProfile?.userId?.userImage

                         com.fictivestudios.ravebae.core.storage.ConfigurationPref(requireActivity()).chatReceiverId =
                             "" + userProfile?.userId?.Id
                         com.fictivestudios.ravebae.core.storage.ConfigurationPref(requireActivity()).chatSenderPhotoUrl =
                             "" + recieverImageURL
                         com.fictivestudios.ravebae.core.storage.ConfigurationPref(requireActivity()).chatUserName =
                             "" + userProfile?.userId?.username

                         // Testing // Reference from message adapter
                         // ConfigurationPref(requireActivity()).conversationUserId = "" + userProfile?.userId?.Id
                     }
                }

                MainActivity.getMainActivity
                    ?.navController?.navigate(R.id.conversationFragment)
            }
            else{
                (context as Activity).onBackPressed()
            }
        }

        try {
            from = this.requireArguments().getString("from")
            val jsonString = this.requireArguments().getString("userprofile")
            val gson = Gson()

            from?.let{
                if(it.equals("messages",ignoreCase = true)){
                    userProfileFromMessages = gson.fromJson(jsonString, Datac::class.java)
                }
                else if(it.equals("reqreceived",ignoreCase = true)){
                    userDataReqReceived = gson.fromJson(jsonString, Datan::class.java)

                }
                else if(it.equals("reqsend",ignoreCase = true)){
                    userDataReqSend = gson.fromJson(jsonString, Datab::class.java)

                }
                else{
                    userProfile = gson.fromJson(jsonString, Datam::class.java)
                    Log.e("sdhhd","x"+userProfile.toString())

                }
            }

            // userProfile = gson.fromJson(jsonString, Datam::class.java)

            // title conversation tap
            val userId = this.requireArguments().getString("userId")
            Log.e("sdhhd",""+userId)


            if(this.requireArguments().getString("isChat")!=null){
                ischat="true"
            }
            else{
                ischat="false"
            }


            // Add Else if tag
            if (from.equals("conversation", ignoreCase = true)) {
                // Hit API and populate data
//                callApi()
                mView.btn_chat.visibility = View.VISIBLE
                userId?.let { callApi(it) }
            }
            else if (from.equals("messages", ignoreCase = true)) {
                // Hit API and populate data
                mView.btn_chat.visibility = View.VISIBLE
                userId?.let { callApi(it) }
            }
            else if (from.equals("reqsend", ignoreCase = true)) {
                userId?.let { callApi(it) }
            } else if (from.equals("reqreceived", ignoreCase = true)) {
                userId?.let { callApi(it) }
            }
//            else if(from.equals("title_conversation", ignoreCase = true)){
//                userId?.let { callApi(it) }
//            }
            else {
                // set data, available from previous class
                setData(userProfile)
                // mView.btn_chat.visibility = View.INVISIBLE
                mView.btn_chat.visibility = View.VISIBLE
            }
            mView.btn_block.setOnClickListener {
                if(from.equals("messages", ignoreCase = true)){
                    var id=""
                    if(!userProfileFromMessages?.recieverId!!.Id.toString().equals(ConfigurationPref(requireActivity()).userId.toString())){
                        id= userProfileFromMessages?.recieverId!!.Id.toString()
                        Log.e("sfjjdff","rec "+ userProfileFromMessages?.recieverId!!.Id.toString())

                    }
                    else{
                        id= userProfileFromMessages?.senderId!!.Id!!.toString()
                        Log.e("sfjjdff","sen "+userProfileFromMessages?.senderId!!.Id!!.toString())

                    }
                    Log.e("dfdffd","id "+id )

                    callApiForUserBlock(id.toString(),"block")
                }
                else if(from.equals("reqreceived")){
                    var id=""
                    if(!userDataReqReceived?.recieverId.toString().equals(ConfigurationPref(requireActivity()).userId.toString())){
                        id= userDataReqReceived?.recieverId.toString()
                        Log.e("sfjjdff","rec "+userDataReqReceived?.recieverId.toString())

                    }
                    else{
                        id= userDataReqReceived?.senderId!!.Id!!.toString()
                        Log.e("sfjjdff","sen "+userDataReqReceived?.senderId!!.Id!!.toString())

                    }
                    Log.e("dfdffd","id "+id )
                    callApiForUserBlock(id.toString(),"block")
                }
                else if(from.equals("reqsend")){
                    var id=""
                    if(userDataReqSend?.recieverId?.Id != ConfigurationPref(requireActivity()).userId){
                        id= userDataReqSend?.recieverId?.Id.toString()
                        Log.e("sfjjdff","sen" +userDataReqSend?.recieverId?.Id!!.toString())

                    }
                    else{
                        id= userDataReqSend!!.senderId!!.toString()
                        Log.e("sfjjdff","rec "+userDataReqSend?.senderId!!.toString())

                    }
                    Log.e("dfdffd","id "+id )
                    callApiForUserBlock(id.toString(),"block")

                } else{
                    Log.e("dfdffd",""+ userId.toString())
                   callApiForUserBlock(userId.toString(),"block")
               }
              //  userId?.let {

              //  }

            }





//            setData(userProfile)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        //   mView.carousel.registerLifecycle(lifecycle)
        //   val list = mutableListOf<CarouselItem>()

//
//        list.add(
//            CarouselItem(
//                imageDrawable = R.drawable.user_dp,
//                caption = ""
//            )
//        )
//
//        list.add(
//            CarouselItem(
//                imageDrawable = R.drawable.user_dp,
//                caption = ""
//            )
//        )
//
//        list.add(
//            CarouselItem(
//                imageDrawable = R.drawable.user_dp,
//                caption = ""
//            )
//        )

        // mView.carousel.setData(list)

        // mView.rv_spotify.adapter = SpotifyAdapter()
        // mView.rv_photos.adapter = PhotosAdapter(userProfile?.userId?.userPhotos)

        return mView
    }

    private fun callApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            viewModel?.loadProfileData("" + ConfigurationPref(requireActivity()).conversationUserId)

            viewModel?.getProfileModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        if (homeMainModel?.status == 0) {
                            // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel.message)
                             if (homeMainModel?.message=="Unauthorized"){
                          ConfigurationPref(requireActivity()).clear()
                          startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                          MainActivity.getMainActivity?.finish()
                          Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                      }
                      else{
                          Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                              .show()
                      }

                        } else {
                            // Update Whole Model And Call onBackPressed..

                            /*
                             Display Data
                            **/
                            setData(homeMainModel?.data!!)
                            // Load Spotify PlayList Items Api Data
                            homeMainModel.data?.preferences?.spotifyList?.get(0)?.let {
                                callPlayListItemsApi(it)
                            }
                        }

                    } else {
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                            .show()
                        Log.d("Login", "Else Block" + homeMainModel?.data)
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (e: Exception) {
            Log.d("Login", "SignUp")
            Toast.makeText(
                requireActivity(),
                "Invalid Credentials,please try again!",
                Toast.LENGTH_LONG
            ).show()
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
        }
    }

    private fun callApi(userId: String) {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadProfileData("" + userId)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }
//            viewModel?.loadProfileData("" + userId)

            viewModel?.getProfileModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        if (homeMainModel?.status == 0) {
                            // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel.message)
                             if (homeMainModel?.message=="Unauthorized"){
                          ConfigurationPref(requireActivity()).clear()
                          startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                          MainActivity.getMainActivity?.finish()
                          Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                      }
                      else{
                          Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                              .show()
                      }

                            Toast.makeText(
                                requireActivity(),
                                homeMainModel.message,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            // Update Whole Model And Call onBackPressed..

                            /*
                             Display Data
                            **/
                            setData(homeMainModel?.data!!)
                            // Unused Case
                            // Load Spotify PlayList Items Api Data
//                            homeMainModel.data?.preferences?.spotifyList?.get(0)?.let {
//                                callPlayListItemsApi(it)
//                            }
                        }

                    } else {
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                            .show()
                        Log.d("Login", "Else Block" + homeMainModel?.data)
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (e: Exception) {
            Log.d("Login", "SignUp")
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            Toast.makeText(
                requireActivity(),
                "Invalid Credentials,please try again!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun callPlayListItemsApi(playListId: String) {
        // Log.d("accesstokenapi", ""+mAccessToken)
        if (spotifyEmptyBearer.equals(
                "" + ConfigurationPref(requireActivity()).spotifyAccessToken,
                ignoreCase = true
            )
        ) {
            Toast.makeText(
                requireActivity(),
                "Please connect spotify to view user playlist",
                Toast.LENGTH_LONG
            ).show()
        } else {
            // Hit API
            try {
                viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

                if(MyUtils.isNetworkAvailable(requireActivity())){
                    viewModel?.loadPlayListItemsData(
                        "" + ConfigurationPref(requireActivity()).spotifyAccessToken,
                        "" + playListId
                    )
                } else{
                    Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
                }
//                viewModel?.loadPlayListItemsData(
//                    "" + ConfigurationPref(requireActivity()).spotifyAccessToken,
//                    "" + playListId
//                )
                viewModel?.playListItemsModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                    try {

                        if (homeMainModel?.items!!.size != 0) {
                            for (i in 0 until homeMainModel.items.size) {
                                val imageUrl: String? =
                                    homeMainModel.items.get(i).track?.album?.images?.get(0)?.url
                                // val songName: String? = homeMainModel.items.get(i).track?.album?.name
                                val songName: String? = homeMainModel.items.get(i).track?.name
                                val artistName: String? =
                                    homeMainModel.items.get(i).track?.album?.artists?.get(0)?.name

                                // val songId: String? =  homeMainModel.items.get(i).track?.album?.artists?.get(0)?.id

                                val model = SongItem("" + songName, "" + artistName, "" + imageUrl)
                                SongsList.add(model)
                            }
                            // Set Visibility of Spotify textView to VISIBLE
                            mView.tv_spotify_playlists.visibility = View.VISIBLE
                            // Set Adapter For Spotify
                            //mView.rv_spotify_playlist.adapter = PlaylistAdapter(SongsList)
                            // Unused Case
                            // mView.rv_spotify.adapter = SpotifyAdapter(SongsList)
                        } else {
                            // Set Visibility of Spotify textView to GONE
                            mView.tv_spotify_playlists.visibility = View.GONE
                            // Do nothing
                            Log.d(
                                "playlistitemsapi",
                                "else homeMainModel?.items!!.size block " + homeMainModel.errorModel?.message
                            )
//                        ErrorDialog.showErrorDialog(
//                            requireActivity(),
//                            homeMainModel.errorModel?.message
//                        )

                            Toast.makeText(
                                requireActivity(),
                                homeMainModel.errorModel?.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

//                        val imageUrl: String? = homeMainModel?.items?.get(0)?.track?.album?.images?.get(0)?.url
//                        val songName: String? = homeMainModel?.items?.get(0)?.track?.album?.name
//                        val artistName: String? = homeMainModel?.items?.get(0)?.track?.album?.artists?.get(0)?.name
                        Log.d("playlistitemsapi", "if block " + homeMainModel)

                        // Hit GET PlayList Items API


                        // MainActivity.getMainActivity?.onBackPressed()
                    } catch (e: Exception) {
                        Log.d("playlistitemsapi", "Catch Block")
                    }
                }
            } catch (ex: Exception) {
                Log.d("playlistitemsapi", "Catch Block")
                // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
                Toast.makeText(
                    requireActivity(),
                    "Invalid Credentials,please try again!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
//        try {
//            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())
//            viewModel?.loadPlayListItemsData(
//                "" + ConfigurationPref(requireActivity()).spotifyAccessToken,
//                "" + playListId
//            )
//            viewModel?.playListItemsModelData?.observe(viewLifecycleOwner) { homeMainModel ->
//                try {
//
//                    if (homeMainModel?.items!!.size != 0) {
//                        for (i in 0 until homeMainModel.items.size) {
//                            val imageUrl: String? =
//                                homeMainModel.items.get(i).track?.album?.images?.get(0)?.url
//                            val songName: String? = homeMainModel.items.get(i).track?.album?.name
//                            val artistName: String? =
//                                homeMainModel.items.get(i).track?.album?.artists?.get(0)?.name
//
//                            // val songId: String? =  homeMainModel.items.get(i).track?.album?.artists?.get(0)?.id
//
//                            val model = SongItem("" + songName, "" + artistName, "" + imageUrl)
//                            SongsList.add(model)
//                        }
//                        // Set Visibility of Spotify textView to VISIBLE
//                        mView.tv_spotify_playlists.visibility = View.VISIBLE
//                        // Set Adapter For Spotify
//                        //mView.rv_spotify_playlist.adapter = PlaylistAdapter(SongsList)
//                        mView.rv_spotify.adapter = SpotifyAdapter(SongsList)
//                    } else {
//                        // Set Visibility of Spotify textView to GONE
//                        mView.tv_spotify_playlists.visibility = View.GONE
//                        // Do nothing
//                        Log.d(
//                            "playlistitemsapi",
//                            "else homeMainModel?.items!!.size block " + homeMainModel.errorModel?.message
//                        )
////                        ErrorDialog.showErrorDialog(
////                            requireActivity(),
////                            homeMainModel.errorModel?.message
////                        )
//
//                        Toast.makeText(requireActivity(), homeMainModel.errorModel?.message, Toast.LENGTH_LONG).show()
//                    }
//
////                        val imageUrl: String? = homeMainModel?.items?.get(0)?.track?.album?.images?.get(0)?.url
////                        val songName: String? = homeMainModel?.items?.get(0)?.track?.album?.name
////                        val artistName: String? = homeMainModel?.items?.get(0)?.track?.album?.artists?.get(0)?.name
//                    Log.d("playlistitemsapi", "if block " + homeMainModel)
//
//                    // Hit GET PlayList Items API
//
//
//                    // MainActivity.getMainActivity?.onBackPressed()
//                } catch (e: Exception) {
//                    Log.d("playlistitemsapi", "Catch Block")
//                }
//            }
//        }
//        catch (ex: Exception) {
//            Log.d("playlistitemsapi", "Catch Block")
//            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
//            Toast.makeText(requireActivity(), "Invalid Credentials,please try again!", Toast.LENGTH_LONG).show()
//        }
    }

    fun setData(userProfile: Datam?) {
        // Setting Data
        Log.e("dffdfd","user"+userProfile?.userId?.username)

        mView.tv_name.text = "" + userProfile?.userId?.username
        // mView.tv_profession.text = "" + userProfile?.userId?.userGender
        val upperString: String =
            userProfile?.userId?.userGender!!.substring(0, 1).toUpperCase() + userProfile?.userId?.userGender!!.substring(1).toLowerCase()

        mView.tv_profession.text = upperString
        mView.tv_about_detail.text = "" + userProfile?.userId?.userDescription

        // carousel
        val profileURL: String =
            "" + Constants.imageURL + "" + "" + userProfile?.userId?.userImage
        profileURL.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(it, mView.carousel, R.drawable.user_dp_placeholder)
            }
        }

        // if songs list is found change label visibility // if spotify list is not null show label
        userProfile?.spotifyList?.let {
            mView.tv_spotify_playlists.visibility = View.VISIBLE
        }



        mView.rv_photos.adapter = PhotosAdapter(userProfile?.userId?.userPhotos)

        mView.rv_spotify.adapter = SpotifyAdapter(userProfile?.spotifyList)

        // Unused case..
//        userProfile?.spotifyList?.get(0)?.let {
//            callPlayListItemsApi(it)
//        }
    }

    fun setData(userProfile: Datap?) {
        // Setting Data
        Log.e("dffdfd",""+userProfile?.username.toString())
        mView.tv_name.text = "" + userProfile?.username
        val upperString: String =
            userProfile?.userGender!!.substring(0, 1).toUpperCase() + userProfile?.userGender!!.substring(1).toLowerCase()

        mView.tv_profession.text = "" + upperString
        mView.tv_about_detail.text = "" + userProfile?.userDescription

        // carousel
        val profileURL: String =
            "" + Constants.imageURL + "" + "" + userProfile?.userImage
        profileURL.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(it, mView.carousel, R.drawable.user_dp)
            }
        }

        // if songs list is found change label visibility // if spotify list is not null show label
        userProfile?.preferences?.spotifyList?.let {
            mView.tv_spotify_playlists.visibility = View.VISIBLE
        }

        mView.rv_photos.adapter = PhotosAdapter(userProfile?.userPhotos)
        mView.rv_spotify.adapter = SpotifyAdapter(userProfile?.preferences?.spotifyList)
    }


    private fun callApiForUserBlock(userId: String,type:String) {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            viewModel?.userBlockData("" + userId,type)

            viewModel?.getUserBlockEventData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        if (homeMainModel?.status == 0) {
                            // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel.message)
                             if (homeMainModel?.message=="Unauthorized"){
                          ConfigurationPref(requireActivity()).clear()
                          startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                          MainActivity.getMainActivity?.finish()
                          Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                      }
                      else{
                          Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                              .show()
                      }

                        } else {
                            // Update Whole Model And Call onBackPressed..
                            (context as Activity).onBackPressed()
                            Toast.makeText(
                                requireActivity(),
                                homeMainModel!!.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                            .show()
                     //   Log.d("Login", "Else Block" + homeMainModel?.data)
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (e: Exception) {
            Log.d("Login", "SignUp")
            Toast.makeText(
                requireActivity(),
                "Invalid Credentials,please try again!",
                Toast.LENGTH_LONG
            ).show()
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
        }
    }

    //Observer
    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (progress == null) progress = CustomProgressDialogue(activity)
            if (isLoading) {
                if (progress?.isShowing == false) progress?.show()
            } else {
                if (progress?.isShowing == true) progress?.dismiss()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}