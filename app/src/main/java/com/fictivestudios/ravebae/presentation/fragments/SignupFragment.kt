package com.fictivestudios.ravebae.presentation.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.viewmodels.RegisterViewModel
import com.fictivestudios.ravebae.utils.*
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import okhttp3.MultipartBody
import java.io.File
import java.util.regex.Pattern


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var isShowPass = false
private var isShowRepeatPass = false

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : BBaseFragment<RegisterViewModel>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View

    private var imageAddress: String? = null

    override fun createViewModel(): RegisterViewModel {
        val factory = GenericViewModelFactory(RegisterViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_signup, container, false)

        mView.btn_create?.setOnClickListener {

            validateFields()
        }

        mView.btn_upload.setOnClickListener {

            openImagePicker()

        }
        mView.iv_profile.setOnClickListener {
            openImagePicker()
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

        mView.iv_show_repeat_pass.setOnClickListener {
            if (isShowRepeatPass) {
                isShowRepeatPass = false
                iv_show_repeat_pass.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                mView.et_confirm_pass.transformationMethod = PasswordTransformationMethod()
            } else {
                isShowRepeatPass = true
                iv_show_repeat_pass.setImageResource(R.drawable.ic_baseline_visibility_24)
                mView.et_confirm_pass.transformationMethod = null
            }

        }

        // access the items of the list
        val gender = resources.getStringArray(R.array.Gender)

        // For gender spinner
        // access the spinner
        if (mView.sp_gender != null) {
            val adapter = ArrayAdapter(
                requireActivity(),
                R.layout.simple_list_item_light, gender
            )
            mView.sp_gender.adapter = adapter
            setSpListener()
        }

        // Text Watcher, it will format your number entry correctly based on your locale
        // mView.et_number.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        // mView.et_number.addTextChangedListener(PhoneNumberFormattingTextWatcher())


        return mView
    }

    /** Listener for Gender Spinner*/
    private fun setSpListener() {
        mView.sp_gender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i == 0) {
                    // tvSpGenderTitle?.visibility = View.VISIBLE
                } else {
                    // tvSpGenderTitle?.visibility = View.GONE
                    Log.d("Selected Item", "Value: " + mView.sp_gender.selectedItem)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun openImagePicker() {
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()

    }

    private fun validateFields() {

        if (
            mView.et_username.text.toString().isNullOrBlank()
        ) {
            // mView.et_username.setError(getString(R.string.fields_cant_be_empty))
            Toast.makeText(
                requireActivity(),
                getString(R.string.username) + " " + getString(R.string.field_cant_be_empty),
                Toast.LENGTH_LONG
            ).show()


            return
        } else if (mView.et_email.text.toString().isNullOrBlank()) {
            // mView.et_email.setError(getString(R.string.fields_cant_be_empty))
            Toast.makeText(
                requireActivity(),
                getString(R.string.email) + " " + getString(R.string.field_cant_be_empty),
                Toast.LENGTH_LONG
            ).show()


            return
        } else if (!Constants.isValidEmail(mView.et_email.text.toString())) {
            // mView.et_email.setError(getString(R.string.invalid_email_format))
            Toast.makeText(
                requireActivity(),
                getString(R.string.invalid_email_format),
                Toast.LENGTH_LONG
            ).show()


            return
        } else if (mView.et_pass.text.toString().isNullOrBlank()) {
            // mView.et_pass.setError(getString(R.string.password_must_be))
            Toast.makeText(
                requireActivity(),
                getString(R.string.password) + " " + getString(R.string.field_cant_be_empty),
                Toast.LENGTH_LONG
            ).show()

            return
        } else if (!mView.et_pass.text.toString().isValidPassword()) {
            // mView.et_pass.setError(getString(R.string.password_must_be))
            Toast.makeText(
                requireActivity(),
                getString(R.string.password_must_be),
                Toast.LENGTH_LONG
            ).show()

            return
        } else if (mView.et_pass.text.toString() != mView.et_confirm_pass.text.toString()) {
            // mView.et_confirm_pass.setError(getString(R.string.confirm_pass_doesnt_match))
            Toast.makeText(
                requireActivity(),
                getString(R.string.confirm_pass_doesnt_match),
                Toast.LENGTH_LONG
            ).show()

            return
        } else if (
            mView.et_number.text.toString().isNullOrBlank()
        ) {
            // mView.et_number.setError(getString(R.string.fields_cant_be_empty))
            Toast.makeText(
                requireActivity(),
                getString(R.string.number) + " " + getString(R.string.field_cant_be_empty),
                Toast.LENGTH_LONG
            ).show()

            return
        }
//        else if (
//            !isValidMobileNo(mView.et_number.text.toString())
//        ) {
//            // mView.et_number.setError(getString(R.string.fields_cant_be_empty))
//            Toast.makeText(
//                requireActivity(),
//                getString(R.string.number)+" "+ getString(R.string.invalid_number_format),
//                Toast.LENGTH_LONG
//            ).show()
//
//            return
//        }
        else if (
            mView.et_description.text.toString().isNullOrBlank()
        ) {
            // mView.et_description.setError(getString(R.string.fields_cant_be_empty))
            Toast.makeText(
                requireActivity(),
                getString(R.string.description) + " " + getString(R.string.fields_cant_be_empty),
                Toast.LENGTH_LONG
            ).show()

            return
        }

        // Check if first value is selected of array
        else if (
            mView.sp_gender.selectedItem.toString().equals("Select Gender", ignoreCase = true)
        ) {
            // ErrorDialog.showErrorDialog(requireActivity(), getString(R.string.please_specify_gender))
            Toast.makeText(
                requireActivity(),
                getString(R.string.please_specify_gender),
                Toast.LENGTH_LONG
            ).show()
            return
        }

//        if (
//            mView.et_username.text.toString().isNullOrBlank()
//        ) {
//            mView.et_username.setError(getString(R.string.fields_cant_be_empty))
//
//            return
//        }
        else {
            // Hit API and Navigate.
            callApi()
//            Constants.isAccountLogin = true
//            ResgistrationActivity.getRegActivity
//               ?.navControllerReg?.navigate(R.id.otpFragment)

        }
    }

    private fun callApi() {
        try {
            var profilePart: MultipartBody.Part? = null

            if (!image.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + image.get())
                profilePart = File(image.get() ?: "").getPartMap("user_image")
            }

            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if (MyUtils.isNetworkAvailable(requireActivity())) {
                // Has to add spinner for user gender
                viewModel?.loadRegisterData(
                    et_username!!.text.toString(),
                    et_email!!.text.toString(),
                    profilePart,
                    et_pass!!.text.toString(),
                    et_number.unMaskedText.toString(),
                    "" + mView.sp_gender.selectedItem,
                    et_description.text.toString(),
                    "android",
                    "" + FCMtoken
                )
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.no_internet),
                    Toast.LENGTH_LONG
                ).show()
            }

            // Has to add spinner for user gender
//            viewModel?.loadRegisterData(
//                et_username!!.text.toString(),
//                et_email!!.text.toString(),
//                profilePart,
//                et_pass!!.text.toString(),
//                et_number.text.toString(),
//                "" + mView.sp_gender.selectedItem,
//                et_description.text.toString(),
//                "android",
//                "" + FCMtoken
//            )

            viewModel?.registerModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        // Has to check this condition
                        if (homeMainModel?.status == 0) {
                            // Email Already Exist Case, Show Dialog
                            // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                            Toast.makeText(
                                requireActivity(),
                                homeMainModel.message,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val bundle = Bundle()
                            bundle.putString("from", "SignUp")

                            // Changing and saving data after otp succes hit
                            // Saving userId
                            ConfigurationPref(requireActivity()).userId =
                                homeMainModel?.data?.userId
                            // Saving userGender
                            ConfigurationPref(requireActivity()).userGender =
                                "" + mView.sp_gender.selectedItem

                            Constants.isAccountLogin = true
                            ResgistrationActivity.getRegActivity
                                ?.navControllerReg?.navigate(R.id.otpFragment, bundle)
                        }

                    } else {
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                            .show()
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("Login", "Else Block" + homeMainModel?.data?.userId)
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (e: Exception) {
            Log.d("Login", "SignUp")
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
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

    private var image: ObservableField<String> = ObservableField("")

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            // val imageForUpload = data?.extras?.get("data") as Bitmap?

            val imageForUpload = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        requireContext().contentResolver,
                        uri
                    )
                )
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            }

            // Image for uploading to server
//            imageAddress = imageForUpload?.let {
//                ImageUtil.saveToInternalStorage(
//                    activity, "fileName.png", it
//                )
//            } + "/fileName.png"

            // mView.iv_profile_image.setImageURI(uri)

            image.set(activity?.let {
                imageForUpload?.let {
                    ImageUtil.saveToInternalStorage(
                        activity, "fileName.png", it
                    )
                } + "/fileName.png"
            })
            // mView.iv_profile_image.setImageURI(uri)

            // Use Uri object instead of File to avoid storage permissions
            mView.iv_profile.setImageURI(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}