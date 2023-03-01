package com.fictivestudios.ravebae.presentation.fragments

// import com.fictivestudios.ravebae.utils.ErrorDialog

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
import com.fictivestudios.ravebae.presentation.adapter.RequestReceivedAdapter
import com.fictivestudios.ravebae.presentation.adapter.RequestSentAdapter
import com.fictivestudios.ravebae.presentation.models.Datab
import com.fictivestudios.ravebae.presentation.models.Datan
import com.fictivestudios.ravebae.presentation.viewmodels.ActivityRequestSendRecievedRequestViewModel
import com.fictivestudios.ravebae.utils.CustomProgressDialogue
import com.fictivestudios.ravebae.utils.GenericViewModelFactory
import com.fictivestudios.ravebae.utils.MyUtils
import com.fictivestudios.ravebae.utils.Titlebar
import kotlinx.android.synthetic.main.fragment_activity.view.*
import kotlinx.android.synthetic.main.fragment_signup.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val FCM_TYPE = "fcm_type"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActivityFragment : BBaseFragment<ActivityRequestSendRecievedRequestViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var mView: View

    private var requestReceivedAdapter: RequestReceivedAdapter? = null

    override fun createViewModel(): ActivityRequestSendRecievedRequestViewModel {
        val factory =
            GenericViewModelFactory(ActivityRequestSendRecievedRequestViewModel(requireActivity()))
        return ViewModelProvider(
            this,
            factory
        ).get(ActivityRequestSendRecievedRequestViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(FCM_TYPE)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setHomeTitle(getString(R.string.activity))
        titlebar.showTitleBar()
    }

    override fun onStart() {
        super.onStart()

        var position: Int = mView.buttonGroup_segmented.getPosition()
        position = --position
        mView.buttonGroup_segmented.setPosition(position, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_activity, container, false)

        mView.btn_sent.setOnClickListener {

//            mView.rv_sent.visibility = View.VISIBLE
//            mView.rv_received.visibility = View.GONE
//            setReqReceived()

            callSendRequestApi()
            // All work under success response..
        }

        mView.btn_received.setOnClickListener {

//            mView.rv_received.visibility = View.VISIBLE
//            mView.rv_sent.visibility = View.GONE
//            setReqSent()

            callRecieveRequestApi()
        }

        if(param1 != null){
            if(param1.equals("request_notify",ignoreCase = true)){
                // Coming from friend request notification..
                callRecieveRequestApi()

                var position: Int = mView.buttonGroup_segmented.getPosition()
                position = ++position
                mView.buttonGroup_segmented.setPosition(position, false)
                //mView.buttonGroup_segmented.setPosition(2,false)
                // mView.buttonGroup_segmented.setPosition(2, true)

//                mView.btn_received.setBackground(R.drawable.round_button_bg)
//                mView.btn_sent.setBackground(R.drawable.round_button_white_bg)
                // buttonGroup_segmented
            } else {
                // API Hit
                callSendRequestApi()
            }
        } else {
            // API Hit
//            var position: Int = mView.buttonGroup_segmented.getPosition()
//            position = --position
//            mView.buttonGroup_segmented.setPosition(position, false)
            callSendRequestApi()
        }

        // API Hit
//        callSendRequestApi()

//        setReqSent()
//        setReqReceived()

        return mView
    }

    private fun setReqReceived(users: List<Datab>?) {
        mView.rv_sent.adapter = RequestSentAdapter(users)
    }

    private fun setReqSent(users: List<Datan>?) {
        requestReceivedAdapter = RequestReceivedAdapter(users)
        mView.rv_received.adapter = requestReceivedAdapter
    }

    private fun callSendRequestApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadSendRequestData()
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }
//            viewModel?.loadSendRequestData()
            viewModel?.sendRequestModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.status == 1) {
                        /*
                         Pass data and populate UI Adapter
                         * */
                        Log.d("RequestSend", "Success if Block")
                        // Toast.makeText(requireActivity(),""+ homeMainModel.message,Toast.LENGTH_LONG).show()

                        mView.rv_sent.visibility = View.VISIBLE
                        mView.rv_received.visibility = View.GONE
                        setReqReceived(homeMainModel.data)
                    } else {
                         if (homeMainModel?.message=="Unauthorized"){
                              ConfigurationPref(requireActivity()).clear()
                              startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                              MainActivity.getMainActivity?.finish()
                              Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                          }
                          else{
                             mView.rv_received.visibility = View.GONE
                          }

                        // Do nothing
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                     //   Toast.makeText(requireActivity(),""+ homeMainModel?.message,Toast.LENGTH_LONG).show()
                        Log.d("RequestSend", "Else Block")

                    }
                } catch (e: Exception) {
                    Log.d("RequestSend", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("RequestSend", "Catch Block")
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            Toast.makeText(
                requireActivity(),
                "Invalid Credentials,please try again!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun callRecieveRequestApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadRequestRecievedData()
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadRequestRecievedData()
            viewModel?.requestRecievedViewModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.status == 1) {
                        /*
                         Pass Data and populate UI Adapter
                         * */
                        Log.d("RequestReceived", "if Block")
//                        Toast.makeText(requireActivity(),"Success",Toast.LENGTH_LONG).show()

                        mView.rv_received.visibility = View.VISIBLE
                        mView.rv_sent.visibility = View.GONE
                        setReqSent(homeMainModel.data)

                        onOptionsClick()
                    } else {
                         if (homeMainModel?.message=="Unauthorized"){
                              ConfigurationPref(requireActivity()).clear()
                              startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                              MainActivity.getMainActivity?.finish()
                              Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                          }
                          else{
                             setReqSent(homeMainModel?.data)
                             Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                                  .show()
                          }

                        // Do Nothing
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("RequestReceived", "Else Block")
                     //   Toast.makeText(requireActivity(),""+ homeMainModel?.message,Toast.LENGTH_LONG).show()

                        mView.rv_sent.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    Log.d("RequestReceived", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("RequestReceived", "Catch Block")
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            Toast.makeText(requireActivity(), "Invalid Credentials,please try again!", Toast.LENGTH_LONG).show()
        }
    }

    /** Interface click listener */
    private fun onOptionsClick() {
        requestReceivedAdapter?.setOnButtonClickListener(object :
            RequestReceivedAdapter.OnButtonClickListener {
            override fun onButtonCLick(
                position: Int,
                model: Datan?,
                action: String?
            ) {
                // Hit API for add friend
                if (action.equals("addfriend", ignoreCase = true)) {
                    callAddRemoveFriendApi(model?.Id, 1)
                } else {
                    callAddRemoveFriendApi(model?.Id, 2)
                }
//                callAddRemoveFriendApi(model?.senderId?.Id)

//                Toast.makeText(
//                    requireActivity(),
//                    "POSITION:" + position + " USERID:" + model?.userId?.Id,
//                    Toast.LENGTH_LONG
//                ).show()
            }
        })
    }

    private fun callAddRemoveFriendApi(id: String?, status: Int?) {
        try {
            val map = HashMap<String, String>()
            map["request_id"] = "" + id
            map["request_status"] = "" + status

            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadAcceptRejectData(map)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadAcceptRejectData(map)
            viewModel?.acceptRejectModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.status == 1) {
                        // Check multiple toast issue resolved with SingleLiveEvent class
                        //Toast.makeText(requireActivity(),""+homeMainModel.message,Toast.LENGTH_LONG).show()
                        // API hit for updated response
                        callRecieveRequestApi()
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
                        // Do Nothing
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("RequestReceived", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("RequestReceived", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("RequestReceived", "Catch Block")
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            // Toast.makeText(requireActivity(),""+homeMainModel.message,Toast.LENGTH_LONG).show()
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
         * @return A new instance of fragment ActivityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ActivityFragment().apply {
                arguments = Bundle().apply {
                    putString(FCM_TYPE, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}