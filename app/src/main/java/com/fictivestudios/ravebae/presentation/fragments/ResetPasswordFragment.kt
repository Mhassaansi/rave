package com.fictivestudios.ravebae.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
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
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.viewmodels.OtpViewModel
import com.fictivestudios.ravebae.presentation.viewmodels.UpdatePasswordViewModel
import com.fictivestudios.ravebae.utils.CustomProgressDialogue
// import com.fictivestudios.ravebae.utils.ErrorDialog
import com.fictivestudios.ravebae.utils.GenericViewModelFactory
import com.fictivestudios.ravebae.utils.MyUtils
import com.fictivestudios.ravebae.utils.Titlebar
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.android.synthetic.main.fragment_reset_password.et_confirm_pass
import kotlinx.android.synthetic.main.fragment_reset_password.iv_show_pass
import kotlinx.android.synthetic.main.fragment_reset_password.view.*
import kotlinx.android.synthetic.main.fragment_reset_password.view.et_pass
import kotlinx.android.synthetic.main.fragment_reset_password.view.iv_show_pass
import kotlinx.android.synthetic.main.fragment_signup.*
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResetPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPasswordFragment : BBaseFragment<UpdatePasswordViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    private var isShowPass = false
    private var isShowPassReEnter = false


    override fun createViewModel(): UpdatePasswordViewModel {
        val factory = GenericViewModelFactory(UpdatePasswordViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(UpdatePasswordViewModel::class.java)
    }

    override fun setTitlebar(titlebar: Titlebar) {
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

        mView = inflater.inflate(R.layout.fragment_reset_password, container, false)

        mView.btn_reset.setOnClickListener {

            validateFields()
        }

        mView.iv_show_pass.setOnClickListener {
            if (isShowPass) {
                isShowPass = false
                iv_show_pass.setImageResource(R.drawable.ic_baseline_visibility_24)
                mView.et_pass.transformationMethod = PasswordTransformationMethod()
            } else {
                isShowPass = true
                iv_show_pass.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                mView.et_pass.transformationMethod = null
            }

        }

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

        mView.iv_show_pass_reenter.setOnClickListener {
            if (isShowPassReEnter) {
                isShowPassReEnter = false
                iv_show_pass_reenter.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                mView.et_confirm_pass.transformationMethod = PasswordTransformationMethod()
            } else {
                isShowPassReEnter = true
                iv_show_pass_reenter.setImageResource(R.drawable.ic_baseline_visibility_24)
                mView.et_confirm_pass.transformationMethod = null
            }

        }

        return mView
    }


    private fun validateFields() {

        if (mView.et_confirm_pass.text.toString().isNullOrBlank() &&
            mView.et_pass.text.toString().isNullOrBlank()
        ) {
            Toast.makeText(context, getString(R.string.fields_cant_be_empty), Toast.LENGTH_SHORT)
                .show()
            return
        }

        else if(mView.et_pass.text.toString().isNullOrBlank()){
            Toast.makeText(context, "Password "+getString(R.string.field_cant_be_empty), Toast.LENGTH_SHORT)
                .show()
            return
        }

        else if (mView.et_pass.text.toString().length < 8) {
            // mView.et_pass.setError(getString(R.string.password_must_be))
            Toast.makeText(context, getString(R.string.password_must_be), Toast.LENGTH_SHORT)
                .show()
            return
        }

        else if(mView.et_confirm_pass.text.toString().isNullOrBlank()){
            Toast.makeText(context, "Confirm Password "+getString(R.string.field_cant_be_empty), Toast.LENGTH_SHORT)
                .show()
            return
        }

        else if (mView.et_confirm_pass.text.toString().length < 8) {
            // mView.et_confirm_pass.setError(getString(R.string.password_must_be))
            Toast.makeText(context, getString(R.string.password_must_be), Toast.LENGTH_SHORT)
                .show()
            return
        }
        else if (!mView.et_confirm_pass.text.toString().equals(mView.et_pass.text.toString())) {
            // mView.et_confirm_pass.setError(getString(R.string.password_must_be))
            Toast.makeText(context, getString(R.string.password_doesnt_match), Toast.LENGTH_SHORT)
                .show()
            return
        }

        else {
            // Hit API and Navigate
//            ResgistrationActivity.getRegActivity
//                ?.navControllerReg?.navigate(R.id.loginFragment)
            callApi()
        }
    }

    private fun callApi() {
        try {
            val map = HashMap<String, String>()
            map["user_id"] = "" + ConfigurationPref(requireActivity()).userId
            map["new_password"] = ""+et_confirm_pass!!.text.toString()

            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadChangePasswordData(map)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }
//            viewModel?.loadChangePasswordData(map)

            viewModel?.updatePasswordModelData?.observe(viewLifecycleOwner) { homeMainModel ->
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
                            // Showing toast
                            Toast.makeText(
                                requireActivity(),
                                "" + homeMainModel?.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            // Password Successfully Changed perform navigation
                            ResgistrationActivity.getRegActivity
                                ?.navControllerReg?.navigate(R.id.loginFragment)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResetPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}