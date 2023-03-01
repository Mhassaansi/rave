package com.fictivestudios.ravebae.presentation.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fictivestudios.imdfitness.activities.fragments.BaseFragment
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.viewmodels.OtpViewModel
import com.fictivestudios.ravebae.presentation.viewmodels.RegisterViewModel
import com.fictivestudios.ravebae.utils.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_otp.view.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "from"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OtpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OtpFragment : BBaseFragment<OtpViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    // SignUp
    private var from: String? = null
    private var timer: CountDownTimer? = null

    override fun createViewModel(): OtpViewModel {
        val factory = GenericViewModelFactory(OtpViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(OtpViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            from = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_otp, container, false)

        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mView.tv_didnt_Receive_code.isEnabled = false
                if ((millisUntilFinished / 1000).toString().length == 1) {
                    mView.tv_timer.setText("00:0" + (millisUntilFinished / 1000).toString())
                } else {
                    mView.tv_timer.setText("00:" + (millisUntilFinished / 1000).toString())
                }

                // mView.tv_timer.setText("00:" + (millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                mView.tv_didnt_Receive_code.isEnabled = true
            }
        }
        timer?.start()

        mView.btn_send.setOnClickListener {
            if (Constants.isAccountLogin) {

                if (mView.et_otp.text.toString().isNullOrBlank()
                ) {
                    Toast.makeText(
                        context,
                        getString(R.string.fields_cant_be_empty),
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
//                    startActivity(Intent(requireContext(), MainActivity::class.java))
//                    ResgistrationActivity.getRegActivity?.finish()

                    // Hit API and OnResponse Navigate..
                    callApi()
                }

            } else {
                if (mView.et_otp.text.toString().isNullOrBlank()
                ) {
                    Toast.makeText(
                        context,
                        getString(R.string.fields_cant_be_empty),
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                        callApi()


                }

            }
        }

        makeTextLink(
            mView.tv_didnt_Receive_code, "Send Again", true, R.color.white
        ) {
            callResendApi()

            // Restart timer
            timer?.cancel()
            timer = object : CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    mView.tv_didnt_Receive_code.isEnabled = false

                    if ((millisUntilFinished / 1000).toString().length == 1) {
                        mView.tv_timer.setText("00:0" + (millisUntilFinished / 1000).toString())
                    } else {
                        mView.tv_timer.setText("00:" + (millisUntilFinished / 1000).toString())
                    }

                    // mView.tv_timer.setText("00:" + (millisUntilFinished / 1000).toString())
                }

                override fun onFinish() {
                    mView.tv_didnt_Receive_code.isEnabled = true
                }
            }
            timer?.start()
        }

        // mView.tv_didnt_Receive_code.setText(Html.fromHtml(String.format(getString(R.string.didnt_received_send_again_hyper))))

        // Resend Code
//        mView.tv_didnt_Receive_code.setOnClickListener {
//            callResendApi()
//        }


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
        // textView.highlightColor = Color.WHITE // white

    }


    private fun callApi() {
        try {
            val map = HashMap<String, String>()
            map["user_id"] = "" + ConfigurationPref(requireActivity()).userId
            map["verification_code"] = et_otp!!.text.toString()

            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadOtpData(map)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

            viewModel?.OtpModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        if (homeMainModel?.status == 0) {
                            // Otp Unsucessful, Show Dialog
                            // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                            Toast.makeText(requireActivity(), homeMainModel.message, Toast.LENGTH_LONG).show()
                        } else {

                            if(from.equals("SignUp")){
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


                                // Pervious in sign up // now added here..
                                // Constants.isAccountLogin = true

                                // Data Coming from SignUp Fragment
                                val bundle = Bundle()
                                bundle.putString("from", "" + from)
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.putExtras(bundle)

                                // Otp Match Successfully perform navigation
                                //startActivity(Intent(requireContext(), MainActivity::class.java))
                                startActivity(intent)
                                ResgistrationActivity.getRegActivity?.finish()
                            }
                            else{


                                ResgistrationActivity.getRegActivity?.navControllerReg
                                    ?.navigate(R.id.resetPasswordFragment)
                            }

                        }

                    } else {
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                        Log.d("Login", "Else Block" + homeMainModel?.data)
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (e: Exception) {
            Log.d("Login", "SignUp")
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            Toast.makeText(requireActivity(), "Invalid Credentials,please try again!", Toast.LENGTH_LONG).show()
        }
    }

    private fun callResendApi() {
        try {
            val map = HashMap<String, String>()
            map["user_id"] = "" + ConfigurationPref(requireActivity()).userId

            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadResendOtpData(map)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadResendOtpData(map)
            viewModel?.OtpResendModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        if (homeMainModel?.status == 0) {
                            // Resend API call succesful, Show Dialog, Code sended to email
                            // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                            Toast.makeText(
                                requireActivity(),
                                homeMainModel?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Resend API call unsuccesful
                            // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                            Toast.makeText(
                                requireActivity(),
                                homeMainModel?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                             viewModel?.OtpResendModelData?.value = null
                        }

                    } else {
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                        Log.d("Login", "Else Block" + homeMainModel?.data)
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (e: Exception) {
            Log.d("Login", "SignUp")
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            Toast.makeText(requireActivity(), "Invalid Credentials,please try again!", Toast.LENGTH_LONG).show()
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
         * @return A new instance of fragment OtpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OtpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}