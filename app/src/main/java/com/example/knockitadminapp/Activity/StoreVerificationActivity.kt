package com.example.knockitadminapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.knockitadminapp.Database.StoreDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityRiderVerificationBinding
import com.example.knockitadminapp.databinding.ActivityStoreVerificationBinding

class StoreVerificationActivity : AppCompatActivity() {

    lateinit var binding: ActivityStoreVerificationBinding
    companion object{
        lateinit var storeRecyclerView: RecyclerView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreVerificationBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)
        storeRecyclerView = findViewById(R.id.storeRecyclerView)

        StoreDatabase.loadPendingStores(this, binding.storeRecyclerView, "All")
    }
}