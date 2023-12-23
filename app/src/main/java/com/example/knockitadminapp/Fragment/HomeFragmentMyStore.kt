package com.example.knockitadminapp.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Activity.AddProductActivity
import com.example.knockitadminapp.Activity.MyStoreActivity
import com.example.knockitadminapp.Adapter.RiderAdapter
import com.example.knockitadminapp.Database.ProductDatabase
import com.example.knockitadminapp.Model.BranchModel
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.FragmentHomeMyStoreBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragmentMyStore : Fragment() {

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHomeMyStoreBinding = FragmentHomeMyStoreBinding.inflate(inflater, container, false)

        FirebaseFirestore.getInstance()
            .collection("BRANCHES")
            .document(FirebaseAuth.getInstance().uid.toString())
            .addSnapshotListener { value, error ->
                var connectWithRider = value?.getString("connectWithRider").toString()

                if (!connectWithRider.equals("")) {
                    Toast.makeText(context, "Rider Connected", Toast.LENGTH_SHORT)
                        .show()
                } else {

                }
            }

        FirebaseFirestore.getInstance().collection("BRANCHES")
            .document(FirebaseAuth.getInstance().uid.toString())
            .get()
            .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
                val model: BranchModel? = documentSnapshot.toObject(BranchModel::class.java)

                try {
                    if (model?.profile.equals("")) {
                        Glide.with(context!!).load(R.drawable.avatara).into(binding.profileImage)
                    } else {
                        Glide.with(context!!).load(model?.profile.toString())
                            .into(binding.profileImage)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
                binding.name.text = model?.name
                binding.email.text = model?.email
                binding.totalEarning.text = "₹"+model?.totalEarning

            })

        ProductDatabase.loadProduct(context!!, binding.productRecyclerView)

        binding.addProductBtn.setOnClickListener {
            startActivity(Intent(context, AddProductActivity::class.java))
        }

        FirebaseFirestore.getInstance()
            .collection("BRANCHES")
            .document(FirebaseAuth.getInstance().uid.toString())
            .addSnapshotListener { value, error ->
                var connectWithRider = value?.getString("connectWithRider").toString()

                if (!connectWithRider.toString().equals("")) {
                    binding.storeConnectionBg.visibility = View.VISIBLE
                    binding.storeConnection.cancelBtn.visibility = View.GONE
                    binding.storeConnection.OkBtn.text = "Rider Connected"
                    FirebaseFirestore.getInstance()
                        .collection("RIDERS")
                        .document(connectWithRider)
                        .addSnapshotListener { value, error ->
                            var storeOwnerName = value?.getString("name").toString()
                            var storeOwnerProfile = value?.getString("profile").toString()
                            var number = value?.getString("number").toString()
                            binding.storeConnectionBg.visibility = View.VISIBLE
                            binding.storeConnection.storeName.text = number
                            binding.storeConnection.storeOwnerName.text = storeOwnerName
                            binding.storeConnection.storeNumber.text = number
                            binding.storeConnection.storeNumber.visibility = View.INVISIBLE

                            binding.storeConnection.OkBtn.setOnClickListener {
                                val builder = AlertDialog.Builder(context)
                                builder.setTitle("Store")
                                builder.setMessage("Your order completed ?")

                                builder.setPositiveButton("Yes") { dialog, which ->
                                    val userData: MutableMap<String, Any?> = HashMap()
                                    userData["connectWithStore"] = ""
                                    FirebaseFirestore.getInstance()
                                        .collection("RIDERS")
                                        .document(connectWithRider.toString())
                                        .update(userData)
                                        .addOnCompleteListener {
                                            val userData: MutableMap<String, Any?> =
                                                HashMap()
                                            userData["connectWithRider"] = ""
                                            FirebaseFirestore.getInstance()
                                                .collection("BRANCHES")
                                                .document(FirebaseAuth.getInstance().uid.toString())
                                                .update(userData)
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        Toast.makeText(context, "Rider Disconnected", Toast.LENGTH_SHORT).show()
                                                    } else {

                                                    }
                                                }
                                        }
                                }
                                builder.setNegativeButton("No") { dialog, which ->
                                }

                                builder.show()

                            }

                            try {
                                Glide.with(context!!).load(storeOwnerProfile)
                                    .placeholder(R.drawable.avatara)
                                    .into(binding.storeConnection.storeOwnerProfile)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                } else {
                    binding.storeConnectionBg.visibility = View.GONE
                }
            }

        return binding.root
    }
}