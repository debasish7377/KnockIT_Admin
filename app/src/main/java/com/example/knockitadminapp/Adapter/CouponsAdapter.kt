package com.example.knockitadminapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.knockitadminapp.Model.CouponsModel
import com.example.knockitadminapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import java.text.SimpleDateFormat
import java.util.Date

class CouponsAdapter(var context: Context, var model: List<CouponsModel>) :
    RecyclerView.Adapter<CouponsAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View =
            LayoutInflater.from(context).inflate(R.layout.item_coupon_redemption, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.title.text = model[position].subject
        val sdf = SimpleDateFormat("dd/MM/yy hh:mm a")
        val netDate = Date(model[position].validTime)
        val date = sdf.format(netDate)
        holder.validDate.text = "Valid - " + date

        holder.itemView.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
            builder.setMessage("Are you sure to delete category ?")

            builder.setPositiveButton("Yes") { dialog, which ->

                FirebaseFirestore.getInstance()
                    .collection("Coupons")
                    .document(model[position].id)
                    .delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Successfully coupon deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }

            builder.setNegativeButton("No") { dialog, which ->
            }
            builder.show()
            true

        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.couponTitle)
        var validDate: TextView = itemView.findViewById(R.id.validDate)
        var usedCoupon: TextView = itemView.findViewById(R.id.used_coupon)
        var couponBg: ConstraintLayout = itemView.findViewById(R.id.coupon_bg)
    }
}