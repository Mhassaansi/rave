package com.fictivestudios.ravebae.presentation.fragments

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
import com.fictivestudios.imdfitness.activities.fragments.BaseFragment
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.adapter.MessagesAdapter
import com.fictivestudios.ravebae.presentation.viewmodels.LoginViewModel
import com.fictivestudios.ravebae.presentation.viewmodels.MessagesViewModel
import com.fictivestudios.ravebae.utils.CustomProgressDialogue
// import com.fictivestudios.ravebae.utils.ErrorDialog
import com.fictivestudios.ravebae.utils.GenericViewModelFactory
import com.fictivestudios.ravebae.utils.MyUtils
import com.fictivestudios.ravebae.utils.Titlebar
import kotlinx.android.synthetic.main.fragment_activity.view.*
import kotlinx.android.synthetic.main.fragment_messages.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MessagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MessagesFragment : BBaseFragment<MessagesViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var mView: View

    override fun createViewModel(): MessagesViewModel {
        val factory = GenericViewModelFactory(MessagesViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(MessagesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setHomeTitle(getString(R.string.messages))
        titlebar.showTitleBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_messages, container, false)


        // mView.rv_messages.adapter = MessagesAdapter()

      /*  mView.btn_write.setOnClickListener {

            MainActivity.getMainActivity
                ?.navController?.navigate(R.id.conversationFragment)
        }*/
        callApi()

        return mView
    }

    private fun callApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadFriendsListData()
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadFriendsListData()
            viewModel?.messagesModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.status == 1) {
                        /*
                         Pass data and populate UI Adapter
                         * */
                        Log.d("RequestSend", "Success if Block")
                        // Toast.makeText(requireActivity(),"Success",Toast.LENGTH_LONG).show()
                        // setReqReceived(homeMainModel.data)
                        mView.rv_messages.adapter = MessagesAdapter(homeMainModel.data, requireActivity())
                    } else {
                         if (homeMainModel?.message=="Unauthorized"){
                              ConfigurationPref(requireActivity()).clear()
                              startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                              MainActivity.getMainActivity?.finish()
                              Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                          }
                          else{
                             mView.rv_received.visibility = View.GONE
                             Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()

                         }

                        // Do nothing
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("RequestSend", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("RequestSend", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("RequestSend", "Catch Block")
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
         * @return A new instance of fragment MessagesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MessagesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}