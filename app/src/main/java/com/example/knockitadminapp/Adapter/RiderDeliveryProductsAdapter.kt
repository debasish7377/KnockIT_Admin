package com.example.knockitadminapp.Adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Activity.AllProductsActivity
import com.example.knockitadminapp.Model.BranchModel
import com.example.knockitadminapp.Model.ProductModel
import com.example.knockitadminapp.R
import com.example.knockitbranchapp.Model.MyOderModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class RiderDeliveryProductsAdapter(var context: Context, var model: List<MyOderModel>) :
    RecyclerView.Adapter<RiderDeliveryProductsAdapter.viewHolder>() {

    companion object {
        var selectQtyDialog: Dialog? = null
        var price: String = ""
        var show_dialog: Boolean = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View =
            LayoutInflater.from(context).inflate(R.layout.item_my_delivered_oder, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        FirebaseFirestore.getInstance()
            .collection("PRODUCTS")
            .document(model[position].productId)
            .get()
            .addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                if (task.isSuccessful) {
                    Glide.with(context).load(task.result.getString("productImage").toString())
                        .into(holder.productImage)
                }
            })
        holder.productTitle.text = model[position].productTitle
        holder.productPrice.text = model[position].productPrice.toString()
        holder.productCuttedPrice.text = model[position].productCuttedPrice.toString()
        holder.yourPrice.text = model[position].price.toString()
        holder.qty_text.text = model[position].qty
        holder.qty_no.text = "qty : " + model[position].qtyNo.toString()
        holder.userUid.text = model[position].uid
        var youSaved: String =
            (model[position].productCuttedPrice.toInt() - model[position].productPrice.toInt()).toString()
        holder.discountedPrice.text = "₹" + youSaved + " Saved"
        if (model[position].delivery.equals("Delivered")) {
            holder.deliveryBtn.text = "Order Successfully Completed"
            holder.canceledBtn.visibility = View.GONE
            holder.deliveryBtn.setOnClickListener {
                Toast.makeText(context, "Order already Delivered", Toast.LENGTH_SHORT).show()
            }
        } else if (model[position].delivery.equals("Canceled")) {
            holder.canceledBtn.text = "Order Canceled"
            holder.deliveryBtn.visibility = View.GONE
        }

//        holder.itemView.setOnClickListener {
//            var intent = Intent(context, UpdateProductActivity::class.java)
//            intent.putExtra("productId", model[position].id)
//            context.startActivity(intent)
//        }

    }

    override fun getItemCount(): Int {
        return model.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var productPrice: TextView = itemView.findViewById<TextView?>(R.id.mini_product_price)
        var productCuttedPrice: TextView =
            itemView.findViewById<TextView?>(R.id.mini_product_cutted_price)
        var productTitle: TextView = itemView.findViewById<TextView?>(R.id.mini_product_title)
        var productImage: ImageView = itemView.findViewById<ImageView?>(R.id.mini_product_image)
        var discountedPrice: TextView = itemView.findViewById<TextView?>(R.id.discount_text)
        var qty_text: TextView = itemView.findViewById<TextView?>(R.id.qty_text)
        var qty_no: TextView = itemView.findViewById<TextView?>(R.id.qty_no)
        var yourPrice: TextView = itemView.findViewById<TextView?>(R.id.yourPrice)
        var userUid: TextView = itemView.findViewById<TextView?>(R.id.userUid)

        var canceledBtn: AppCompatButton = itemView.findViewById<AppCompatButton?>(R.id.canceledBtn)
        var deliveryBtn: AppCompatButton = itemView.findViewById<AppCompatButton?>(R.id.deliveryBtn)

    }
}