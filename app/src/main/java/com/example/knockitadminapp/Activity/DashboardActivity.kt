package com.example.knockitadminapp.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.knockitadminapp.Fragment.HomeFragment
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityDashboardBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    val HOME_FRAGMENT = 0
    val CATEGORY_FRAGMENT = 1
    val MY_ODER_FRAGMENT = 2
    val WALLET_FRAGMENT = 3
    val PROFILE_FRAGMENT = 5
    val RIDER_FRAGMENT = 4
    var CurrentFragment = -1

    lateinit var binding: ActivityDashboardBinding
    lateinit var reviewDialog: Dialog
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        
        setFragment(HomeFragment(), HOME_FRAGMENT)
        window.setStatusBarColor(ContextCompat.getColor(this@DashboardActivity,R.color.primary));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)

        binding.bottomNavigationView!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    setCheckedChancel()
                    invalidateOptionsMenu()
                    setFragment(HomeFragment(), HOME_FRAGMENT)
                    window.setStatusBarColor(ContextCompat.getColor(this@DashboardActivity,R.color.primary));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }

                R.id.category -> {
                    setCheckedChancel()
                    invalidateOptionsMenu()
                    startActivity(Intent(this, CategoryActivity::class.java))
                    item.isChecked = true
                    window.setStatusBarColor(ContextCompat.getColor(this@DashboardActivity,R.color.primary));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }

                R.id.stores -> {
                    setCheckedChancel()
                    invalidateOptionsMenu()
                    startActivity(Intent(this, StoresActivity::class.java))
                    item.isChecked = true
                    window.setStatusBarColor(ContextCompat.getColor(this@DashboardActivity,R.color.primary));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }

                R.id.rider -> {
                    setCheckedChancel()
                    invalidateOptionsMenu()
                    startActivity(Intent(this, AllRidersActivity::class.java))
                    item.isChecked = true
                    window.setStatusBarColor(ContextCompat.getColor(this@DashboardActivity,R.color.primary));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }

                R.id.coupons -> {
                    setCheckedChancel()
                    invalidateOptionsMenu()
                    startActivity(Intent(this, CouponsActivity::class.java))
                    item.isChecked = true
                    window.setStatusBarColor(ContextCompat.getColor(this@DashboardActivity,R.color.primary));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment, fragmentNo: Int) {
        if (fragmentNo != CurrentFragment) {
            CurrentFragment = fragmentNo
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                R.anim.slide_from_right,
                R.anim.slideout_from_left
            )
            fragmentTransaction.replace(binding.frameLayout!!.id, fragment)
            fragmentTransaction.commit()
        }
    }

    private fun setCheckedChancel() {
        binding.bottomNavigationView.getMenu().getItem(0).setChecked(false)
        binding.bottomNavigationView.getMenu().getItem(1).setChecked(false)
        binding.bottomNavigationView.getMenu().getItem(2).setChecked(false)
        binding.bottomNavigationView.getMenu().getItem(3).setChecked(false)
        binding.bottomNavigationView.getMenu().getItem(4).setChecked(false)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}