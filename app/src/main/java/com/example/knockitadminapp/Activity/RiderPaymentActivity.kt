package com.example.knockitadminapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.knockitadminapp.Database.PaymentDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityCouponsBinding
import com.example.knockitadminapp.databinding.ActivityRiderPaymentBinding

class RiderPaymentActivity : AppCompatActivity() {

    lateinit var binding: ActivityRiderPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiderPaymentBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        PaymentDatabase.loadRiderPayment(this, binding.riderPaymentRecyclerView)
    }
}