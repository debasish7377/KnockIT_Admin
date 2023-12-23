package com.example.knockitadminapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Model.BannerModel
import com.example.knockitadminapp.R
import com.google.firebase.firestore.FirebaseFirestore

class BannerAdapter(var context: Context, var model: List<BannerModel>) :
    RecyclerView.Adapter<BannerAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View =
            LayoutInflater.from(context).inflate(R.layout.item_banner_layout, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        Glide.with(context).load(model[position].bannerImage).into(holder.bannerImage)
        holder.bannerImageBg.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(model[position].bannerBackground))

        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
            builder.setMessage("Are you sure to delete category ?")

            builder.setPositiveButton("Yes") { dialog, which ->

                FirebaseFirestore.getInstance()
                    .collection("HOME")
                    .document("HomeData")
                    .collection("Banner")
                    .document(model[position].id)
                    .delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            notifyDataSetChanged()
                            Toast.makeText(context, "Banner Deleted Successfully", Toast.LENGTH_SHORT).show()
                        } else {

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

        var bannerImage: ImageView = itemView.findViewById<ImageView?>(R.id.banner_image)
        var bannerImageBg: ConstraintLayout =
            itemView.findViewById<ConstraintLayout?>(R.id.banner_image_bg)
    }
}