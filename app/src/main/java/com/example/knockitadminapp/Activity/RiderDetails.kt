package com.example.knockitadminapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Database.RiderDatabase
import com.example.knockitadminapp.Database.StoreDatabase
import com.example.knockitadminapp.Model.RiderModel
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityMyStoreBinding
import com.example.knockitadminapp.databinding.ActivityRiderDetailsBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

class RiderDetails : AppCompatActivity() {

    lateinit var binding: ActivityRiderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiderDetailsBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)
        var riderId = intent.getStringExtra("riderId")

        FirebaseFirestore.getInstance()
            .collection("RIDERS")
            .document(riderId.toString())
            .addSnapshotListener { querySnapshot: DocumentSnapshot?, e: FirebaseFirestoreException? ->
                querySnapshot?.let {
                    val riderModel = it.toObject(RiderModel::class.java)
                    if (!riderModel?.profile.equals("")) {
                        Glide.with(this).load(riderModel?.profile.toString())
                            .placeholder(R.drawable.avatara).into(binding.imageView4)
                    } else {
                        Glide.with(this).load(R.drawable.avatara).into(binding.imageView4)
                    }
                    binding.name.text = riderModel?.name.toString()
                    binding.email.text = riderModel?.email.toString()
                    binding.phone.text = riderModel?.number.toString()

                    binding.accountHolderName.text = riderModel?.bankHolderName
                    binding.accountNumber.text = riderModel?.bankAccountNumber
                    binding.ifscCode.text = riderModel?.bankIFSCCode
                    binding.payment.text = "₹" + riderModel?.totalEarning
                    binding.bankName.text = riderModel?.bankName

                    Glide.with(this).load(riderModel?.drivingLicenceImage_1).into(binding.drivingLi1)
                    Glide.with(this).load(riderModel?.drivingLicenceImage_2).into(binding.drivingLi2)
                }
            }

        RiderDatabase.loadRiderDeliveryProduct(this, binding.riderDeliveryRecyclerView, riderId.toString())
    }
}