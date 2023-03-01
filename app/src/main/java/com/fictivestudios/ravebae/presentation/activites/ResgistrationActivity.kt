package com.fictivestudios.ravebae.presentation.activites

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.fictivestudios.ravebae.R

class ResgistrationActivity : AppCompatActivity() {

    lateinit var navControllerReg : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)

        getRegActivity = this

        navControllerReg = Navigation.findNavController(this, R.id.container_reg);

        navControllerReg.navigate(R.id.preLoginFragment)
    }
    companion object{
        var getRegActivity: ResgistrationActivity? =null
    }

    fun getFragmentCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

    override fun onBackPressed() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_reg)
        val backStackEntryCount = navHostFragment?.childFragmentManager?.backStackEntryCount





        if (backStackEntryCount == 1) {
            Log.d("entry",backStackEntryCount.toString())
          finish()
        } else {
            super.onBackPressed()
        }
    }
}