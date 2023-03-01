package com.fictivestudios.ravebae.presentation.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.viewmodels.LoginViewModel
import com.fictivestudios.ravebae.utils.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.et_email
import kotlinx.android.synthetic.main.fragment_login.view.et_pass
import kotlinx.android.synthetic.main.fragment_login.view.iv_show_pass
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BBaseFragment<LoginViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    private var isShowPass = false

    override fun createViewModel(): LoginViewModel {
        val factory = GenericViewModelFactory(LoginViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_login, container, false)

        mView.btn_login.setOnClickListener {

            validateFields()
        }

        mView.tv_forgot_pass.setOnClickListener {
            ResgistrationActivity.getRegActivity
                ?.navControllerReg?.navigate(R.id.forgotFragment)
        }


//        makeTextLink(
//            mView.tv_dont_have_acc, "Signup Here", true, R.color.white
//        ) {
//            ResgistrationActivity.getRegActivity
//                ?.navControllerReg?.navigate(R.id.signupFragment)
//        }
        mView.tv_sign_up_here.setOnClickListener {
            ResgistrationActivity.getRegActivity?.navControllerReg?.navigate(R.id.signupFragment)
        }

        mView.tv_sign_up_here.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)

        mView.iv_show_pass.setOnClickListener {
            if (isShowPass) {
                isShowPass = false
                iv_show_pass.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                mView.et_pass.transformationMethod = PasswordTransformationMethod()
            } else {
                isShowPass = true
                iv_show_pass.setImageResource(R.drawable.ic_baseline_visibility_24)
                mView.et_pass.transformationMethod = null
            }

        }

        return mView
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun makeTextLink(
        textView: TextView,
        str: String,
        underlined: Boolean,
        color: Int?,
        action: (() -> Unit)? = null
    ) {
        val spannableString = SpannableString(textView.text)
        val textColor = color ?: textView.currentTextColor
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                action?.invoke()
            }

            override fun updateDrawState(drawState: TextPaint) {
                super.updateDrawState(drawState)
                drawState.isUnderlineText = underlined
                drawState.color = Color.WHITE
                drawState.typeface = Typeface.DEFAULT_BOLD
            }
        }
        val index = spannableString.indexOf(str)
        spannableString.setSpan(
            clickableSpan,
            index,
            index + str.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.WHITE

    }


    private fun validateFields() {

        if (mView.et_email.text.toString().isNullOrBlank()&&
            mView.et_pass.text.toString().isNullOrBlank()
        )
        {
            Toast.makeText(context, getString(R.string.fields_cant_be_empty), Toast.LENGTH_SHORT).show()
            return
        }

        else if (mView.et_email.text.toString().isNullOrBlank()
        )
        {
            Toast.makeText(context, getString(R.string.email)+" "+getString(R.string.field_cant_be_empty), Toast.LENGTH_SHORT).show()
            return
        }

        else if (!Constants.isValidEmail(mView.et_email.text.toString())) {
            // mView.et_email.setError(getString(R.string.invalid_email_format))
            Toast.makeText(requireActivity(),getString(R.string.invalid_email_format),Toast.LENGTH_LONG).show()
            return
        }
        else if (mView.et_pass.text.toString().isNullOrBlank()
        )
        {
            Toast.makeText(context, getString(R.string.password)+" "+getString(R.string.field_cant_be_empty), Toast.LENGTH_SHORT).show()
            return
        }
        else if (!mView.et_pass.text.toString().isValidPassword()) {
            // mView.et_pass.setError(getString(R.string.password_must_be))
            Toast.makeText(requireActivity(),getString(R.string.password_must_be),Toast.LENGTH_LONG).show()
            return
        } else {
            // Hit API with viewModel
            callApi()
        }
    }

    private fun callApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())
            val map = HashMap<String, String>()
            map["email"] = et_email?.text.toString().trim { it <= ' ' }
            map["password"] = et_pass?.text.toString()
            map["user_device_type"] = "android"
            map["user_device_token"] = "" + FCMtoken

            // map["email"] = "test@yopmail.com"
            // map["password"] = "Abcd@123"

            if(MyUtils.isNetworkAvailable(requireActivity())){
            viewModel?.loadLoginData(map)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadLoginData(map)
            viewModel?.loginModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        /*
                         Saving user in shared Preferences
                         * */
                        ConfigurationPref(requireActivity()).user = homeMainModel?.toString()
                        ConfigurationPref(requireActivity()).token = homeMainModel?.data?.userAuthentication
                        ConfigurationPref(requireActivity()).userId = homeMainModel?.data?.Id
                        ConfigurationPref(requireActivity()).userName = homeMainModel?.data?.username
                        ConfigurationPref(requireActivity()).userGender = homeMainModel?.data?.userGender
                        ConfigurationPref(requireActivity()).mobileNumber = homeMainModel?.data?.phoneNumber
                        ConfigurationPref(requireActivity()).description = homeMainModel?.data?.userDescription
                        ConfigurationPref(requireActivity()).email = homeMainModel?.data?.email
                        ConfigurationPref(requireActivity()).profilePic = "" + homeMainModel?.data?.userImage
                        ConfigurationPref(requireActivity()).coverPic = "" + homeMainModel?.data?.coverImage
                        next()
                    } else {
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                        Log.d("Login", "Else Block"+homeMainModel?.data?.isVerified)

                        homeMainModel?.data?.isVerified?.let {
                            if(it == 0) {
                                // Case when
                                // Navigate to verify otp screen..
                                val bundle = Bundle()
                                // Giving key sign up because need same behaviour
                                // as signup in this case
                                bundle.putString("from", "SignUp")
                                ResgistrationActivity.getRegActivity?.navControllerReg?.navigate(R.id.otpFragment, bundle)
                            }
                        }

                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("Login", "Catch Block")
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            Toast.makeText(requireActivity(), "Invalid Credentials,please try again!", Toast.LENGTH_LONG).show()
        }
    }

    operator fun next() {
        // Data Coming from SignUp Fragment
//        val bundle = Bundle()
//        bundle.putString("from", "SignUp")
        val intent = Intent(requireContext(), MainActivity::class.java)
//        intent.putExtras(bundle)

        startActivity(intent)
        //startActivity(Intent(context, MainActivity::class.java))
        ResgistrationActivity?.getRegActivity?.finish()
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

    fun CharSequence.isValidPassword(): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(this)
        return matcher.matches()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun setTitlebar(titlebar: Titlebar) {
    }


}