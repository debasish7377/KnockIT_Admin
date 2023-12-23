package com.example.knockitadminapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.knockitadminapp.Database.UsersDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityBannerBinding
import com.example.knockitadminapp.databinding.ActivityUsersBinding

class UsersActivity : AppCompatActivity() {

    lateinit var binding: ActivityUsersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        UsersDatabase.loadUsers(this, binding.usersRecyclerView)
    }
}