package com.fictivestudios.ravebae.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.Coil
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.models.ProfileModel
import com.fictivestudios.ravebae.presentation.viewmodels.UpdateProfileViewModel
import com.fictivestudios.ravebae.utils.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.tv_name
import kotlinx.android.synthetic.main.fragment_my_profile.view.btn_edit_cover
import kotlinx.android.synthetic.main.fragment_my_profile.view.btn_edit_image
import kotlinx.android.synthetic.main.fragment_my_profile.view.iv_cover
import kotlinx.android.synthetic.main.fragment_my_profile.view.iv_profile_image
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "userModel"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : BBaseFragment<UpdateProfileViewModel>() {
    // TODO: Rename and change types of parameters
    private var jsonString: String? = null
    private var userModel: ProfileModel? = null

    private var param2: String? = null

    private var isImage = false
    private var isCover = false

    private lateinit var mView: View

    private val photosId = arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
    private var isPhoto = arrayOf(false, false, false, false, false, false, false, false)
    private var isPhotosFromServer = arrayOf(false, false, false, false, false, false, false, false)
    private val photo = "photo_"

    private var userGender: String? = null

    private var imagefromUpload = false
    private var isPhotosFromServerUrl =
        arrayOf<File?>(null, null, null, null, null, null, null, null)

//    private var profileImageAddress: String? = null
//    private var coverImageAddress: String? = null

    //Permission Request Handler
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    //ImageLoader instance
    private lateinit var imageLoader: ImageLoader

    // User Photo
    private var userPhoto1: String? = null
    private var userPhoto2: String? = null
    private var userPhoto3: String? = null
    private var userPhoto4: String? = null
    private var userPhoto5: String? = null
    private var userPhoto6: String? = null
    private var userPhoto7: String? = null
    private var userPhoto8: String? = null


    override fun createViewModel(): UpdateProfileViewModel {
        val factory = GenericViewModelFactory(UpdateProfileViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(UpdateProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            jsonString = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        userGender = ConfigurationPref(requireActivity()).userGender
    }

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setBtnBack("Edit Profile", resources.getColor(R.color.white))
        titlebar.showTitleBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        mView.btn_edit_cover.setOnClickListener {

            isCover = true
            openImagePicker()
        }
        mView.btn_edit_image.setOnClickListener {

            isImage = true
            // openImagePicker()

//            val intent = Intent()
//            intent.action = Intent.ACTION_GET_CONTENT
//            intent.type = "image/*"
//            startActivityForResult(intent, SELECT_IMAGE)
            openImagePicker()
        }


        mView.btn_save_changes.setOnClickListener {
            // MainActivity.getMainActivity?.onBackPressed()

            // Check if first value is selected of array // testing
            // Hit API
            if (
                mView.sp_gender.selectedItem.toString()
                    .equals("Select Gender", ignoreCase = true) &&
                ConfigurationPref(requireActivity()).userGender == null
            ) {
//                ErrorDialog.showErrorDialog(
//                    requireActivity(),
//                    getString(R.string.please_specify_gender)
//                )
                Toast.makeText(
                    requireActivity(),
                    "" + getString(R.string.please_specify_gender),
                    Toast.LENGTH_LONG
                ).show()
            } else if (mView.sp_gender.selectedItem.toString()
                    .equals("Select Gender", ignoreCase = true) &&
                ConfigurationPref(requireActivity()).userGender != null
            ) {
                // update gender with configuration preference..
                callApiPreFilledGender("" + ConfigurationPref(requireActivity()).userGender)
            } else {
                // condition if user gender
                callApi()
            }
            // Hit API
            // callApi()
        }

        setData()

        // User_Photos Click Listener
        userPhotosClick()

        // User_Photos Callbacks
        userPhotosCallbacks()

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

        // Callback for spinner
        //setSpListener()

        // For curved imageview
        // setting imageview curved
        curvedImageView()

        // ConfigurationPref(requireActivity()).userGender?
        userGender?.let {
            if (it.equals("male", ignoreCase = true)) {
                // Male Case
                mView.tvSpGenderTitle?.visibility = View.VISIBLE
                mView.tvSpGenderTitle?.text = getString(R.string.male)
            } else if (it.equals("female", ignoreCase = true)) {
                // Female Case
                mView.tvSpGenderTitle?.visibility = View.VISIBLE
                mView.tvSpGenderTitle?.text = getString(R.string.female)
            } else if (it.equals("couple", ignoreCase = true)) {
                // Couple Case
                mView.tvSpGenderTitle?.visibility = View.VISIBLE
                mView.tvSpGenderTitle?.text = getString(R.string.couple)
            } else {
                mView.tvSpGenderTitle?.visibility = View.VISIBLE
            }
        }

        val gson = Gson()
        userModel = gson.fromJson(jsonString, ProfileModel::class.java)

        //Setting up Activity Permission Request Handler
        setPermissionCallback()
        //getting imageloader instance
        imageLoader = Coil.imageLoader(requireActivity())

        try {
            // Has to fix this show image if already exists and upload..
            // (Constants.imageURL + userModel?.data?.userPhotos?.get(0))
            setPhotosIfAlreadyExists()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return mView
    }

    private fun curvedImageView() {
        mView.uploadImageHost1.setClipToOutline(true)
        mView.uploadImageHost2.setClipToOutline(true)
        mView.uploadImageHost3.setClipToOutline(true)
        mView.uploadImageHost4.setClipToOutline(true)
        mView.uploadImageHost5.setClipToOutline(true)
        mView.uploadImageHost6.setClipToOutline(true)
        mView.uploadImageHost7.setClipToOutline(true)
        mView.uploadImageHost8.setClipToOutline(true)
    }

    private fun setPhotosIfAlreadyExists() {
        user_photo_1.set(userModel?.data?.userPhotos?.get(0) ?: "")
        // Load Image to ImageView
        userPhoto1 =
            "" + Constants.imageURL + "" + userModel?.data?.userPhotos?.get(0)
        userPhoto1?.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(
                    it,
                    mView.uploadImageHost1,
                    R.drawable.user_dp_placeholder
                )
                isPhotosFromServer[0] = true

                // if (progress == null) progress = CustomProgressDialogue(activity)
                // Save image
                val bitmapURL = it.trim()
                //when download is pressed check permission and save bitmap from url
                checkPermissionAndDownloadBitmap(bitmapURL, "img1")
            }
        }

        // mView.uploadImageHost2
        user_photo_2.set(userModel?.data?.userPhotos?.get(1) ?: "")
        // Load Image to ImageView
        userPhoto2 =
            "" + Constants.imageURL + "" + userModel?.data?.userPhotos?.get(1)
        userPhoto2?.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(
                    it,
                    mView.uploadImageHost2,
                    R.drawable.user_dp_placeholder
                )
                isPhotosFromServer[1] = true

                // Save image
                val bitmapURL = it.trim()
                //when download is pressed check permission and save bitmap from url
                checkPermissionAndDownloadBitmap(bitmapURL, "img2")
            }
        }

        user_photo_3.set(userModel?.data?.userPhotos?.get(2) ?: "")
        userPhoto3 =
            "" + Constants.imageURL + "" + userModel?.data?.userPhotos?.get(2)
        userPhoto3?.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(
                    it,
                    mView.uploadImageHost3,
                    R.drawable.user_dp_placeholder
                )
                isPhotosFromServer[2] = true

                // Save image
                val bitmapURL = it.trim()
                //when download is pressed check permission and save bitmap from url
                checkPermissionAndDownloadBitmap(bitmapURL, "img3")
            }
        }

        user_photo_4.set(userModel?.data?.userPhotos?.get(3) ?: "")
        userPhoto4 =
            "" + Constants.imageURL + "" + userModel?.data?.userPhotos?.get(3)
        userPhoto4?.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(
                    it,
                    mView.uploadImageHost4,
                    R.drawable.user_dp_placeholder
                )
                isPhotosFromServer[3] = true

                // Save image
                val bitmapURL = it.trim()
                //when download is pressed check permission and save bitmap from url
                checkPermissionAndDownloadBitmap(bitmapURL, "img4")
            }
        }

        user_photo_5.set(userModel?.data?.userPhotos?.get(4) ?: "")
        userPhoto5 =
            "" + Constants.imageURL + "" + userModel?.data?.userPhotos?.get(4)
        userPhoto5?.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(
                    it,
                    mView.uploadImageHost5,
                    R.drawable.user_dp_placeholder
                )
                isPhotosFromServer[4] = true

                // Save image
                val bitmapURL = it.trim()
                //when download is pressed check permission and save bitmap from url
                checkPermissionAndDownloadBitmap(bitmapURL, "img5")
            }
        }

        user_photo_6.set(userModel?.data?.userPhotos?.get(5) ?: "")
        userPhoto6 =
            "" + Constants.imageURL + "" + userModel?.data?.userPhotos?.get(5)
        userPhoto6?.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(
                    it,
                    mView.uploadImageHost6,
                    R.drawable.user_dp_placeholder
                )
                isPhotosFromServer[5] = true

                // Save image
                val bitmapURL = it.trim()
                //when download is pressed check permission and save bitmap from url
                checkPermissionAndDownloadBitmap(bitmapURL, "img6")
            }
        }

        user_photo_7.set(userModel?.data?.userPhotos?.get(6) ?: "")
        userPhoto7 =
            "" + Constants.imageURL + "" + userModel?.data?.userPhotos?.get(6)
        userPhoto7?.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(
                    it,
                    mView.uploadImageHost7,
                    R.drawable.user_dp_placeholder
                )
                isPhotosFromServer[6] = true

                // Save image
                val bitmapURL = it.trim()
                //when download is pressed check permission and save bitmap from url
                checkPermissionAndDownloadBitmap(bitmapURL, "img7")
            }
        }

        user_photo_8.set(userModel?.data?.userPhotos?.get(7) ?: "")
        userPhoto8 =
            "" + Constants.imageURL + "" + userModel?.data?.userPhotos?.get(7)
        userPhoto8?.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(
                    it,
                    mView.uploadImageHost8,
                    R.drawable.user_dp_placeholder
                )
                isPhotosFromServer[7] = true

                // Save image
                val bitmapURL = it.trim()
                //when download is pressed check permission and save bitmap from url
                checkPermissionAndDownloadBitmap(bitmapURL, "img8")
            }
        }

        // if (progress?.isShowing == true) progress?.dismiss()
    }

    //Allowing activity to automatically handle permission request
    private fun setPermissionCallback() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    getBitmapFromUrl(userPhoto1.toString().trim(), "img1")
                    getBitmapFromUrl(userPhoto2.toString().trim(), "img2")
                    getBitmapFromUrl(userPhoto3.toString().trim(), "img3")
                    getBitmapFromUrl(userPhoto4.toString().trim(), "img4")
                    getBitmapFromUrl(userPhoto5.toString().trim(), "img5")
                    getBitmapFromUrl(userPhoto6.toString().trim(), "img6")
                    getBitmapFromUrl(userPhoto7.toString().trim(), "img7")
                    getBitmapFromUrl(userPhoto8.toString().trim(), "img8")
                    Log.d("isGranted", "isGranted executed")
                }
            }
    }

    //function to check and request storage permission
    private fun checkPermissionAndDownloadBitmap(bitmapURL: String, fileName: String) {
        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                getBitmapFromUrl(bitmapURL, fileName)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                activity?.showPermissionRequestDialog(
                    "Write Permission",
                    "Write Permission Request"
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    //this function will fetch the Bitmap from the given URL
    private fun getBitmapFromUrl(bitmapURL: String, fileName: String) = lifecycleScope.launch {
        // progressbar.visible(true)
        // image_view.load(bitmapURL)
//        val request = ImageRequest.Builder(requireActivity())
//            .data(bitmapURL)
//            .build()
        try {
            // val downloadedBitmap = (imageLoader.execute(request).drawable as BitmapDrawable).bitmap
            // image_view.setImageBitmap(downloadedBitmap)

            val request = ImageRequest.Builder(requireActivity())
                .data("" + bitmapURL)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            val result = (imageLoader.execute(request) as SuccessResult).drawable
            val downloadedBitmap = (result as BitmapDrawable).bitmap

            saveMediaToStorage(downloadedBitmap, fileName)
        } catch (e: Exception) {
            // activity?.toast(e.message)
            // Log.d("bitmapurl",""+bitmapURL)
            Log.d("bitmapurl", "" + e.message)
        }
        // progressbar.visible(false)
    }

    //the function it is used to save the Bitmap to external storage // filename
    private fun saveMediaToStorage(bitmap: Bitmap, photoFileName: String) {
        val filename = "${System.currentTimeMillis()}.jpg"
//        val filename = "${fileName}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)

                populateIsPhotosFromServerUrl(image, photoFileName)

                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)

            populateIsPhotosFromServerUrl(image, photoFileName)

            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            // activity?.toast("Saved to Photos")
            Log.d("saved", "Saved to Photos")
        }
    }

    fun populateIsPhotosFromServerUrl(image: File, fileName: String) {
        when (fileName) {
            "img1" -> isPhotosFromServerUrl[0] = image
            "img2" -> isPhotosFromServerUrl[1] = image
            "img3" -> isPhotosFromServerUrl[2] = image
            "img4" -> isPhotosFromServerUrl[3] = image
            "img5" -> isPhotosFromServerUrl[4] = image
            "img6" -> isPhotosFromServerUrl[5] = image
            "img7" -> isPhotosFromServerUrl[6] = image
            "img8" -> isPhotosFromServerUrl[7] = image
        }
    }


    /** Listener for Gender Spinner*/
    private fun setSpListener() {
        mView.sp_gender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (i == 0) {
                    mView.tvSpGenderTitle?.visibility = View.VISIBLE
                } else if (i == 1) {
                    // Male Case
                    mView.tvSpGenderTitle?.visibility = View.VISIBLE
                    mView.tvSpGenderTitle?.text = getString(R.string.male)
                } else if (i == 2) {
                    // Female Case
                    mView.tvSpGenderTitle?.visibility = View.VISIBLE
                    mView.tvSpGenderTitle?.text = getString(R.string.female)
                } else if (i == 3) {
                    // Couple Case
                    mView.tvSpGenderTitle?.visibility = View.VISIBLE
                    mView.tvSpGenderTitle?.text = getString(R.string.couple)
                } else {
                    mView.tvSpGenderTitle?.visibility = View.GONE
                    Log.d("Selected Item", "Value: " + mView.sp_gender.selectedItem)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun userPhotosClick() {
        mView.btn_add_1.setOnClickListener {
            isPhoto[0] = true
            openImagePicker()
        }
        mView.icon_1.setOnClickListener {
            // set background for imageview
            mView.btn_add_1.setImageResource(R.drawable.add_icon);
            mView.uploadImageHost1.setImageResource(android.R.color.transparent);
            // reset observable variable
            user_photo_1.set("")
            isPhotosFromServerUrl[0] = null
            isPhotosFromServer[0] = false
        }

        mView.btn_add_2.setOnClickListener {
            isPhoto[1] = true
            openImagePicker()
        }
        mView.icon_2.setOnClickListener {
            // set background for imageview
            mView.btn_add_2.setImageResource(R.drawable.add_icon);
            mView.uploadImageHost2.setImageResource(android.R.color.transparent);
            // clear preference image storage path
            ConfigurationPref(requireActivity()).userPhoto2 = ""
            // reset observable variable
            user_photo_2.set("")
            isPhotosFromServerUrl[1] = null
            isPhotosFromServer[1] = false
        }

        mView.btn_add_3.setOnClickListener {
            isPhoto[2] = true
            openImagePicker()
        }
        mView.icon_3.setOnClickListener {
            // set background for imageview
            mView.btn_add_3.setImageResource(R.drawable.add_icon);
            mView.uploadImageHost3.setImageResource(android.R.color.transparent);
            // reset observable variable
            user_photo_3.set("")
            // clear preference image storage path
            ConfigurationPref(requireActivity()).userPhoto3 = ""
            isPhotosFromServerUrl[2] = null
            isPhotosFromServer[2] = false
        }

        mView.btn_add_4.setOnClickListener {
            isPhoto[3] = true
            openImagePicker()
        }
        mView.icon_4.setOnClickListener {
            // set background for imageview
            mView.btn_add_4.setImageResource(R.drawable.add_icon);
            mView.uploadImageHost4.setImageResource(android.R.color.transparent);
            // reset observable variable
            user_photo_4.set("")
            isPhotosFromServerUrl[3] = null
            isPhotosFromServer[3] = false
        }

        mView.btn_add_5.setOnClickListener {
            isPhoto[4] = true
            openImagePicker()
        }
        mView.icon_5.setOnClickListener {
            // set background for imageview
            mView.btn_add_5.setImageResource(R.drawable.add_icon);
            mView.uploadImageHost5.setImageResource(android.R.color.transparent);
            // reset observable variable
            user_photo_5.set("")
            isPhotosFromServerUrl[4] = null
            isPhotosFromServer[4] = false
        }

        mView.btn_add_6.setOnClickListener {
            isPhoto[5] = true
            openImagePicker()
        }
        mView.icon_6.setOnClickListener {
            // set background for imageview
            mView.btn_add_6.setImageResource(R.drawable.add_icon);
            mView.uploadImageHost6.setImageResource(android.R.color.transparent);
            // reset observable variable
            user_photo_6.set("")
            isPhotosFromServerUrl[5] = null
            isPhotosFromServer[5] = false
        }

        mView.btn_add_7.setOnClickListener {
            isPhoto[6] = true
            openImagePicker()
        }
        mView.icon_7.setOnClickListener {
            // set background for imageview
            mView.btn_add_7.setImageResource(R.drawable.add_icon);
            mView.uploadImageHost7.setImageResource(android.R.color.transparent);
            // reset observable variable
            user_photo_7.set("")
            isPhotosFromServerUrl[6] = null
            isPhotosFromServer[6] = false
        }

        mView.btn_add_8.setOnClickListener {
            isPhoto[7] = true
            openImagePicker()
        }
        mView.icon_8.setOnClickListener {
            // set background for imageview
            mView.btn_add_8.setImageResource(R.drawable.add_icon);
            mView.uploadImageHost8.setImageResource(android.R.color.transparent);
            // reset observable variable
            user_photo_8.set("")
            isPhotosFromServerUrl[7] = null
            isPhotosFromServer[7] = false
        }
    }

    private fun userPhotosCallbacks() {
        // set visibility of remove button on
        user_photo_1.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (user_photo_1.get() != "") {
                    mView.btn_add_1.visibility = View.GONE
                    mView.icon_1.visibility = View.VISIBLE
                    mView.btn_container_2.visibility = View.VISIBLE

                    // if from Upload save address to pref
//                    if (imagefromUpload) {
//                        ConfigurationPref(requireActivity()).userPhoto1 = user_photo_1.get()
//                        imagefromUpload = false
//                    }
                } else {
                    // Condition for baseURL
                    mView.icon_1.visibility = View.INVISIBLE
                    mView.btn_add_1.visibility = View.VISIBLE
                }
            }
        })

        user_photo_2.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (user_photo_2.get() != "") {
                    mView.btn_add_2.visibility = View.GONE
                    mView.icon_2.visibility = View.VISIBLE
                    mView.btn_container_3.visibility = View.VISIBLE

                    // if from Upload save address to pref
//                    if (imagefromUpload) {
//                        ConfigurationPref(requireActivity()).userPhoto2 = user_photo_2.get()
//                        imagefromUpload = false
//                    }

                } else {
//                    if (user_photo_1.get() != Constants.imageURL) {
//                        mView.icon_2.visibility = View.INVISIBLE
//                        mView.btn_add_2.visibility = View.VISIBLE
//                    }

                    mView.icon_2.visibility = View.INVISIBLE
                    mView.btn_add_2.visibility = View.VISIBLE
                }
            }
        })

        user_photo_3.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (user_photo_3.get() != "") {
                    mView.btn_add_3.visibility = View.GONE
                    mView.icon_3.visibility = View.VISIBLE
                    mView.btn_container_4.visibility = View.VISIBLE

                    // if from Upload save address to pref
//                    if (imagefromUpload) {
//                        ConfigurationPref(requireActivity()).userPhoto3 = user_photo_3.get()
//                        imagefromUpload = false
//                    }

                } else {
                    mView.icon_3.visibility = View.INVISIBLE
                    mView.btn_add_3.visibility = View.VISIBLE
                }
            }
        })

        user_photo_4.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (user_photo_4.get() != "") {
                    mView.btn_add_4.visibility = View.GONE
                    mView.icon_4.visibility = View.VISIBLE
                    mView.btn_container_5.visibility = View.VISIBLE

                    // if from Upload save address to pref
//                    if (imagefromUpload) {
//                        ConfigurationPref(requireActivity()).userPhoto4 = user_photo_4.get()
//                        imagefromUpload = false
//                    }
                } else {
                    mView.icon_4.visibility = View.INVISIBLE
                    mView.btn_add_4.visibility = View.VISIBLE
                }
            }
        })

        user_photo_5.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (user_photo_5.get() != "") {
                    mView.btn_add_5.visibility = View.GONE
                    mView.icon_5.visibility = View.VISIBLE
                    mView.btn_container_6.visibility = View.VISIBLE

                    // if from Upload save address to pref
//                    if (imagefromUpload) {
//                        ConfigurationPref(requireActivity()).userPhoto5 = user_photo_5.get()
//                        imagefromUpload = false
//                    }
                } else {
                    mView.icon_5.visibility = View.INVISIBLE
                    mView.btn_add_5.visibility = View.VISIBLE
                }
            }
        })

        user_photo_6.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (user_photo_6.get() != "") {
                    mView.btn_add_6.visibility = View.GONE
                    mView.icon_6.visibility = View.VISIBLE
                    mView.btn_container_7.visibility = View.VISIBLE

                    // if from Upload save address to pref
//                    if (imagefromUpload) {
//                        ConfigurationPref(requireActivity()).userPhoto6 = user_photo_6.get()
//                        imagefromUpload = false
//                    }
                } else {
                    mView.icon_6.visibility = View.INVISIBLE
                    mView.btn_add_6.visibility = View.VISIBLE
                }
            }
        })

        user_photo_7.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (user_photo_7.get() != "") {
                    mView.btn_add_7.visibility = View.GONE
                    mView.icon_7.visibility = View.VISIBLE
                    mView.btn_container_8.visibility = View.VISIBLE

                    // if from Upload save address to pref
//                    if (imagefromUpload) {
//                        ConfigurationPref(requireActivity()).userPhoto7 = user_photo_7.get()
//                        imagefromUpload = false
//                    }
                } else {
                    mView.icon_7.visibility = View.INVISIBLE
                    mView.btn_add_7.visibility = View.VISIBLE
                }
            }
        })

        user_photo_8.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {

            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (user_photo_8.get() != "") {
                    mView.btn_add_8.visibility = View.GONE
                    mView.icon_8.visibility = View.VISIBLE

                    // if from Upload save address to pref
//                    if (imagefromUpload) {
//                        ConfigurationPref(requireActivity()).userPhoto8 = user_photo_8.get()
//                        imagefromUpload = false
//                    }
                } else {
                    mView.btn_add_8.visibility = View.VISIBLE
                    mView.icon_8.visibility = View.INVISIBLE
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        val profileURL: String =
            "" + Constants.imageURL + "" + ConfigurationPref(requireActivity()).profilePic
        val coverURL: String =
            "" + Constants.imageURL + "" + ConfigurationPref(requireActivity()).coverPic


        mView.tv_name.text = "" + ConfigurationPref(requireActivity()).userName
        mView.tv_mobile_num.text = "" + ConfigurationPref(requireActivity()).mobileNumber
        mView.tv_bio.setText("" + ConfigurationPref(requireActivity()).description)
        mView.tv_email_txt.text = "" + ConfigurationPref(requireActivity()).email
        // ConfigurationPref(requireActivity()).profilePic.let { ImageLoadUtil.loadImageNotFit(it!!, mView.iv_profile_image, R.drawable.user_dp) }
        //ConfigurationPref(requireActivity()).coverPic.let { ImageLoadUtil.loadImageNotFit(it!!, mView.iv_cover) }
        // data.data?.image?.let { ImageLoadUtil.loadImageNotFit(it, ivHeader) }

        profileURL.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageNotFit(
                    it,
                    mView.iv_profile_image,
                    R.drawable.user_dp_placeholder
                )
            }
            // ImageLoadUtil.loadImageNotFit(it, mView.iv_profile_image)
        }
        //ConfigurationPref(requireActivity()).coverPic.let { ImageLoadUtil.loadImageNotFit(it!!, mView.iv_cover) }
        coverURL.let {
            if (it != Constants.imageURL) {
                // R.drawable.user_dp
                ImageLoadUtil.loadImageWithoutPlaceHolder(it, mView.iv_cover)
            }
        }

    }

    private fun callApi() {
        try {
            var profilePart: MultipartBody.Part? = null
            var coverPart: MultipartBody.Part? = null

            var userPhotoPart1: MultipartBody.Part? = null
            var userPhotoPart2: MultipartBody.Part? = null
            var userPhotoPart3: MultipartBody.Part? = null
            var userPhotoPart4: MultipartBody.Part? = null
            var userPhotoPart5: MultipartBody.Part? = null
            var userPhotoPart6: MultipartBody.Part? = null
            var userPhotoPart7: MultipartBody.Part? = null
            var userPhotoPart8: MultipartBody.Part? = null


            if (!image.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + image.get())
                profilePart = File(image.get() ?: "").getPartMap("user_image")
            }

            if (!coverImage.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + coverImage.get())
                coverPart = File(coverImage.get() ?: "").getPartMap("cover_image")
            }

            // user_photos array
            if (!user_photo_1.get().isNullOrEmpty()) {
                // Testing // replace and check
                Log.d("ioooossss", "" + user_photo_1.get())
                Log.d("ioooossss", "Pref:" + ConfigurationPref(requireActivity()).userPhoto1)

                // Testing
                if (isPhotosFromServer[0]) {
                    userPhotoPart1 = isPhotosFromServerUrl[0]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart1 = File(user_photo_1.get() ?: "").getPartMap("user_photos")
                }

                // userPhotoPart1 = File(user_photo_1.get() ?: "").getPartMap("user_photos")
            }

            if (!user_photo_2.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_2.get())
                Log.d("ioooossss", "Pref:" + ConfigurationPref(requireActivity()).userPhoto2)

                if (isPhotosFromServer[1]) {
                    // userPhotoPart2 = File(URL(userPhoto2).toURI()).getPartMap("user_photos")
                    userPhotoPart2 = isPhotosFromServerUrl[1]?.getPartMap("user_photos")
                    // isPhotosFromServer[1] = false
                } else {
                    userPhotoPart2 = File(user_photo_2.get() ?: "").getPartMap("user_photos")
                }
                // userPhotoPart2 = File(user_photo_2.get() ?: "").getPartMap("user_photos")
            }

            if (!user_photo_3.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_3.get())
                Log.d("ioooossss", "Pref:" + ConfigurationPref(requireActivity()).userPhoto3)

                if (isPhotosFromServer[2]) {
                    // userPhotoPart3 = File(URL(userPhoto3).toURI()).getPartMap("user_photos")
                    userPhotoPart3 = isPhotosFromServerUrl[2]?.getPartMap("user_photos")
                    // isPhotosFromServer[2] = false
                } else {
                    userPhotoPart3 = File(user_photo_3.get() ?: "").getPartMap("user_photos")
                }

                // userPhotoPart3 = File(user_photo_3.get() ?: "").getPartMap("user_photos")
            }

            if (!user_photo_4.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_4.get())
                // userPhotoPart4 = File(user_photo_4.get() ?: "").getPartMap("user_photos")
                if (isPhotosFromServer[3]) {
                    userPhotoPart4 = isPhotosFromServerUrl[3]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart4 = File(user_photo_4.get() ?: "").getPartMap("user_photos")
                }

            }

            if (!user_photo_5.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_5.get())
                // userPhotoPart5 = File(user_photo_5.get() ?: "").getPartMap("user_photos")
                if (isPhotosFromServer[4]) {
                    userPhotoPart5 = isPhotosFromServerUrl[4]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart5 = File(user_photo_5.get() ?: "").getPartMap("user_photos")
                }

            }

            if (!user_photo_6.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_6.get())
                // userPhotoPart6 = File(user_photo_6.get() ?: "").getPartMap("user_photos")
                if (isPhotosFromServer[5]) {
                    userPhotoPart6 = isPhotosFromServerUrl[5]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart6 = File(user_photo_6.get() ?: "").getPartMap("user_photos")
                }

            }

            if (!user_photo_7.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_7.get())
                // userPhotoPart7 = File(user_photo_7.get() ?: "").getPartMap("user_photos")
                if (isPhotosFromServer[6]) {
                    userPhotoPart7 = isPhotosFromServerUrl[6]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart7 = File(user_photo_7.get() ?: "").getPartMap("user_photos")
                }

            }

            if (!user_photo_8.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_8.get())
                // userPhotoPart8 = File(user_photo_8.get() ?: "").getPartMap("user_photos")
                if (isPhotosFromServer[7]) {
                    userPhotoPart8 = isPhotosFromServerUrl[7]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart8 = File(user_photo_8.get() ?: "").getPartMap("user_photos")
                }

            }

//            else {
//                Toast.makeText(requireActivity(), "Image Field is Empty", Toast.LENGTH_LONG).show()
//            }


            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if (MyUtils.isNetworkAvailable(requireActivity())) {
                viewModel?.loadProfileData(
                    "" + ConfigurationPref(requireActivity()).userName,
                    profilePart,
                    coverPart,
                    userPhotoPart1,
                    userPhotoPart2,
                    userPhotoPart3,
                    userPhotoPart4,
                    userPhotoPart5,
                    userPhotoPart6,
                    userPhotoPart7,
                    userPhotoPart8,
                    "" + ConfigurationPref(requireActivity()).mobileNumber,
                    "" + mView.sp_gender.selectedItem,
                    "" + mView.tv_bio.text.toString(),
                )
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.no_internet),
                    Toast.LENGTH_LONG
                ).show()
            }

            viewModel?.updateProfileModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        if (homeMainModel?.status == 0) {
                            // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)

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

                        } else {
                            // Update Whole Model And Call onBackPressed..

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
                            ConfigurationPref(requireActivity()).coverPic =
                                "" + homeMainModel?.data?.coverImage

                            Toast.makeText(
                                requireActivity(),
                                "" + homeMainModel?.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            MainActivity.getMainActivity?.onBackPressed()
                        }

                    } else {
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                            .show()
                        Log.d("Login", "Else Block" + homeMainModel?.data?.user_id)
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (e: Exception) {
            Log.d("EditProfile", "EditProfile exception :" + e)
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            Toast.makeText(
                requireActivity(),
                "Invalid Credentials,please try again!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun callApiPreFilledGender(gender: String) {
        try {
            var profilePart: MultipartBody.Part? = null
            var coverPart: MultipartBody.Part? = null

            var userPhotoPart1: MultipartBody.Part? = null
            var userPhotoPart2: MultipartBody.Part? = null
            var userPhotoPart3: MultipartBody.Part? = null
            var userPhotoPart4: MultipartBody.Part? = null
            var userPhotoPart5: MultipartBody.Part? = null
            var userPhotoPart6: MultipartBody.Part? = null
            var userPhotoPart7: MultipartBody.Part? = null
            var userPhotoPart8: MultipartBody.Part? = null


            if (!image.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + image.get())
                profilePart = File(image.get() ?: "").getPartMap("user_image")
            }

            if (!coverImage.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + coverImage.get())
                coverPart = File(coverImage.get() ?: "").getPartMap("cover_image")
            }

            // user_photos array
            if (!user_photo_1.get().isNullOrEmpty()) {
                // Testing // replace and check
                Log.d("ioooossss", "" + user_photo_1.get())
                Log.d("ioooossss", "Pref:" + ConfigurationPref(requireActivity()).userPhoto1)

                // Testing
                if (isPhotosFromServer[0]) {
                    userPhotoPart1 = isPhotosFromServerUrl[0]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart1 = File(user_photo_1.get() ?: "").getPartMap("user_photos")
                }

                // userPhotoPart1 = File(user_photo_1.get() ?: "").getPartMap("user_photos")
            }

            if (!user_photo_2.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_2.get())
                Log.d("ioooossss", "Pref:" + ConfigurationPref(requireActivity()).userPhoto2)

                if (isPhotosFromServer[1]) {
                    // userPhotoPart2 = File(URL(userPhoto2).toURI()).getPartMap("user_photos")
                    userPhotoPart2 = isPhotosFromServerUrl[1]?.getPartMap("user_photos")
                    // isPhotosFromServer[1] = false
                } else {
                    userPhotoPart2 = File(user_photo_2.get() ?: "").getPartMap("user_photos")
                }
                // userPhotoPart2 = File(user_photo_2.get() ?: "").getPartMap("user_photos")
            }

            if (!user_photo_3.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_3.get())
                Log.d("ioooossss", "Pref:" + ConfigurationPref(requireActivity()).userPhoto3)

                if (isPhotosFromServer[2]) {
                    // userPhotoPart3 = File(URL(userPhoto3).toURI()).getPartMap("user_photos")
                    userPhotoPart3 = isPhotosFromServerUrl[2]?.getPartMap("user_photos")
                    // isPhotosFromServer[2] = false
                } else {
                    userPhotoPart3 = File(user_photo_3.get() ?: "").getPartMap("user_photos")
                }

                // userPhotoPart3 = File(user_photo_3.get() ?: "").getPartMap("user_photos")
            }

            if (!user_photo_4.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_4.get())
                // userPhotoPart4 = File(user_photo_4.get() ?: "").getPartMap("user_photos")
                if (isPhotosFromServer[3]) {
                    userPhotoPart4 = isPhotosFromServerUrl[3]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart4 = File(user_photo_4.get() ?: "").getPartMap("user_photos")
                }

            }

            if (!user_photo_5.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_5.get())
                // userPhotoPart5 = File(user_photo_5.get() ?: "").getPartMap("user_photos")
                if (isPhotosFromServer[4]) {
                    userPhotoPart5 = isPhotosFromServerUrl[4]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart5 = File(user_photo_5.get() ?: "").getPartMap("user_photos")
                }

            }

            if (!user_photo_6.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_6.get())
                // userPhotoPart6 = File(user_photo_6.get() ?: "").getPartMap("user_photos")
                if (isPhotosFromServer[5]) {
                    userPhotoPart6 = isPhotosFromServerUrl[5]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart6 = File(user_photo_6.get() ?: "").getPartMap("user_photos")
                }

            }

            if (!user_photo_7.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_7.get())
                // userPhotoPart7 = File(user_photo_7.get() ?: "").getPartMap("user_photos")
                if (isPhotosFromServer[6]) {
                    userPhotoPart7 = isPhotosFromServerUrl[6]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart7 = File(user_photo_7.get() ?: "").getPartMap("user_photos")
                }

            }

            if (!user_photo_8.get().isNullOrEmpty()) {
                Log.d("ioooossss", "" + user_photo_8.get())
                // userPhotoPart8 = File(user_photo_8.get() ?: "").getPartMap("user_photos")
                if (isPhotosFromServer[7]) {
                    userPhotoPart8 = isPhotosFromServerUrl[7]?.getPartMap("user_photos")
                    // isPhotosFromServer[0] = false
                } else {
                    userPhotoPart8 = File(user_photo_8.get() ?: "").getPartMap("user_photos")
                }

            }

//            else {
//                Toast.makeText(requireActivity(), "Image Field is Empty", Toast.LENGTH_LONG).show()
//            }


            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if (MyUtils.isNetworkAvailable(requireActivity())) {
                viewModel?.loadProfileData(
                    "" + ConfigurationPref(requireActivity()).userName,
                    profilePart,
                    coverPart,
                    userPhotoPart1,
                    userPhotoPart2,
                    userPhotoPart3,
                    userPhotoPart4,
                    userPhotoPart5,
                    userPhotoPart6,
                    userPhotoPart7,
                    userPhotoPart8,
                    "" + ConfigurationPref(requireActivity()).mobileNumber,
                    "" + gender,
                    "" + mView.tv_bio.text.toString(),
                )
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.no_internet),
                    Toast.LENGTH_LONG
                ).show()
            }

            viewModel?.updateProfileModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.error.equals(
                            "no",
                            ignoreCase = true
                        )
                    ) {
                        if (homeMainModel?.status == 0) {
                            // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
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

                        } else {
                            // Update Whole Model And Call onBackPressed..

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
                            ConfigurationPref(requireActivity()).coverPic =
                                "" + homeMainModel?.data?.coverImage

                            Toast.makeText(
                                requireActivity(),
                                "" + homeMainModel?.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            MainActivity.getMainActivity?.onBackPressed()
                        }

                    } else {
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                            .show()
                        Log.d("Login", "Else Block" + homeMainModel?.data?.user_id)
                    }
                } catch (e: Exception) {
                    Log.d("Login", "Catch Block")
                }
            }
        } catch (e: Exception) {
            Log.d("EditProfile", "EditProfile exception :" + e)
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


    // Testing // for selecting an image from gallery
    fun getMediaFilePathFor(uri: Uri, context: Context): String {
        val returnCursor =
            context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.filesDir, name)
        try {
            val inputStream =
                context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream!!.available()
            //int bufferSize = 1024;
            val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size %d", "" + file.length())
            inputStream.close()
            outputStream.close()
            Log.e("File Size %s", file.path)
            Log.e("File Size %d", "" + file.length())
        } catch (e: java.lang.Exception) {
            Log.e("File Size %s", e.message!!)
        }
        return file.path
    }


    // val SELECT_IMAGE = 22
    private var image: ObservableField<String> = ObservableField("")
    private var coverImage: ObservableField<String> = ObservableField("")

    private var user_photo_1: ObservableField<String> = ObservableField("")
    private var user_photo_2: ObservableField<String> = ObservableField("")
    private var user_photo_3: ObservableField<String> = ObservableField("")
    private var user_photo_4: ObservableField<String> = ObservableField("")
    private var user_photo_5: ObservableField<String> = ObservableField("")
    private var user_photo_6: ObservableField<String> = ObservableField("")
    private var user_photo_7: ObservableField<String> = ObservableField("")
    private var user_photo_8: ObservableField<String> = ObservableField("")


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

            // Use Uri object instead of File to avoid storage permissions
            if (isImage) {
                isImage = false
                mView.iv_profile_image.setImageURI(uri)

                image.set(activity?.let {
                    imageForUpload?.let {
                        ImageUtil.saveToInternalStorage(
                            activity, "fileName.png", it
                        )
                    } + "/fileName.png"
                })
                // image.set(activity?.let { getMediaFilePathFor(uri!!, it) })
                mView.iv_profile_image.setImageURI(uri)
                // mView.iv_profile_image.background = null
            } else if (isCover) {
                isCover = false
                mView.iv_cover.setImageURI(uri)
               // mView.iv_cover.background = null

                coverImage.set(activity?.let {
                    imageForUpload?.let {
                        ImageUtil.saveToInternalStorage(
                            activity, "coverName.png", it
                        )
                    } + "/coverName.png"
                })
            } else if (isPhoto.get(0)) {
                isPhoto.set(0, false)
                // mView.btn_add_1.setImageURI(uri)
                mView.uploadImageHost1.setImageURI(uri)
                //mView.btn_container_1.setImageURI(uri)

                setIsPhotosData(1, imageForUpload)
            } else if (isPhoto.get(1)) {
                isPhoto.set(1, false)
                // mView.btn_add_2.setImageURI(uri)
                mView.uploadImageHost2.setImageURI(uri)

                setIsPhotosData(2, imageForUpload)
            } else if (isPhoto.get(2)) {
                isPhoto.set(2, false)
                // mView.btn_add_3.setImageURI(uri)
                mView.uploadImageHost3.setImageURI(uri)

                setIsPhotosData(3, imageForUpload)

            } else if (isPhoto.get(3)) {
                isPhoto.set(3, false)
                // mView.btn_add_4.setImageURI(uri)
                mView.uploadImageHost4.setImageURI(uri)

                setIsPhotosData(4, imageForUpload)
            } else if (isPhoto.get(4)) {
                isPhoto.set(4, false)
                // mView.btn_add_5.setImageURI(uri)
                mView.uploadImageHost5.setImageURI(uri)


                setIsPhotosData(5, imageForUpload)
            } else if (isPhoto.get(5)) {
                isPhoto.set(5, false)
                // mView.btn_add_6.setImageURI(uri)
                mView.uploadImageHost6.setImageURI(uri)

                setIsPhotosData(6, imageForUpload)
            } else if (isPhoto.get(6)) {
                isPhoto.set(6, false)
                // mView.btn_add_7.setImageURI(uri)
                mView.uploadImageHost7.setImageURI(uri)

                setIsPhotosData(7, imageForUpload)

            } else if (isPhoto.get(7)) {
                isPhoto.set(7, false)
                // mView.btn_add_8.setImageURI(uri)
                mView.uploadImageHost8.setImageURI(uri)

                setIsPhotosData(8, imageForUpload)

            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setIsPhotosData(isPhoto: Int, imageBitmap: Bitmap) {
        when (isPhoto) {
            1 -> {
                val fileName = "$photo${photosId.get(0)}"

                user_photo_1.set(activity?.let {
                    imageBitmap.let {
                        ImageUtil.saveToInternalStorage(
                            activity, "$fileName.png", it
                        )
                    } + "/$fileName.png"
                })

                imagefromUpload = true
            }
            2 -> {
                val fileName = "$photo${photosId.get(1)}"

                user_photo_2.set(activity?.let {
                    imageBitmap.let {
                        ImageUtil.saveToInternalStorage(
                            activity, "$fileName.png", it
                        )
                    } + "/$fileName.png"
                })

                imagefromUpload = true
            }
            3 -> {
                val fileName = "$photo${photosId.get(2)}"

                user_photo_3.set(activity?.let {
                    imageBitmap.let {
                        ImageUtil.saveToInternalStorage(
                            activity, "$fileName.png", it
                        )
                    } + "/$fileName.png"
                })

                imagefromUpload = true
            }
            4 -> {
                val fileName = "$photo${photosId.get(3)}"

                user_photo_4.set(activity?.let {
                    imageBitmap.let {
                        ImageUtil.saveToInternalStorage(
                            activity, "$fileName.png", it
                        )
                    } + "/$fileName.png"
                })

                imagefromUpload = true
            }
            5 -> {
                val fileName = "$photo${photosId.get(4)}"

                user_photo_5.set(activity?.let {
                    imageBitmap.let {
                        ImageUtil.saveToInternalStorage(
                            activity, "$fileName.png", it
                        )
                    } + "/$fileName.png"
                })

                imagefromUpload = true
            }
            6 -> {
                val fileName = "$photo${photosId.get(5)}"

                user_photo_6.set(activity?.let {
                    imageBitmap.let {
                        ImageUtil.saveToInternalStorage(
                            activity, "$fileName.png", it
                        )
                    } + "/$fileName.png"
                })

                imagefromUpload = true
            }
            7 -> {
                val fileName = "$photo${photosId.get(6)}"

                user_photo_7.set(activity?.let {
                    imageBitmap.let {
                        ImageUtil.saveToInternalStorage(
                            activity, "$fileName.png", it
                        )
                    } + "/$fileName.png"
                })

                imagefromUpload = true
            }
            8 -> {
                val fileName = "$photo${photosId.get(7)}"

                user_photo_8.set(activity?.let {
                    imageBitmap.let {
                        ImageUtil.saveToInternalStorage(
                            activity, "$fileName.png", it
                        )
                    } + "/$fileName.png"
                })

                imagefromUpload = true
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
         * @return A new instance of fragment EditProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}