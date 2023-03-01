package com.fictivestudios.ravebae.presentation.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.fictivestudios.ravebae.presentation.models.ProfileModel
import com.fictivestudios.ravebae.presentation.viewmodels.ProfileViewModel
import com.fictivestudios.ravebae.utils.*
import com.fictivestudios.ravebae.utils.Constants.Companion.imageURL
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import okhttp3.Call
import okhttp3.OkHttpClient
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyProfileFragment : BBaseFragment<ProfileViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var isImage = false
    private var isCover = false

    private lateinit var mView: View

    private var activity: MainActivity? = null

    // sample client_id : 089d841ccc194c10a77afad9e1c11d54

//    Client ID 408698e400134110b5e1dce045098204
//    Client Secret eecb29ab874e4dffa7bcbb67b2fd6658

    val CLIENT_ID = "7bf56252cd644b339cc97df5b4d7eeee"
    val AUTH_TOKEN_REQUEST_CODE = 0x10
    val AUTH_CODE_REQUEST_CODE = 0x11

    private var mAccessToken: String? = null
    private var mAccessCode: String? = null
    private val mCall: Call? = null

    // Spotify App Package Name
    private val spotifyAppPackageName = "com.spotify.music"
    private val redirectUri = "ravebae://spotify_login_callback"

    private var userModel: ProfileModel? = null


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
        // , "Profile"
        titlebar.setBtnBack(getString(R.string.profile), resources.getColor(R.color.white))
        titlebar.showTitleBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_my_profile, container, false)

        mView.btn_preferences.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("access_token",mAccessToken)
            MainActivity.getMainActivity?.navController?.navigate(R.id.preferencesFragment)
            // onRequestTokenClicked()
        }

        mView.btn_edit_profile.setOnClickListener {
            val gson = Gson()
            val json:String = gson.toJson(userModel)

            val bundle = Bundle()
            bundle.putString("userModel",json)

            MainActivity.getMainActivity
                ?.navController?.navigate(R.id.editProfileFragment,bundle)
        }

        mView.btn_edit_cover.setOnClickListener {

            isCover = true
            openImagePicker()
        }
        mView.btn_edit_image.setOnClickListener {

            isImage = true
            openImagePicker()
        }

        mView.btn_connect_spotify.setOnClickListener {
            val pm = requireContext().packageManager
            val isAppInstalled = isPackageInstalled(""+spotifyAppPackageName, pm)

            if(isAppInstalled){
                onRequestTokenClicked()
            } else {
                // App not installed navigate to playstore
                Toast.makeText(requireActivity(),"Please Install Spotify App To Connect Spotify",Toast.LENGTH_SHORT).show()

                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$spotifyAppPackageName")
                        )
                    )
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=$spotifyAppPackageName")
                        )
                    )
                }
            }

//            onRequestTokenClicked()
        }

        activity = requireActivity() as MainActivity
        accessTokenCallBack()

        callApi()

        return mView
    }

    private var formatter: MaskedFormatter? = null

    @SuppressLint("SetTextI18n")
    private fun setData(model: ProfileModel) {
        userModel = model

        val profileURL: String = "" + imageURL + "" + model.data?.userImage
        val coverURL: String = "" + imageURL + "" + model.data?.coverImage

        // val s = formatter?.formatString(model.data?.phoneNumber.toString())?.unMaskedString
        val formatter = MaskedFormatter("+# (###) ###-####")
        mView.tv_mobile_num.addTextChangedListener(MaskedWatcher(formatter, mView.tv_mobile_num))

        mView.tv_name.text = "" + MyUtils.printAsString(model.data?.username)
        mView.tv_mobile_num.setText("" + MyUtils.printAsString(model.data?.phoneNumber))
        // mView.tv_mobile_num.text = "" + MyUtils.printAsString(model.data?.phoneNumber)
        mView.tv_bio.setText("" + MyUtils.printAsString(model.data?.userDescription))
        mView.tv_email_txt.text = "" + MyUtils.printAsString(model.data?.email)
        // tv_preferences_txt
        // ?.interest?.split(' ')
        // mView.tv_preferences_txt.text = "" + MyUtils.printAsString(model.data?.preferences?.interest)
        mView.tv_preferences_txt.text = "" + MyUtils.printAsString(model.data?.preferences?.interest?.split(' ')?.joinToString(" ") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()) else it.toString() } }
        )

        if(model.data?.preferences == null) {
            mView.tv_preferences_txt.visibility = View.GONE
        } else {
            mView.tv_preferences_txt.visibility = View.VISIBLE
        }

//                 holder.itemView.tv_preferences_txt.text = profiles?.get(position)?.interest?.split(' ')
//                    ?.joinToString(" ") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
        // mView.tv_profession_profile.text = "" + MyUtils.printAsString(model?.user?.userGender)

        mView.tv_gender_detail.text = "" + MyUtils.printAsString(model.data?.userGender?.split(' ')
            ?.joinToString(" ") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } })

        profileURL.let {
            if (it != imageURL) {
                ImageLoadUtil.loadImageNotFit(it, mView.iv_profile_image, R.drawable.user_dp_placeholder)
            }
            // ImageLoadUtil.loadImageNotFit(it, mView.iv_profile_image)
        }
        //ConfigurationPref(requireActivity()).coverPic.let { ImageLoadUtil.loadImageNotFit(it!!, mView.iv_cover) }
        coverURL.let {
            if (it != imageURL) {
                ImageLoadUtil.loadImageWithoutPlaceHolder(it, mView.iv_cover)
            }
        }
    }

    private fun callApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadProfileData("" + ConfigurationPref(requireActivity()).userId)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadProfileData("" + ConfigurationPref(requireActivity()).userId)

            viewModel?.getProfileModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        if (homeMainModel?.status == 0) {
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
                        } else {
                            // Update Whole Model And Call onBackPressed..

                            /*
                             Display Data
                            **/
                            setData(homeMainModel!!)
                            // Saving Data in Pref
                            ConfigurationPref(requireActivity()).preferenceToggleState = homeMainModel.data?.preferences?.interest
                        }

                    } else {
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("Login", "Else Block" + homeMainModel?.data)
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (e: Exception) {
            Log.d("Login", "SignUp")
            Toast.makeText(requireActivity(), "Invalid Credentials,please try again!", Toast.LENGTH_LONG).show()
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

    private fun openImagePicker() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()

        isPhoto = true
    }

//    override fun onResume() {
//        super.onResume()
//
//        callApi()
//    }

    var isPhoto = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.d("rresponse", "RESULT_OK")

            // Has to test
            if (isPhoto) {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!

                // Use Uri object instead of File to avoid storage permissions
                if (isImage) {
                    isImage = false
                    mView.iv_profile_image.setImageURI(uri)

                    mView.iv_profile_image.background = null
                } else if (isCover) {
                    isCover = false
                    mView.iv_cover.setImageURI(uri)
                    mView.iv_cover.background = null
                }

                isPhoto = false
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }

//        val rrresponse = AuthorizationClient.getResponse(resultCode, data)
//        Log.d("myprofilefragment","my profile code: "+rrresponse.code)
//
//        if (resultCode == Activity.RESULT_OK) {
//            Log.d("rresponse", "RESULT_OK")
//        }
//
//        val rresponse = data?.extras?.get("response")
//        Log.d("rresponse", "" + rresponse)
//
//        val response = AuthorizationClient.getResponse(resultCode, data)
//        if (response.error != null && !response.error.isEmpty()) {
//            setResponse(response.error)
//            Toast.makeText(
//                requireActivity(),
//                "Error: response.error" + response.error,
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
//            mAccessToken = response.accessToken
//            Toast.makeText(requireActivity(), "AccessToken: " + mAccessToken, Toast.LENGTH_SHORT)
//                .show()
//            updateTokenView()
//        } else if (requestCode == AUTH_CODE_REQUEST_CODE) {
//            mAccessCode = response.code
//            // Testing
//            onRequestTokenClicked()
//            // updateCodeView()
//            Log.d("ravebae", "auth request code token:" + mAccessCode)
//            Toast.makeText(requireActivity(), "AccessCode" + mAccessCode, Toast.LENGTH_SHORT).show()
//        }
    }


    /* ==========================  Spotify work ===========================**/


    private fun setResponse(text: String) {
        requireActivity().runOnUiThread(Runnable {
            // val responseView: TextView = findViewById<TextView>(R.id.response_text_view)
            // responseView.text = text
            Log.d("ravebae", "response:" + text)
        })
    }

    private fun updateTokenView() {
        //val tokenView: TextView = findViewById<TextView>(R.id.token_text_view)
        // tokenView.text = getString(R.string.token, mAccessToken)
        Log.d("ravebae", "access token:" + mAccessToken)
    }

    fun onRequestCodeClicked() {
        val request: AuthorizationRequest =
            getAuthenticationRequest(AuthorizationResponse.Type.CODE)
        AuthorizationClient.openLoginActivity(requireActivity(), AUTH_CODE_REQUEST_CODE, request)
    }

    // .setCampaign("your-campaign-token")
    // getRedirectUri().toString()
    private fun getAuthenticationRequest(type: AuthorizationResponse.Type): AuthorizationRequest {
        return AuthorizationRequest.Builder(
            CLIENT_ID,
            type,
            getRedirectUri().toString()
        )
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email", "playlist-read-private", "user-read-private"))
            .build()
    }

    // ravebae://spotify_login_callback
    private fun getRedirectUri(): Uri? {
        return Uri.Builder()
            // testing
            // spotify-sdk , RaveBae
            // ravebae://spotify_login_callback
            // ravebae, spotify_login_callback
            // http://com.fictivestudios.ravebae/callback
            .scheme("ravebae")
            .authority("spotify_login_callback")
            .build()
    }

    fun onRequestTokenClicked() {
        val request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN)
        AuthorizationClient.openLoginActivity(requireActivity(), AUTH_TOKEN_REQUEST_CODE, request)
    }

    /** ============== Interface Click Litener (MainActivity) ========= */
    /** Interface click listener */
    private fun accessTokenCallBack() {
        activity?.setOnButtonClickListener(object :
            MainActivity.OnButtonClickListener {
            override fun onButtonCLick(
                token: String?,
                type: String?,
                resultCode: Int?
            ) {
                token?.let {
                    Log.d("callback", "access token:" + token + " "+type + " "+resultCode)
                    // mAccessToken = token
                    ConfigurationPref(requireActivity()).spotifyAccessToken = token
                    Toast.makeText(requireActivity(),"Spotify Connected Successfully",Toast.LENGTH_SHORT).show()
                    Log.d("callback", "mAccessToken:" +  ConfigurationPref(requireActivity()).spotifyAccessToken )
                }
//                Log.d("callback", "access token:" + token + " "+type + " "+resultCode)
//                // mAccessToken = token
//                ConfigurationPref(requireActivity()).spotifyAccessToken = token
//                Toast.makeText(requireActivity(),"Spotify Connected Successfully",Toast.LENGTH_SHORT).show()
//                Log.d("callback", "mAccessToken:" +  ConfigurationPref(requireActivity()).spotifyAccessToken )
            }
        })
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment myProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}