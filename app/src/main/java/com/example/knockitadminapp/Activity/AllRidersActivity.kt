package com.example.knockitadminapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.knockitadminapp.Database.RiderDatabase
import com.example.knockitadminapp.Database.UsersDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityAllRidersBinding
import com.example.knockitadminapp.databinding.ActivityUsersBinding

class AllRidersActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllRidersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllRidersBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        RiderDatabase.loadAllRiders(this, binding.riderRecyclerView)
    }
}