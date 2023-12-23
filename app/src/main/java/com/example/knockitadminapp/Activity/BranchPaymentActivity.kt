package com.example.knockitadminapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.knockitadminapp.Database.PaymentDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityBranchPaymentBinding
import com.example.knockitadminapp.databinding.ActivityRiderPaymentBinding

class BranchPaymentActivity : AppCompatActivity() {

    lateinit var binding: ActivityBranchPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBranchPaymentBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        PaymentDatabase.loadBranchPayment(this, binding.riderPaymentRecyclerView)
    }
}