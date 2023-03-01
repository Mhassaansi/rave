package com.fictivestudios.ravebae.presentation.fragments

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.viewmodels.BaseViewModel
import com.fictivestudios.ravebae.utils.CustomProgressDialogue
import com.fictivestudios.ravebae.utils.Titlebar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

abstract class BBaseFragment<VM : BaseViewModel?> : Fragment() {
    val FCM_TAG: String = "FCMtoken"
    var FCMtoken :  String ? = null

    @JvmField
    protected var viewModel: VM? = null
    @JvmField
    var progress: CustomProgressDialogue? = null
    protected abstract fun createViewModel(): VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progress = CustomProgressDialogue(activity)
        // Testing
        progress?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


        viewModel = createViewModel()


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(FCM_TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            FCMtoken = task.result

            // Log and toast
            // val msg = getString(R.string.msg_token_fmt, token)
            Log.d(FCM_TAG, FCMtoken.toString())
            // Toast.makeText(requireActivity(), "FCM Token: "+FCMtoken, Toast.LENGTH_SHORT).show()
        })
    }

    fun isValidEmail(target: CharSequence): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return target.toString().matches(emailPattern.toRegex())
        //  return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    /** Base Fragment*/
    var getActivityContext: MainActivity? = null

    abstract fun setTitlebar(titlebar: Titlebar)

    fun getActivityContext(): Activity? {
        return getActivityContext
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            val contex = context as MainActivity?
            if (contex != null)
                getActivityContext = context
        }

    }

    override fun onResume() {
        super.onResume()
        if (getActivityContext != null) {
            setTitlebar(getActivityContext!!.getTitlebar())
        }


    }

}