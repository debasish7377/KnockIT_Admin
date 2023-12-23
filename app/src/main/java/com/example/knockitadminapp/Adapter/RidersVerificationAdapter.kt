package com.example.knockitadminapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Activity.RiderDetails
import com.example.knockitadminapp.Activity.RiderVerificationActivity
import com.example.knockitadminapp.Database.RiderDatabase
import com.example.knockitadminapp.Model.CouponsModel
import com.example.knockitadminapp.Model.RiderModel
import com.example.knockitadminapp.Model.UserModel
import com.example.knockitadminapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Date

class RidersVerificationAdapter(var context: Context, var model: List<RiderModel>) :
    RecyclerView.Adapter<RidersVerificationAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View =
            LayoutInflater.from(context).inflate(R.layout.item_riders, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.name.text = model[position].name
        holder.email.text = model[position].email
        if (model[position].profile.equals("")){
            Glide.with(context).load(R.drawable.avatara).into(holder.profile)
        }else{
            Glide.with(context).load(model[position].profile).placeholder(R.drawable.avatara).into(holder.profile)
        }

        holder.connectRider.text = "Verify"
        holder.connectRider.setOnClickListener {
            val userData: MutableMap<String, Any?> =
                HashMap()
            userData["driverAccount"] = "Verified"
            FirebaseFirestore.getInstance()
                .collection("RIDERS")
                .document(model[position].riderId)
                .update(userData)
                .addOnCompleteListener {
                    Toast.makeText(context, "Rider Verified", Toast.LENGTH_SHORT).show()
                    RiderDatabase.loadPendingRiders(context, RiderVerificationActivity.riderRecyclerView)
                }
        }

        holder.itemView.setOnClickListener {
            var intent = Intent(context, RiderDetails::class.java)
            intent.putExtra("riderId", model[position].riderId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var profile: CircleImageView = itemView.findViewById(R.id.imageView6)
        var name: TextView = itemView.findViewById(R.id.riderName)
        var email: TextView = itemView.findViewById(R.id.riderEmail)
        var connectRider: AppCompatButton = itemView.findViewById(R.id.connectRider)
    }
}