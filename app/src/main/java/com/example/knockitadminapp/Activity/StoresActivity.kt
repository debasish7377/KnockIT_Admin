package com.example.knockitadminapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.knockitadminapp.Database.CategoryDatabase
import com.example.knockitadminapp.Database.StoreDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityStoresBinding

class StoresActivity : AppCompatActivity() {

    lateinit var binding: ActivityStoresBinding
    companion object {
        lateinit var storeRecyclerView: RecyclerView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoresBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)
        storeRecyclerView = findViewById(R.id.storeRecyclerView)

        StoreDatabase.loadStore(this, binding.storeRecyclerView, "All")
        CategoryDatabase.loadCategoryByStore(this, binding.categoryRecyclerView)
    }
}