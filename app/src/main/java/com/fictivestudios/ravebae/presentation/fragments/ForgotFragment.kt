package com.fictivestudios.ravebae.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.viewmodels.ForgotViewModel
import com.fictivestudios.ravebae.utils.*
import kotlinx.android.synthetic.main.fragment_forgot.*
import kotlinx.android.synthetic.main.fragment_forgot.view.*
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotFragment : BBaseFragment<ForgotViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    override fun createViewModel(): ForgotViewModel {
        val factory = GenericViewModelFactory(ForgotViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(ForgotViewModel::class.java)
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
        mView = inflater.inflate(R.layout.fragment_forgot, container, false)

        mView.btn_send.setOnClickListener {

            Constants.isAccountLogin = false

            validateFields()
        }

        return mView
    }


    private fun validateFields() {

        if (mView.et_email.text.toString().isNullOrBlank()
        ) {
            Toast.makeText(context, getString(R.string.fields_cant_be_empty), Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            // Hit API, save access token and navigate
            callApi()
//            ResgistrationActivity.getRegActivity
//                ?.navControllerReg?.navigate(R.id.otpFragment)
        }
    }

    private fun callApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())
            val map = HashMap<String, String>()
            map["email"] = "" + et_email.text.toString()

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadForgotData(map)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadForgotData(map)
            viewModel?.forgotModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        ConfigurationPref(requireActivity()).userId = homeMainModel?.data?.user_id

                        ResgistrationActivity.getRegActivity
                            ?.navControllerReg?.navigate(R.id.otpFragment)
                    } else {
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                        //ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("Forgot", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("Forgot", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("Forgot", "Catch Block")
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
         * @return A new instance of fragment ForgotFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgotFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    override fun setTitlebar(titlebar: Titlebar) {
    }
}