package com.example.knockitadminapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.knockitadminapp.Database.RiderDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityAddProductBinding
import com.example.knockitadminapp.databinding.ActivityRiderVerificationBinding

class RiderVerificationActivity : AppCompatActivity() {

    lateinit var binding: ActivityRiderVerificationBinding
    companion object{
        lateinit var riderRecyclerView: RecyclerView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiderVerificationBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)
        riderRecyclerView = findViewById(R.id.riderRecyclerView)

        RiderDatabase.loadPendingRiders(this, binding.riderRecyclerView)
    }
}