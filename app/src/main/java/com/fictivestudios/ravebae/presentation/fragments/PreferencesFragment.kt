package com.fictivestudios.ravebae.presentation.fragments

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
import com.fictivestudios.ravebae.presentation.adapter.PlaylistAdapter
import com.fictivestudios.ravebae.presentation.models.ArrayListSongsItem
import com.fictivestudios.ravebae.presentation.models.SongItem
import com.fictivestudios.ravebae.presentation.viewmodels.PreferenceViewModel
import com.fictivestudios.ravebae.utils.CustomProgressDialogue
// \import com.fictivestudios.ravebae.utils.ErrorDialog
import com.fictivestudios.ravebae.utils.GenericViewModelFactory
import com.fictivestudios.ravebae.utils.MyUtils
import com.fictivestudios.ravebae.utils.Titlebar
import kotlinx.android.synthetic.main.fragment_preferences.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "access_token"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PreferencesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreferencesFragment : BBaseFragment<PreferenceViewModel>() {
    // TODO: Rename and change types of parameters
    // private var mAccessToken: String? = null
    private var param2: String? = null

    private var isMale = false
    private var isFemale = false
    private var isCouple = false
    private var gender: String = "male"
    private lateinit var mView: View

    private var SongsList: ArrayList<SongItem> = ArrayList()

    private var playListIdList: ArrayList<String> = ArrayList()

    val spotifyAccessTokenEmpty = "Authorization\": 'Bearer "
    var isSpotifyConnect=false


    override fun createViewModel(): PreferenceViewModel {
        val factory = GenericViewModelFactory(PreferenceViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(PreferenceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setBtnBack(getString(R.string.preferences), R.color.purple,"Preference",requireActivity())
        titlebar.showTitleBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_preferences, container, false)

        mView.btn_male.setOnClickListener {
//            if (!isMale) {
//                isMale = true
//                isCouple = false
//                isFemale = false
//
//                gender = "male"
//                mView.btn_male.setBackgroundResource(R.drawable.round_button_bg)
//                mView.btn_male.setTextColor(resources.getColor(R.color.white))
//
//                mView.btn_female.setBackgroundResource(R.drawable.round_button_white_bg)
//                mView.btn_female.setTextColor(resources.getColor(R.color.gray))
//                mView.btn_couple.setBackgroundResource(R.drawable.round_button_white_bg)
//                mView.btn_couple.setTextColor(resources.getColor(R.color.gray))
//            }

            isMale()
        }


        mView.btn_save_changes.setOnClickListener {
            if(isSpotifyConnect) {
                callApi()
            }
            else{
                Toast.makeText(requireActivity(), "Connect Spotify First", Toast.LENGTH_LONG).show()

            }
            // MainActivity.getMainActivity?.onBackPressed()
        }

        mView.btn_female.setOnClickListener {

//            if (!isFemale) {
//                gender = "female"
//                isFemale = true
//
//                isCouple = false
//                isMale = false
//
//                mView.btn_female.setBackgroundResource(R.drawable.round_button_bg)
//                mView.btn_female.setTextColor(resources.getColor(R.color.white))
//
//                mView.btn_couple.setBackgroundResource(R.drawable.round_button_white_bg)
//                mView.btn_couple.setTextColor(resources.getColor(R.color.gray))
//                mView.btn_male.setBackgroundResource(R.drawable.round_button_white_bg)
//                mView.btn_male.setTextColor(resources.getColor(R.color.gray))
//            }

            isFemale()
        }

        mView.btn_couple.setOnClickListener {
//            if (!isCouple) {
//                isCouple = true
//                isMale = false
//                isFemale = false
//                gender = "couple"
//                mView.btn_couple.setBackgroundResource(R.drawable.round_button_bg)
//                mView.btn_couple.setTextColor(resources.getColor(R.color.white))
//
//
//                mView.btn_male.setBackgroundResource(R.drawable.round_button_white_bg)
//                mView.btn_male.setTextColor(resources.getColor(R.color.gray))
//                mView.btn_female.setBackgroundResource(R.drawable.round_button_white_bg)
//                mView.btn_female.setTextColor(resources.getColor(R.color.gray))
//
//            }

            isCouple()
        }

        ConfigurationPref(requireActivity()).preferenceToggleState?.let {
            if(it.equals("male",ignoreCase = true) ){
                isMale()
            } else if(it.equals("female",ignoreCase = true)) {
                isFemale()
            } else if(it.equals("couple",ignoreCase = true)) {
                isCouple()
            }
        }

        if (ConfigurationPref(requireActivity()).spotifyAccessToken != spotifyAccessTokenEmpty) {
            // Hit API and Pass Access Token
            callPlayListApi()
        } else {
            Toast.makeText(requireActivity(), "Connect Spotify to Add Playlist", Toast.LENGTH_SHORT)
                .show()
        }

//        mView.rv_spotify_playlist.adapter = PlaylistAdapter()

        return mView
    }

    private fun isMale(){
        if (!isMale) {
            isMale = true
            isCouple = false
            isFemale = false

            gender = "male"
            mView.btn_male.setBackgroundResource(R.drawable.round_button_bg)
            mView.btn_male.setTextColor(resources.getColor(R.color.white))

            mView.btn_female.setBackgroundResource(R.drawable.round_button_white_bg)
            mView.btn_female.setTextColor(resources.getColor(R.color.gray))
            mView.btn_couple.setBackgroundResource(R.drawable.round_button_white_bg)
            mView.btn_couple.setTextColor(resources.getColor(R.color.gray))
        }
    }

    private fun isFemale(){
        if (!isFemale) {
            gender = "female"
            isFemale = true

            isCouple = false
            isMale = false

            mView.btn_female.setBackgroundResource(R.drawable.round_button_bg)
            mView.btn_female.setTextColor(resources.getColor(R.color.white))

            mView.btn_couple.setBackgroundResource(R.drawable.round_button_white_bg)
            mView.btn_couple.setTextColor(resources.getColor(R.color.gray))
            mView.btn_male.setBackgroundResource(R.drawable.round_button_white_bg)
            mView.btn_male.setTextColor(resources.getColor(R.color.gray))
        }
    }

    private fun isCouple(){
        if (!isCouple) {
            isCouple = true
            isMale = false
            isFemale = false
            gender = "couple"
            mView.btn_couple.setBackgroundResource(R.drawable.round_button_bg)
            mView.btn_couple.setTextColor(resources.getColor(R.color.white))


            mView.btn_male.setBackgroundResource(R.drawable.round_button_white_bg)
            mView.btn_male.setTextColor(resources.getColor(R.color.gray))
            mView.btn_female.setBackgroundResource(R.drawable.round_button_white_bg)
            mView.btn_female.setTextColor(resources.getColor(R.color.gray))

        }
    }

    private fun callPlayListApi() {
        Log.d("accesstokenapi", "" + ConfigurationPref(requireActivity()).spotifyAccessToken)

        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadPlayListData("" + ConfigurationPref(requireActivity()).spotifyAccessToken)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadPlayListData("" + ConfigurationPref(requireActivity()).spotifyAccessToken)
            viewModel?.playListModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.items!!.size != 0) {
                        Toast.makeText(
                            requireActivity(),
                            "PlayList Fetched Successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d("playlistapi", "if block " + homeMainModel)


                        // Hit GET PlayList Items API
                        if (homeMainModel?.items?.get(0)?.id != null) {
                            // Instead of playlist ID passing songs name..
                            // playListIdList.add(homeMainModel.items.get(0).id.toString())
                            callPlayListItemsApi(homeMainModel.items.get(0).id.toString())
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "PlayList is not available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
//
//                        // MainActivity.getMainActivity?.onBackPressed()
//                    }
                    else {
//                        ErrorDialog.showErrorDialog(
//                            requireActivity(),
//                            homeMainModel.errorModel?.message
//                        )

                        Toast.makeText(
                            requireActivity(),
                            homeMainModel.errorModel?.message,
                            Toast.LENGTH_LONG
                        ).show()

                        Log.d("playlistapi", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("playlistapi", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("playlistapi", "Catch Block")
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

            viewModel?.loadPlayListItemsData(
                "" + ConfigurationPref(requireActivity()).spotifyAccessToken,
                "" + playListId
            )
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

                            val songId: String? =
                                homeMainModel.items.get(i).track?.album?.artists?.get(0)?.id

                            val model = SongItem("" + songName, "" + artistName, "" + imageUrl)
                            SongsList.add(model)
                            // Populating playlist ID Api with songs names
                            playListIdList.add(songName.toString())
                        }

                        // Set Adapter For Spotify
                        mView.rv_spotify_playlist.adapter = PlaylistAdapter(SongsList)
                        isSpotifyConnect=true

                    } else {
                        // Do nothing
                        Log.d(
                            "playlistitemsapi",
                            "else homeMainModel?.items!!.size block " + homeMainModel
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
                        isSpotifyConnect=false

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

    private fun callApi() {
        Log.d("gender", gender)

//        val songName : String?,
//        val artistName : String?,
//        val imageUrl : String

        val listHashData: ArrayList<HashMap<String, String>> = ArrayList()

        for (i in 0 until SongsList.size) {
            val hashData = HashMap<String, String>()

            hashData.put("songName", "" + SongsList.get(i).songName)
            hashData.put("artistName", "" + SongsList.get(i).artistName)
            hashData.put("imageUrl", "" + SongsList.get(i).imageUrl)

            Log.d("hashData", SongsList.get(i).songName.toString())

            listHashData.add(hashData)
        }



        try {
            val arr: List<String> = arrayListOf("xx", "yy")
            // listHashData
            val arrSongsList = ArrayListSongsItem(playListIdList, gender)

            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadPreferenceData(gender, arrSongsList)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }
//            viewModel?.loadPreferenceData(gender, arrSongsList)
            viewModel?.preferenceModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        Toast.makeText(
                            requireActivity(),
                            "" + homeMainModel?.message,
                            Toast.LENGTH_LONG
                        ).show()
                        // MainActivity.getMainActivity?.navController?.navigate(R.id.myProfileFragment)
                        // MainActivity.getMainActivity?.onBackPressed()
                        MainActivity.getMainActivity?.navController?.popBackStack()
                    } else {

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

                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("Preference", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("Preference", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("Preference", "Catch Block")
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            Toast.makeText(
                requireActivity(),
                "Invalid Credentials,please try again!",
                Toast.LENGTH_LONG
            ).show()
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
         * @return A new instance of fragment PreferencesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PreferencesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}