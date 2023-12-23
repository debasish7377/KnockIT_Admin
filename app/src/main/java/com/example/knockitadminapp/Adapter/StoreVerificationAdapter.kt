package com.example.knockitadminapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Activity.RiderVerificationActivity
import com.example.knockitadminapp.Activity.StoreDetailsActivity
import com.example.knockitadminapp.Activity.StoreVerificationActivity
import com.example.knockitadminapp.Database.RiderDatabase
import com.example.knockitadminapp.Database.StoreDatabase
import com.example.knockitadminapp.Model.BranchModel
import com.example.knockitadminapp.R
import com.google.firebase.firestore.FirebaseFirestore

class StoreVerificationAdapter(var context: Context, var model: List<BranchModel>) : RecyclerView.Adapter<StoreVerificationAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_stores,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        Glide.with(context).load(model[position].storeImage).into(holder.storeImage)
        holder.storeTitle.text = model[position].storeName
        holder.storeDesc.text = model[position].storeDescription
        holder.storeTiming.text = model[position].deliveryTiming

        holder.storeVerification.visibility = View.VISIBLE
        holder.storeVerification.setOnClickListener {
            val userData: MutableMap<String, Any?> =
                HashMap()
            userData["storeVerification"] = "Verified"
            FirebaseFirestore.getInstance()
                .collection("BRANCHES")
                .document(model[position].storeId)
                .update(userData)
                .addOnCompleteListener {
                    Toast.makeText(context, "Store Verified", Toast.LENGTH_SHORT).show()
                    StoreDatabase.loadPendingStores(context, StoreVerificationActivity.storeRecyclerView, "All")
                }
        }

        holder.itemView.setOnClickListener {
            var intent = Intent(context, StoreDetailsActivity::class.java)
            intent.putExtra("storeId", model[position].storeId)
            intent.putExtra("storeImage", model[position].storeImage)
            intent.putExtra("storeProfile", model[position].profile)
            intent.putExtra("storeName", model[position].storeName)
            intent.putExtra("storeDesc", model[position].storeDescription)
            intent.putExtra("deliveryTiming", model[position].deliveryTiming)
            context.startActivity(intent)
        }

        }

    override fun getItemCount(): Int {
        return model.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var storeImage: ImageView = itemView.findViewById<ImageView?>(R.id.storeImage)
        var storeTitle: TextView  = itemView.findViewById<TextView?>(R.id.storeTitle)
        var storeDesc: TextView  = itemView.findViewById<TextView?>(R.id.storeDesc)
        var storeTiming: TextView  = itemView.findViewById<TextView?>(R.id.deliveryTiming)
        var storeVerification: AppCompatButton  = itemView.findViewById<AppCompatButton?>(R.id.storeVerification)
    }
}