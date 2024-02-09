package com.example.knockitadminapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Database.MyOderDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityUserDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class UserDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: ActivityUserDetailsBinding
    lateinit var map: GoogleMap
    lateinit var latitude: String
    lateinit var longitude: String
    lateinit var address: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        setSupportActionBar(binding.toolbar17);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        }

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment;
        mapFragment.getMapAsync(this)


        var name = intent.getStringExtra("name").toString()
        var number = intent.getStringExtra("number").toString()
        var profile = intent.getStringExtra("profile").toString()
        address = intent.getStringExtra("address").toString()
        var city = intent.getStringExtra("city").toString()
        var pincode = intent.getStringExtra("pincode").toString()
        latitude = intent.getStringExtra("latitude").toString()
        longitude = intent.getStringExtra("longitude").toString()
        var uid = intent.getStringExtra("uid").toString()

        if (profile.equals("")){
            Glide.with(this).load(R.drawable.avatara).into(binding.circleImageView2)
        }else{
            Glide.with(this).load(profile).placeholder(R.drawable.avatara).into(binding.circleImageView2)
        }
        binding.userName.text = "Name :- "+name
        binding.userNumber.text = "Number :- "+number
        binding.userAddress.text = "Address :- "+address
        binding.userCity.text = "City :- "+city
        binding.userPincode.text = "Pincode :- "+pincode

        MyOderDatabase.loadUserOder(this, binding.userOrderRecyclerView, "", uid)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


        // Sample coordinates
        val latLngOrigin = LatLng(latitude.toDouble(), longitude.toDouble()) // Ayala
        val latLngDestination = LatLng(10.311795, 123.915864) // SM City
        this.map!!.addMarker(MarkerOptions().position(latLngOrigin).title(address))
        //this.map!!.addMarker(MarkerOptions().position(latLngDestination).title("SM City"))
        this.map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 17.5f))
    }
}