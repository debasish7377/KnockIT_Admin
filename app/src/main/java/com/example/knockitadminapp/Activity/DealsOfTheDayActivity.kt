package com.example.knockitadminapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.knockitadminapp.Database.DealsOfTheDayDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityDealsOfTheDayBinding

class DealsOfTheDayActivity : AppCompatActivity() {

    lateinit var binding: ActivityDealsOfTheDayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDealsOfTheDayBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)
        DealsOfTheDayDatabase.loadDealsOfTheDay(this,binding.dealsOfTheDayRecyclerView)


    }
}