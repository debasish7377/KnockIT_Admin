package com.example.knockitadminapp.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.knockitadminapp.Database.RiderDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.FragmentRiderMyStoreBinding

class RiderFragmentMyStore : Fragment() {

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentRiderMyStoreBinding = FragmentRiderMyStoreBinding.inflate(inflater, container, false)

        RiderDatabase.loadRider(context!!, binding.riderRecyclerView)
        return binding.root
    }
}