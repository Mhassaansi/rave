package com.fictivestudios.ravebae.presentation.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.fragments.DiscoverFragment
import com.fictivestudios.ravebae.presentation.fragments.MyProfileFragment
import com.fictivestudios.ravebae.presentation.models.NotificationChatSenderModel
import com.fictivestudios.ravebae.utils.Constants
import com.fictivestudios.ravebae.utils.LoginModel
import com.fictivestudios.ravebae.utils.Titlebar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.spotify.sdk.android.auth.AuthorizationClient
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val extras = intent.extras
        var from: String? = null
        var FcmType: String? = null


        // catch from notification
        val intent = intent.extras

        if (intent != null || intent?.getString("notification_type") != "null") {
            FcmType = intent?.getString("notification_type")
            ConfigurationPref(this).chatReceiverId = intent?.getString("sender_id")
            ConfigurationPref(this).chatSenderPhotoUrl = "" + Constants.imageURL + ""+intent?.getString("sender_image")
            ConfigurationPref(this).chatUserName = intent?.getString("sender_name")
        }

        if (extras != null) {
            from = extras.getString("from")
        }

        getMainActivity = this

        navController = Navigation.findNavController(this, R.id.container);

        from?.let {
            if (from.equals("SignUp", ignoreCase = true)) {
                ConfigurationPref(this).from = "SignUp"
                supportFragmentManager.beginTransaction().add(R.id.container, DiscoverFragment())
                    .commit()
                // navController.navigate(R.id.preferencesFragment)
//                supportFragmentManager.beginTransaction().add(R.id.container, PreferencesFragment())
//                    .commit()
                supportFragmentManager.beginTransaction().add(R.id.container, MyProfileFragment())
                    .commit()
            }
        }
        // supportFragmentManager.beginTransaction().add(R.id.container,DiscoverFragment()).commit()

        // If opening from FCM Intent Service..
        // val intent = intent
        // FcmType = intent.getStringExtra("type")

        FcmType?.let {
            when (it) {
                // Open Notification
                "request_notify" -> {
                    val bundle = Bundle()
                    bundle.putString("fcm_type", FcmType)
                    navController.navigate(R.id.activityFragment, bundle)
                }
                // Open Chat
                "msg_notify" -> {
                    navController.navigate(R.id.conversationFragment)
                }

            }
        }


        val navView = bottomBar




        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item0 -> {
                    // handle click
                    navController.navigate(R.id.activityFragment)

                    true
                }
                R.id.item1 -> {
                    // handle click

                    navController.navigate(R.id.discoverFragment2)
                    true
                }
                R.id.item2 -> {
                    // handle click

                    navController.navigate(R.id.messagesFragment)
                    true
                }

                else -> false
            }
        }

        /*    bottomBar.setActiveItem(1)

            bottomBar.onItemSelected = {

                if (it==0)
                {
                    navController.navigate(R.id.activityFragment)
                }
                else if(it==1)
                {
                    navController.navigate(R.id.discoverFragment2)
                }
                else if (it==2)
                {
                    navController.navigate(R.id.messagesFragment)
                }

            }*/


    }


    fun getFragmentCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container)
        val backStackEntryCount = navHostFragment?.childFragmentManager?.backStackEntryCount

        // Condition for direct coming from signup
//        when (navController.currentDestination?.id) {
//            R.id.preferencesFragment -> {
//                navController.navigate(R.id.myProfileFragment)
//            }
//            R.id.myProfileFragment -> {
//                navController.navigate(R.id.discoverFragment2)
//            }
//            // navController.popBackStack()
//            else -> super.onBackPressed()
//        }

        if (backStackEntryCount == 0) {
            Log.d("entry", backStackEntryCount.toString())
            finish()
        } else {
            super.onBackPressed()
        }

//        super.onBackPressed()
    }

    override fun setMainFrameLayoutID() {

    }

    fun getTitlebar(): Titlebar {
        return main_title_bar
    }

    fun hideBottomBar() {
        bottomBar.visibility = View.GONE
        btn_home.visibility = View.GONE
    }

    fun showBottomBar() {
        bottomBar.visibility = View.VISIBLE
        btn_home.visibility = View.VISIBLE
    }

    companion object {
        var getMainActivity: MainActivity? = null
    }

    // For sending data to MyProifleFragment
    var onButtonClickListener: OnButtonClickListener? = null

    @JvmName("setOnButtonClickListener1")
    fun setOnButtonClickListener(onButtonClickListener: OnButtonClickListener?) {
        this.onButtonClickListener = onButtonClickListener
    }

    interface OnButtonClickListener {
        fun onButtonCLick(
            token: String?,
            type: String?,
            resultCode: Int?
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val rresponse = data?.extras?.get("response")
        Log.d("rresponse", "" + rresponse)

        val response = AuthorizationClient.getResponse(resultCode, data)
        Log.d("mainactivity", "" + response.code)
        Log.d("mainactivity", "response: " + response)
        Log.d("mainactivity", "accesstoken: " + response.accessToken)


        // response
        if (response != null) {
            // added null checker through let
            response.accessToken?.let {
                if (onButtonClickListener != null)
                    onButtonClickListener!!.onButtonCLick(
                        "" + response.accessToken,
                        "Access Token",
                        resultCode
                    )
            }
        } else {
            Toast.makeText(MainActivity@ this, "Spotify Authentication Failed", Toast.LENGTH_SHORT)
                .show()
        }
    }
}