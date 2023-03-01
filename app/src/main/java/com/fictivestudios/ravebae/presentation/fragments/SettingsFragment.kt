package com.fictivestudios.ravebae.presentation.fragments

import android.Manifest
import android.content.Intent
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
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.viewmodels.SettingsViewModel
import com.fictivestudios.ravebae.utils.*
// import com.fictivestudios.ravebae.utils.ErrorDialog
import kotlinx.android.synthetic.main.fragment_settings.view.*
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : BBaseFragment<SettingsViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View


    override fun createViewModel(): SettingsViewModel {
        val factory = GenericViewModelFactory(SettingsViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(SettingsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setBtnBack("", R.color.purple)
        titlebar.showTitleBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_settings, container, false)

        mView.btn_logout.setOnClickListener {
            // Hit API and onsuccessful response clear preference and navigate..
            callLogoutApi()
        }

        mView.btn_delete_user.setOnClickListener {
            activity?.showDeleteUserRequestDialog(
                ""+getString(R.string.delete_account),
                getString(R.string.sure_delete_account)
            ) {
                // requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                // call api on ok
                callDeleteUserApi()
            }
        }

        mView.btn_term.setOnClickListener {

            MainActivity.getMainActivity
                ?.navController?.navigate(R.id.termsAndConditionsFragment)
        }

        mView.btn_help.setOnClickListener {

            MainActivity.getMainActivity
                ?.navController?.navigate(R.id.privacyAndPolicyFragment)
        }

        mView.btn_change_pas.setOnClickListener {

            MainActivity.getMainActivity
                ?.navController?.navigate(R.id.changePassowrdFragment)
        }

        mView.notification_combobox.setOnCheckedChangeListener(null);
        // By default changing checked status based on previous selected value
        // Do something with the check status
        ConfigurationPref(requireActivity()).notificationState?.let {
                // Toggle to false
                mView.notification_combobox.setChecked(it)
        }
        // mView.notification_combobox.setChecked(true);
        //switch.setOnCheckChangedListener(mOnCheckChangedListener);
        // .setOnCheckedChangeListener { buttonView, isChecked ->
        mView.notification_combobox.setOnCheckedChangeListener { buttonView, isChecked ->
            val notification_on_off: String = if (mView.notification_combobox.isChecked) {
                "on"
            } else {
                "off"
            }

            callNotificationApi("" + notification_on_off)
        }

        ConfigurationPref(requireActivity()).from?.let {
            if (it.equals("google", ignoreCase = true) ||
                it.equals("facebook", ignoreCase = true)
            ) {
                mView.btn_change_pas.visibility = View.GONE
            }
        }

        return mView
    }

    private fun callDeleteUserApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadDeleteProfileData("" + ConfigurationPref(requireActivity()).userId)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadLogoutData(map)
            viewModel?.deleteProfileModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.status == 1
                    ) {
                        // Testing purpose..
                        ConfigurationPref(requireActivity()).clear()

                        startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                        MainActivity.getMainActivity?.finish()

                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                            .show()
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
                        Log.d("Login", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("delete", "Catch Block:"+ex)
            Toast.makeText(
                requireActivity(),
                "Invalid Credentials,please try again!",
                Toast.LENGTH_LONG
            ).show()
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
        }
    }

    private fun callLogoutApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())
            val map = HashMap<String, String>()
            map["user_id"] = "" + ConfigurationPref(requireActivity()).userId

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadLogoutData(map)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadLogoutData(map)
            viewModel?.logoutModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        // Testing purpose..
                        ConfigurationPref(requireActivity()).clear()

                        startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                        MainActivity.getMainActivity?.finish()

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
                        Log.d("Login", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("Login", "Catch Block")
            Toast.makeText(
                requireActivity(),
                "Invalid Credentials,please try again!",
                Toast.LENGTH_LONG
            ).show()
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
        }
    }

    private fun callNotificationApi(notification: String) {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())
            val map = HashMap<String, String>()
            map["is_notification"] = "" + notification

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadNotificationData(map)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }
//            viewModel?.loadNotificationData(map)
            viewModel?.notificationModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.status?.equals(0)!!) {

                         if (homeMainModel?.message=="Unauthorized"){
                              ConfigurationPref(requireActivity()).clear()
                              startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                              MainActivity.getMainActivity?.finish()
                              Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                          }
                          else{
                             ConfigurationPref(requireActivity()).notificationState = false
                              Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                                  .show()
                          }

                        // Off state

                    } else {
                        // On State
                        ConfigurationPref(requireActivity()).notificationState = true
                        Toast.makeText(
                            requireActivity(),
                            "" + homeMainModel.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("Notification", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("Notification", "Catch Block")
                    Toast.makeText(
                        requireActivity(),
                        "" + homeMainModel?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (ex: Exception) {
            Log.d("Notification", "Catch Block")
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
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}