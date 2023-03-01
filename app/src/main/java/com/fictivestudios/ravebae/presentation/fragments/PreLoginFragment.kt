package com.fictivestudios.ravebae.presentation.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Selection
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.viewmodels.PreLoginViewModel
import com.fictivestudios.ravebae.utils.*
import com.fictivestudios.ravebae.utils.Constants.Companion.EMAIL
import com.fictivestudios.ravebae.utils.Constants.Companion.PHONE
import com.fictivestudios.ravebae.utils.Constants.Companion.isAccountLogin
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import kotlinx.android.synthetic.main.fragment_pre_login.view.*
import java.util.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PreLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreLoginFragment : BBaseFragment<PreLoginViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    //a constant for detecting the login intent result
    private val RC_SIGN_IN = 234

    //creating a GoogleSignInClient object
    var mGoogleSignInClient: GoogleSignInClient? = null

    //And also a Firebase Auth object
    var mAuth: FirebaseAuth? = null

    // Social Login Variables
    var userSocialToken: String? = null
    var userSocialType: String? = null
    var email: String? = null


    override fun createViewModel(): PreLoginViewModel {
        val factory = GenericViewModelFactory(PreLoginViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(PreLoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(
            com.fictivestudios.ravebae.R.layout.fragment_pre_login,
            container,
            false
        )


        mView.btn_phone.setOnClickListener {

            isAccountLogin = true
            Constants.loginType = PHONE
            ResgistrationActivity.getRegActivity
                ?.navControllerReg?.navigate(com.fictivestudios.ravebae.R.id.agreementFragment)
        }

        mView.btn_email.setOnClickListener {

            Constants.loginType = EMAIL
            ResgistrationActivity.getRegActivity?.navControllerReg?.navigate(R.id.loginFragment)
            // ResgistrationActivity.getRegActivity?.navControllerReg?.navigate(com.fictivestudios.ravebae.R.id.agreementFragment)
        }

//        <string name="default_web_client_id">217072362646-coed13gf0kfef6iu8he3fr5rgeeb53d9.apps.googleusercontent.com</string>


        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance()

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        // .requestIdToken("217072362646-tmd9ovimucfcrkrerum448ili233siv3.apps.googleusercontent.com")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // 217072362646-tmd9ovimucfcrkrerum448ili233siv3.apps.googleusercontent.com // web
            // 217072362646-coed13gf0kfef6iu8he3fr5rgeeb53d9.apps.googleusercontent.com // json
            // 217072362646-tmd9ovimucfcrkrerum448ili233siv3.apps.googleusercontent.com // latest
            // AIzaSyAWcMt-IzaZKnHytIol0RfZy5yJ3xZXk3k // outside
            .requestIdToken("217072362646-tmd9ovimucfcrkrerum448ili233siv3.apps.googleusercontent.com")
            .requestEmail()
            .build()

        //Then we will get the GoogleSignInClient object from GoogleSignIn class

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        mView.btn_google.setOnClickListener {
            googleSignIn();
        }

        // setSpannableListener()
        mView.termsAndConditionTv.setOnClickListener { ResgistrationActivity.getRegActivity?.navControllerReg?.navigate(R.id.termsAndConditionsFragment) }
        mView.PrivacyPolicyTv.setOnClickListener { ResgistrationActivity.getRegActivity?.navControllerReg?.navigate(R.id.privacyAndPolicyFragment) }

        return mView
    }

//    private fun setSpannableListener() {
//        val spannableString = SpannableString(getString(R.string.termsAndPolicy))
//
//
//        val clickableSpan1 = object : ClickableSpan() {
//            override fun updateDrawState(textPaint: TextPaint) {
//                // use this to change the link color
//                // textPaint.color = textPaint.linkColor
//                textPaint.color = resources.getColor(R.color.darkblue)
//                // toggle below value to enable/disable
//                // the underline shown below the clickable text
//                textPaint.isUnderlineText = false
//            }
//
//            override fun onClick(view: View) {
//                 // Toast.makeText(requireActivity(), "Clicked Privacy", Toast.LENGTH_SHORT).show()
//                // appVariables.from = "terms"
//                // LoginActivity.getLoginActivity?.navControllerLogin?.navigate(R.id.termsAndPolicyFragment)
//                ResgistrationActivity.getRegActivity?.navControllerReg?.navigate(R.id.termsAndConditionsFragment)
//            }
//        }
//        // T & C
//        spannableString.setSpan(
//            clickableSpan1, 32, 50, // 55, 69
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        val clickableSpan2 = object : ClickableSpan() {
//            override fun updateDrawState(textPaint: TextPaint) {
//                // use this to change the link color
//                // textPaint.color = textPaint.linkColor
//                textPaint.color = resources.getColor(R.color.darkblue)
//                // toggle below value to enable/disable
//                // the underline shown below the clickable text
//                textPaint.isUnderlineText = false
//            }
//
//            override fun onClick(view: View) {
//                 // Toast.makeText(requireActivity(), "Clicked Conditions", Toast.LENGTH_SHORT).show()
//                // appVariables.from = "privacy"
//                // LoginActivity.getLoginActivity?.navControllerLogin?.navigate(R.id.termsAndPolicyFragment)
//                ResgistrationActivity.getRegActivity?.navControllerReg?.navigate(R.id.privacyAndPolicyFragment)
//            }
//        }
//
//        // Privacy Policy
//        spannableString.setSpan(
//            clickableSpan2, 54, 69,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        mView.termsAndPolicyTv.movementMethod = LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
//        mView.termsAndPolicyTv.setText(spannableString, TextView.BufferType.SPANNABLE)
//        // mView.termsAndPolicyTv.highlightColor = Color.WHITE // white // R.drawable.spanselector
//    }


    override fun onStart() {
        super.onStart()

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
//        if (mAuth!!.currentUser != null) {
//            finish()
//            startActivity(Intent(this, ProfileActivity::class.java))
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode === RC_SIGN_IN) {
            // similar condition for facebook
            ConfigurationPref(requireActivity()).from = "google"

            //Getting the GoogleSignIn Task
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

                //authenticating with firebase
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("googlesigninwithfb", "firebaseAuthWithGoogle:" + acct.id)

        //getting the auth credential
//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        email = acct.email.toString()
        userSocialToken = acct.idToken.toString()
        userSocialType = acct.account.toString()

        // val name = acct.displayName.toString()
        // val mobileNo = acct.photoUrl.toString()

        // Hit Social API
        callApi()
    }

    //this method is called on click
    private fun googleSignIn() {
        //getting the google signin intent
        val signInIntent = mGoogleSignInClient!!.signInIntent

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Hit social login API
    private fun callApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            val map = HashMap<String, String>()
            map["email"] = "" + email
            map["user_social_token"] = "" + userSocialToken
            map["user_social_type"] = "" + userSocialType
            map["user_device_type"] = "android"
            map["user_device_token"] = "" + FCMtoken

          //  viewModel?.loadSocialLoginData(map)

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadSocialLoginData(map)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

            viewModel?.socialLoginModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        if (homeMainModel?.status == 0) {
                            Toast.makeText(
                                requireActivity(),
                                homeMainModel.message,
                                Toast.LENGTH_LONG
                            ).show()
                            // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        } else {
                            // Update Whole Model And Call onBackPressed..

                            /*
                            Saving user in shared Preferences
                            **/
                            ConfigurationPref(requireActivity()).user = homeMainModel?.toString()
                            ConfigurationPref(requireActivity()).token =
                                homeMainModel?.data?.userAuthentication
                            ConfigurationPref(requireActivity()).userId = homeMainModel?.data?.Id
                            ConfigurationPref(requireActivity()).userName =
                                homeMainModel?.data?.username
                            ConfigurationPref(requireActivity()).userGender =
                                homeMainModel?.data?.userGender
                            ConfigurationPref(requireActivity()).mobileNumber =
                                homeMainModel?.data?.phoneNumber
                            ConfigurationPref(requireActivity()).description =
                                homeMainModel?.data?.userDescription
                            ConfigurationPref(requireActivity()).email = homeMainModel?.data?.email
                            ConfigurationPref(requireActivity()).profilePic =
                                "" + homeMainModel?.data?.userImage

                            Toast.makeText(
                                requireActivity(),
                                "" + homeMainModel?.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            // Otp Match Successfully perform navigation
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            ResgistrationActivity.getRegActivity?.finish()
                        }

                    } else {
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                            .show()
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("Login", "Else Block" + homeMainModel?.data?.user_id)
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (e: Exception) {
            Log.d("Login", "SignUp")
            //ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
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
         * @return A new instance of fragment PreLoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PreLoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun setTitlebar(titlebar: Titlebar) {
    }
}