package com.example.knockitadminapp.Adapter

import android.app.AlertDialog
import android.app.Dialog
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
import com.example.knockitadminapp.Activity.DealsOfTheDayActivity
import com.example.knockitadminapp.Model.DealsOfTheDayModel
import com.example.knockitadminapp.Model.ProductModel
import com.example.knockitadminapp.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.UUID

class DealsOfTheDayAdapter(var context: Context, var model: List<DealsOfTheDayModel>) : RecyclerView.Adapter<DealsOfTheDayAdapter.viewHolder>() {

    lateinit var loadingDialog: Dialog
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_deals_of_the_day,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        ////// load productDetails
        FirebaseFirestore.getInstance().collection("PRODUCTS")
            .document(model[position].productId)
            .get()
            .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
                val productModel: ProductModel? =
                    documentSnapshot.toObject(ProductModel::class.java)

                holder.miniProductTitle.text = productModel?.productTitle!!
                holder.miniProductPrice.text = "₹"+productModel.productPrice
                Glide.with(context).load(productModel.productImage).into(holder.miniProductImage)
                holder.miniProductCuttedPrice.text = "₹"+productModel.productCuttedPrice
                holder.miniProductRatting.text  = productModel.productRating
                holder.miniProductTotalRatting.text = productModel.productTotalRating
                if (productModel.productPrice.toLong() >= 800){
                    holder.miniProductDelivery.text = "Free Delivery"
                }else{
                    holder.miniProductDelivery.text = "80₹ Rupees Delivery Price"
                }
            })
        ////// load productDetails

        ////////////////loading dialog
        loadingDialog = Dialog(context)
        loadingDialog?.setContentView(R.layout.dialog_loading)
        loadingDialog?.setCancelable(true)
        loadingDialog.window?.setBackgroundDrawable(context.getDrawable(R.drawable.btn_buy_now))
        loadingDialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        ////////////////loading dialog

        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Deals of the day")
            builder.setMessage("Are you sure to delete this ?")

            builder.setPositiveButton("Yes") { dialog, which ->

                FirebaseFirestore.getInstance()
                    .collection("HOME")
                    .document("HomeData")
                    .collection("Deals_of_the_day")
                    .document(model[position].id)
                    .delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Successfully deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                            notifyDataSetChanged()
                        }
                    }

            }
            builder.setNegativeButton("No") { dialog, which ->
            }

            builder.show()
            true
        }

//        holder.itemView.setOnClickListener {
//            var intent = Intent(context, ProductDetailsActivity::class.java)
//            intent.putExtra("productId",model[position].productId)
//            context.startActivity(intent)
//        }

        }

    override fun getItemCount(): Int {
        return model.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var miniProductTitle: TextView = itemView.findViewById<TextView?>(R.id.mini_product_title)
        var miniProductPrice: TextView = itemView.findViewById<TextView?>(R.id.mini_product_price)
        var miniProductRatting: TextView = itemView.findViewById<TextView?>(R.id.mini_product_ratting_text)
        var miniProductTotalRatting: TextView = itemView.findViewById<TextView?>(R.id.mini_product_total_ratting)
        var miniProductDelivery: TextView = itemView.findViewById<TextView?>(R.id.mini_product_delivery)
        var miniProductCuttedPrice: TextView = itemView.findViewById<TextView?>(R.id.mini_product_cutted_price)
        var miniProductImage: ImageView = itemView.findViewById<ImageView?>(R.id.mini_product_image)
    }
}