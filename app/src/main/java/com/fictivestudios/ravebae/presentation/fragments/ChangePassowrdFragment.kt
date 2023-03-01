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
import com.fictivestudios.ravebae.presentation.viewmodels.ChangePasswordViewModel
import com.fictivestudios.ravebae.utils.CustomProgressDialogue
// import com.fictivestudios.ravebae.utils.ErrorDialog
import com.fictivestudios.ravebae.utils.GenericViewModelFactory
import com.fictivestudios.ravebae.utils.MyUtils
import com.fictivestudios.ravebae.utils.Titlebar
import kotlinx.android.synthetic.main.fragment_change_passowrd.*
import kotlinx.android.synthetic.main.fragment_change_passowrd.view.*
import kotlinx.android.synthetic.main.fragment_change_passowrd.view.iv_show_repeat_pass
import java.util.HashMap
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePassowrdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePassowrdFragment : BBaseFragment<ChangePasswordViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mView: View

    private var isShowRepeatPass = false
    private var isShowPass = false
    private var isShowNewPass = false

    override fun createViewModel(): ChangePasswordViewModel {
        val factory = GenericViewModelFactory(ChangePasswordViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(ChangePasswordViewModel::class.java)
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
        mView = inflater.inflate(R.layout.fragment_change_passowrd, container, false)

        mView.btn_save.setOnClickListener {
            validateFields()
        }


        mView.iv_show_pass.setOnClickListener {
            if (isShowPass) {
                isShowPass = false
                mView.iv_show_pass.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                mView.et_old_pass.transformationMethod = PasswordTransformationMethod()
            } else {
                isShowPass = true
                mView.iv_show_pass.setImageResource(R.drawable.ic_baseline_visibility_24)
                mView.et_old_pass.transformationMethod = null
            }

        }

        mView.iv_show_repeat_pass.setOnClickListener {
            if (isShowRepeatPass) {
                isShowRepeatPass = false
                mView.iv_show_repeat_pass.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                mView.et_reenter_pass.transformationMethod = PasswordTransformationMethod()
            } else {
                isShowRepeatPass = true
                mView.iv_show_repeat_pass.setImageResource(R.drawable.ic_baseline_visibility_24)
                mView.et_reenter_pass.transformationMethod = null
            }

        }

        mView.iv_show_new_pass.setOnClickListener {
            if (isShowNewPass) {
                isShowNewPass = false
                mView.iv_show_new_pass.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                mView.et_new_pass.transformationMethod = PasswordTransformationMethod()
            } else {
                isShowNewPass = true
                mView.iv_show_new_pass.setImageResource(R.drawable.ic_baseline_visibility_24)
                mView.et_new_pass.transformationMethod = null
            }

        }

        return mView
    }


    private fun validateFields() {

         if (mView.et_old_pass.text.toString().isNullOrBlank()&&
             mView.et_reenter_pass.text.toString().isNullOrBlank()&&
             mView.et_new_pass.text.toString().isNullOrBlank()
         )
         {
             Toast.makeText(context, getString(R.string.fields_cant_be_empty), Toast.LENGTH_SHORT).show()
             return
         }

        else if (mView.et_old_pass.text.toString().isNullOrBlank()) {
            // mView.et_old_pass.setError(getString(R.string.password_must_be))
            Toast.makeText(requireActivity(),getString(R.string.old_pass)+" "+getString(R.string.field_cant_be_empty),Toast.LENGTH_LONG).show()
             return
        }

        else if (!mView.et_old_pass.text.toString().isValidPassword()) {
            // mView.et_old_pass.setError(getString(R.string.password_must_be))
            Toast.makeText(requireActivity(),getString(R.string.password_must_be),Toast.LENGTH_LONG).show()
            return
        }

        else if (mView.et_new_pass.text.toString().isNullOrBlank()) {
            // mView.et_old_pass.setError(getString(R.string.password_must_be))
            Toast.makeText(requireActivity(),getString(R.string.new_pass)+" "+getString(R.string.field_cant_be_empty),Toast.LENGTH_LONG).show()
            return
        }

        else if (!mView.et_new_pass.text.toString().isValidPassword()) {
            // mView.et_new_pass.setError(getString(R.string.password_must_be))
            Toast.makeText(requireActivity(),getString(R.string.password_must_be),Toast.LENGTH_LONG).show()
            return
        }

/*

        if (!mView.et_reenter_pass.text.toString().isValidPassword() )
        {
            mView.et_reenter_pass.setError(getString(R.string.password_must_be))
            return
        }
*/

        else if (mView.et_new_pass.text.toString() != mView.et_reenter_pass.text.toString()) {
            // mView.et_reenter_pass.setError(getString(R.string.password_doesnt_match))
            Toast.makeText(requireActivity(),getString(R.string.password_doesnt_match),Toast.LENGTH_LONG).show()
            return
        }

        /* if (mView.et_reenter_pass.text.toString().length <8 )
         {
             mView.et_reenter_pass.setError(getString(R.string.password_must_be))
             return
         }*/

        else if (mView.et_old_pass.text.toString() == mView.et_new_pass.text.toString()) {
            // mView.et_new_pass.setError(getString(R.string.old_pass_and_new_pass_cant_be_same))
            Toast.makeText(requireActivity(),getString(R.string.old_pass_and_new_pass_cant_be_same),Toast.LENGTH_LONG).show()
            return
        } else {
            // MainActivity.getMainActivity?.onBackPressed()
            // Hit API and respective show toast
            callApi()
        }
    }

    private fun callApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())
            val map = HashMap<String, String>()
            map["user_id"] = "" + ConfigurationPref(requireActivity()).userId
            map["old_password"] = "" + et_old_pass.text.toString()
            map["new_password"] = "" + et_reenter_pass.text.toString()

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadChangePasswordData(map)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadChangePasswordData(map)
            viewModel?.changePasswordModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.status == 0
                    ) {
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
                     // MainActivity.getMainActivity?.onBackPressed()
                    } else {
                        Toast.makeText(requireActivity(), "" + homeMainModel?.message, Toast.LENGTH_LONG).show()
                        MainActivity.getMainActivity?.onBackPressed()
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("Login", "Else Block")
                    }


//                    if (homeMainModel?.error.equals(
//                            "no",
//                            ignoreCase = true
//                        )
//                    ) {
//                        Toast.makeText(requireActivity(), "" + homeMainModel?.message, Toast.LENGTH_SHORT).show()
//                        MainActivity.getMainActivity?.onBackPressed()
//                    } else {
//                        Toast.makeText(requireActivity(), "" + homeMainModel?.message, Toast.LENGTH_LONG).show()
//                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
//                        Log.d("Login", "Else Block")
//                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("Login", "Catch Block")
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            Toast.makeText(requireActivity(), "Invalid Credentials,please try again!", Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment ChangePassowrdFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChangePassowrdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun setTitlebar(titlebar: Titlebar) {
    }
}