package com.riyas.whatsapp.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.riyas.whatsapp.R
import com.riyas.whatsapp.view.fragments.LoginFragment
import com.riyas.whatsapp.view.fragments.StatusFragment

class MainActivity : AppCompatActivity() {
    lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        mNavController = findNavController(R.id.my_nav_host_fragment)
    }

    override fun onBackPressed() {
        val mNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
        val currentFragment = mNavHostFragment?.childFragmentManager?.fragments?.get(0)
        if (currentFragment is StatusFragment || currentFragment is LoginFragment) {
            finish()
            return
        }

    }
}
