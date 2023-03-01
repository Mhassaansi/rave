package com.fictivestudios.ravebae.presentation.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fictivestudios.imdfitness.activities.fragments.BaseFragment
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.models.ContentModel
import com.fictivestudios.ravebae.presentation.viewmodels.PreferenceViewModel
import com.fictivestudios.ravebae.presentation.viewmodels.TermsAndConditionViewModel
import com.fictivestudios.ravebae.utils.CustomProgressDialogue
// import com.fictivestudios.ravebae.utils.ErrorDialog
import com.fictivestudios.ravebae.utils.GenericViewModelFactory
import com.fictivestudios.ravebae.utils.MyUtils
import com.fictivestudios.ravebae.utils.Titlebar
import kotlinx.android.synthetic.main.fragment_privacy_and_policy.view.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_terms_and_conditions.view.*
import kotlinx.android.synthetic.main.fragment_terms_and_conditions.view.btn_back
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TermsAndConditionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TermsAndConditionsFragment : BBaseFragment<TermsAndConditionViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mView: View

    private var wv1: WebView? = null

    override fun createViewModel(): TermsAndConditionViewModel {
        val factory = GenericViewModelFactory(TermsAndConditionViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(TermsAndConditionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {
/*        titlebar.setBtnBack(getString(R.string.terms),R.color.purple)*/

        titlebar.resetTitlebar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false)

        mView.btn_back.setOnClickListener {

            (context as Activity).onBackPressed()
        }

        // callApi(mView)

        val url: String = "http://ravebae.org/terms-and-conditions"

        wv1 = mView.termsWebView

        // wv1?.webViewClient = MyBrowser()
        wv1?.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                //change your progress bar // 100
                if (newProgress == 80) {
                    // binding.loaderProgressBar.visibility = View.GONE
                }
                Log.d("progress", "newProgress: " + newProgress)
            }
        })

        wv1?.settings?.loadsImagesAutomatically = true
        wv1?.settings?.javaScriptEnabled = true
        // Added
        wv1?.settings?.javaScriptCanOpenWindowsAutomatically = true
        wv1?.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        url?.let { wv1?.loadUrl(it) }

        return mView
    }

    @SuppressLint("SetTextI18n")
    private fun setData(mView: View, contentModel: ContentModel){
//        mView.tv_data.text = "" + contentModel.message?.get(0)?.content
        mView.tv_data.text = "" + contentModel.data?.get(0)?.content
    }

    private fun callApi(view: View) {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadTermsAndConditionData()
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadTermsAndConditionData()
            viewModel?.termsAndConditionModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
//                    homeMainModel?.error.equals(
//                        "no",
//                        ignoreCase = true )
                    if (homeMainModel?.status == 1) {
                       // Success display data
                       setData(view, homeMainModel!!)
                    } else {
                        // Donothing,  error toast in viewmodel
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("TermsAndCondition", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("TermsAndCondition", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("TermsAndCondition", "Catch Block")
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
         * @return A new instance of fragment TermsAndConditionsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TermsAndConditionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}