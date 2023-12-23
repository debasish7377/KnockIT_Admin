package com.example.knockitadminapp.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Activity.AllProductsActivity
import com.example.knockitadminapp.Activity.BannerActivity
import com.example.knockitadminapp.Activity.BranchPaymentActivity
import com.example.knockitadminapp.Activity.CouponsActivity
import com.example.knockitadminapp.Activity.MainActivity
import com.example.knockitadminapp.Activity.MyStoreActivity
import com.example.knockitadminapp.Activity.RiderPaymentActivity
import com.example.knockitadminapp.Activity.RiderVerificationActivity
import com.example.knockitadminapp.Activity.StoreVerificationActivity
import com.example.knockitadminapp.Activity.StoresActivity
import com.example.knockitadminapp.Activity.UsersActivity
import com.example.knockitadminapp.Activity.DealsOfTheDayActivity
import com.example.knockitadminapp.Database.BannerDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        BannerDatabase.loadBanner(context!!, binding.bannerLayout.bannerRecyclerView)

        FirebaseFirestore.getInstance()
            .collection("ADMIN")
            .document(FirebaseAuth.getInstance().uid.toString())
            .addSnapshotListener { value, error ->
                var name = value?.getString("name").toString()
                var email = value?.getString("email").toString()
                var profile = value?.getString("profile").toString()

                try {
                    if (profile.equals("")) {
                        Glide.with(context!!).load(R.drawable.avatara).into(binding.profileImage)
                    } else {
                        Glide.with(context!!).load(profile.toString())
                            .into(binding.profileImage)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
                binding.name.text = name
                binding.email.text = email
            }

        binding.layoutHome.myStore.setOnClickListener {
            FirebaseFirestore.getInstance()
                .collection("ADMIN")
                .document(FirebaseAuth.getInstance().uid.toString())
                .addSnapshotListener { value, error ->
                    var connectWithRider = value?.getString("yourStore").toString()
                    var name = value?.getString("name").toString()
                    var email = value?.getString("email").toString()
                    var profile = value?.getString("profile").toString()

                    try {
                        if (profile.equals("")) {
                            Glide.with(context!!).load(R.drawable.avatara).into(binding.profileImage)
                        } else {
                            Glide.with(context!!).load(profile.toString())
                                .into(binding.profileImage)
                        }
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                    binding.name.text = name
                    binding.email.text = email

                    if (connectWithRider.toString().equals("false")) {
                        startActivity(Intent(context, MainActivity::class.java))
                    } else {
                        startActivity(Intent(context, MyStoreActivity::class.java))
                    }
                }
        }

        binding.layoutHome.branches.setOnClickListener {
            startActivity(Intent(context, StoresActivity::class.java))
        }
        binding.layoutHome.allProducts.setOnClickListener {
            startActivity(Intent(context, AllProductsActivity::class.java))
        }
        binding.layoutHome.addBanner.setOnClickListener {
            startActivity(Intent(context, BannerActivity::class.java))
        }
        binding.layoutHome.playQuiz.setOnClickListener {
            startActivity(Intent(context, CouponsActivity::class.java))
        }
        binding.layoutHome.riderPayment.setOnClickListener {
            startActivity(Intent(context, RiderPaymentActivity::class.java))
        }
        binding.layoutHome.branchPayment.setOnClickListener {
            startActivity(Intent(context, BranchPaymentActivity::class.java))
        }
        binding.layoutHome.users.setOnClickListener {
            startActivity(Intent(context, UsersActivity::class.java))
        }
        binding.layoutHome.riderVerify.setOnClickListener {
            startActivity(Intent(context, RiderVerificationActivity::class.java))
        }
        binding.layoutHome.storeVerify.setOnClickListener {
            startActivity(Intent(context, StoreVerificationActivity::class.java))
        }
        binding.layoutHome.dealsOfTheDay.setOnClickListener {
            startActivity(Intent(context, DealsOfTheDayActivity::class.java))
        }

        return binding.root
    }
}