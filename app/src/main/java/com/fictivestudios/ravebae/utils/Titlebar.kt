package com.fictivestudios.ravebae.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.fragments.DiscoverFragment
import com.fictivestudios.ravebae.presentation.models.Datac
import com.fictivestudios.ravebae.presentation.models.Datam
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import kotlinx.android.synthetic.main.item_received.view.*
import kotlinx.android.synthetic.main.titlebar.view.*


class Titlebar : RelativeLayout {

    private lateinit var mView: View

    constructor(context: Context) : super(context) {
        initLayout(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initLayout(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initLayout(context)
    }

    fun initLayout(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = inflater.inflate(R.layout.titlebar, this, true)
    }

    fun hideTitleBar() {
        resetTitlebar()
    }

    fun showTitleBar() {
        mView?.rlTitlebarMainLayout?.setVisibility(View.VISIBLE)
    }

    fun resetTitlebar() {
        mView?.rlTitlebarMainLayout?.setVisibility(View.GONE)
    }

/*    fun setHomeTitle(titleText:String,
                     leftBtnListener: OnClickListener?,
                     drawableLeft: Int,
                     rightBtnListener: OnClickListener,
                     drawableRight: Int)
    {

        mView?.tvTitle?.text = titleText

        mView?.ivBack!!.visibility = View.GONE
        mView?.ivProfile!!.visibility = View.VISIBLE
        mView?.ivProfile!!.setImageResource(drawableLeft)
        mView?.ivProfile!!.setOnClickListener(leftBtnListener)

        mView?.iv_notification!!.visibility = View.VISIBLE
        mView?.ivProfileRight!!.visibility = View.GONE
        mView?.iv_notification!!.setImageResource(drawableRight)
        mView?.iv_notification!!.setOnClickListener(rightBtnListener)
    }*/

    fun setHomeTitle(titleText: String) {
        mView?.tvTitle?.text = titleText
        mView?.tvTitle?.setTextColor(Color.parseColor("#6500FA"))
        mView?.btn_left.setImageResource(R.drawable.settings_icon)
        mView?.btn_profile.setImageResource(R.drawable.user_icon)
        mView?.btn_profile.visibility = View.VISIBLE

        mView?.btn_left!!.setOnClickListener {

            MainActivity.getMainActivity?.navController
                ?.navigate(R.id.settingsFragment)
        }
        mView?.btn_profile!!.setOnClickListener {

            MainActivity.getMainActivity?.navController
                ?.navigate(R.id.myProfileFragment)
        }

        MainActivity.getMainActivity?.showBottomBar()
    }


    fun setBtnBack(titleText: String, colorId: Int) {

        mView?.tvTitle?.text = titleText
        mView?.tvTitle?.setTextColor(colorId)
        mView?.btn_left.setImageResource(R.drawable.back_arrow_icon)

        mView?.btn_profile.visibility = View.GONE
        mView?.btn_user_profile.visibility = View.GONE

        MainActivity.getMainActivity?.hideBottomBar()
        mView?.btn_left!!.setOnClickListener {
            MainActivity.getMainActivity?.onBackPressed()
            // MainActivity.getMainActivity?.navController?.popBackStack()
        }
    }

    // For Preference Fragment
    fun setBtnBack(titleText: String, colorId: Int, fragment: String, activity: Activity) {

        mView?.tvTitle?.text = titleText
        // mView?.tvTitle?.setTextColor(colorId)
        mView?.tvTitle?.setTextColor(Color.parseColor("#6500FA"))
        mView?.btn_left.setImageResource(R.drawable.back_arrow_icon)

        mView?.btn_profile.visibility = View.GONE
        mView?.btn_user_profile.visibility = View.GONE

        MainActivity.getMainActivity?.hideBottomBar()
        mView?.btn_left!!.setOnClickListener {
//            if(fragment.equals("Preference",ignoreCase = true)){
////                MainActivity.getMainActivity
////                    ?.navController?.navigate(R.id.myProfileFragment)
//                activity.finish();
//                activity.startActivity(activity.getIntent());
//            }
////            else if(fragment.equals("Profile",ignoreCase = true)) {
////                MainActivity.getMainActivity
////                    ?.navController?.navigate(R.id.discoverFragment2)
////            }
//            else {
//                 MainActivity.getMainActivity?.onBackPressed()
//                // MainActivity.getMainActivity?.navController?.popBackStack()
//            }

            if (fragment.equals("Preference", ignoreCase = true)) {
                MainActivity.getMainActivity?.navController?.popBackStack()
            }

            if (fragment.equals("Profile", ignoreCase = true)) {
                //  MainActivity.getMainActivity?.supportFragmentManager?.beginTransaction()?.add(R.id.container, DiscoverFragment())?.commit()
                // Restart an antivity
                activity.finish()
                activity.startActivity(Intent(activity, MainActivity::class.java));
            }

            // MainActivity.getMainActivity?.navController?.popBackStack()
            // MainActivity.getMainActivity?.onBackPressed()
        }
    }

    // Orignal
//    fun setBtnBack( titleText:String,colorId:Int) {
//
//        mView?.tvTitle?.text = titleText
//        mView?.tvTitle?.setTextColor(colorId)
//        mView?.btn_left.setImageResource(R.drawable.back_arrow_icon)
//        mView?.btn_profile.visibility=View.INVISIBLE
//
//
//        MainActivity.getMainActivity?.hideBottomBar()
//        mView?.btn_left!!.setOnClickListener {
//            MainActivity.getMainActivity?.onBackPressed()
//        }
//    }

    fun setProfileBtn(titleText: String) {
        mView?.tvTitle?.text = titleText
        mView?.btn_left.setImageResource(R.drawable.back_arrow_icon)
        // mView?.bt.setImageResource(R.drawable.back_arrow_icon)

        mView?.btn_profile.visibility = View.GONE
        mView?.btn_user_profile.visibility = View.VISIBLE

        mView?.btn_left!!.setOnClickListener {

            MainActivity.getMainActivity?.onBackPressed()
        }
        mView?.btn_user_profile!!.setOnClickListener {

            MainActivity.getMainActivity?.navController
                ?.navigate(R.id.userProfileFragment)
        }



        MainActivity.getMainActivity?.hideBottomBar()

    }

    /** Dedicated method for conversation fragment*/
    @RequiresApi(Build.VERSION_CODES.M)
    fun setProfileBtn(titleText: String, imageUrl: String, from: String, userId: String) {
        // val imageURL: String = "" + Constants.imageURL + "" + imageUrl

        mView?.tvTitle?.text = titleText
        mView?.tvTitle?.setTextColor(context.getColor(R.color.purple))
        mView?.btn_left.setImageResource(R.drawable.back_arrow_icon)
        // mView?.bt.setImageResource(R.drawable.back_arrow_icon)

        mView?.btn_profile.visibility = View.GONE
        mView?.btn_user_profile.visibility = View.VISIBLE

        imageUrl.let {
            if (it != Constants.imageURL) {
                ImageLoadUtil.loadImageNotFit(it, mView.btn_user_profile, R.drawable.user_dp_placeholder)
            }
        }

        mView?.btn_left!!.setOnClickListener {

            MainActivity.getMainActivity?.onBackPressed()
        }
        mView?.btn_user_profile!!.setOnClickListener {
            val bundle = Bundle()

            bundle.putString("from", "" + from)
            bundle.putString("userId", "" + userId)
            bundle.putString("isChat", "" + "true")



            MainActivity.getMainActivity?.navController
                ?.navigate(R.id.userProfileFragment, bundle)
        }



        MainActivity.getMainActivity?.hideBottomBar()

    }

    /** For Conversation Fragment Titlebar*/
    fun setProfileBtn(titleText: String, photoURL: String) {
        mView?.tvTitle?.text = titleText
        mView?.btn_left.setImageResource(R.drawable.back_arrow_icon)
        mView?.btn_profile.visibility = View.GONE
        mView?.btn_user_profile.visibility = View.VISIBLE

        ImageLoadUtil.loadImageNotFit(photoURL, mView.iv_profile_image, R.drawable.user_dp)

        mView?.btn_left!!.setOnClickListener {

            MainActivity.getMainActivity?.onBackPressed()
        }


        mView?.btn_user_profile!!.setOnClickListener {

            MainActivity.getMainActivity?.navController
                ?.navigate(R.id.userProfileFragment)
        }

        MainActivity.getMainActivity?.hideBottomBar()

    }

/*    fun setBtnBack( listener: OnClickListener?) {

            mView?.ivBack!!.visibility = View.VISIBLE
            mView?.ivProfile!!.visibility = View.GONE
           // mView?.ivBack!!.setImageResource(drawable)
            mView?.ivBack!!.setOnClickListener(listener)
    }

    fun setBtnProfile(*//*drawable: Int,*//* listener: OnClickListener?) {

            mView?.ivBack!!.visibility = View.GONE
            mView?.ivProfile!!.visibility = View.VISIBLE
           // mView?.ivProfile!!.setImageResource(drawable)
            mView?.ivProfile!!.setOnClickListener(listener)


    }

    fun setBtnRight(drawable: Int, listener: OnClickListener?) {
        mView?.iv_notification!!.visibility = View.VISIBLE
        mView?.ivProfileRight!!.visibility = View.GONE
        mView?.iv_notification!!.setImageResource(drawable)
        mView?.iv_notification!!.setOnClickListener(listener)

    }

    fun setBtnRightUser(drawable: Int, listener: OnClickListener?) {
        mView?.iv_notification!!.visibility = View.GONE
        mView?.ivProfileRight!!.visibility = View.VISIBLE
        mView?.ivProfileRight!!.setImageResource(drawable)
        mView?.ivProfileRight!!.setOnClickListener(listener)

    }


    fun setTitle(getActivityContext: MainActivity, title: String) {
        mView?.rlTitlebarMainLayout?.setVisibility(View.VISIBLE)
        mView?.tvTitle?.text = title
     *//*   mView?.ivBack?.visibility = View.GONE

        mView?.iv_notification?.visibility = View.VISIBLE*//*


    }*/


/*    fun setHideTitle() {
        resetTitlebar()
        mView?.rlTitlebarMainLayout?.setVisibility(View.VISIBLE)
        mView?.btnMenu?.visibility = View.INVISIBLE
    }*/
}