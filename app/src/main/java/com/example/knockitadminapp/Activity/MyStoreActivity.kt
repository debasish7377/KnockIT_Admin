package com.example.knockitadminapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.knockitadminapp.Fragment.HomeFragmentMyStore
import com.example.knockitadminapp.Fragment.RiderFragmentMyStore
import com.example.knockitadminapp.Fragment.WalletFragmentMyStore
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityMyStoreBinding

class MyStoreActivity : AppCompatActivity() {
    val HOME_FRAGMENT = 0
    val CATEGORY_FRAGMENT = 1
    val MY_ODER_FRAGMENT = 2
    val WALLET_FRAGMENT = 3
    val PROFILE_FRAGMENT = 5
    val RIDER_FRAGMENT = 4
    var CurrentFragment = -1

    lateinit var binding: ActivityMyStoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyStoreBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)
        setFragment(HomeFragmentMyStore(), HOME_FRAGMENT)
        window.setStatusBarColor(ContextCompat.getColor(this@MyStoreActivity,R.color.primary));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)

        binding.bottomNavigationView!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    setCheckedChancel()
                    invalidateOptionsMenu()
                    setFragment(HomeFragmentMyStore(), HOME_FRAGMENT)
                    window.setStatusBarColor(ContextCompat.getColor(this@MyStoreActivity,R.color.primary));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }

                R.id.oder -> {
                    setCheckedChancel()
                    invalidateOptionsMenu()
                    startActivity(Intent(this, DeliveryActivity::class.java))
                    item.isChecked = true
                    window.setStatusBarColor(ContextCompat.getColor(this@MyStoreActivity,R.color.primary));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }

                R.id.rider -> {
                    setCheckedChancel()
                    invalidateOptionsMenu()
                    setFragment(RiderFragmentMyStore(), RIDER_FRAGMENT)
                    item.isChecked = true
                    window.setStatusBarColor(ContextCompat.getColor(this@MyStoreActivity,R.color.white));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }

                R.id.wallet -> {
                    setCheckedChancel()
                    invalidateOptionsMenu()
                    startActivity(Intent(this, WalletActivity::class.java))
                    item.isChecked = true
                    window.setStatusBarColor(ContextCompat.getColor(this@MyStoreActivity,R.color.white));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
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
    }

}