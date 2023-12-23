package com.example.knockitadminapp.Adapter

import android.app.AlertDialog
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
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Activity.AllProductsActivity
import com.example.knockitadminapp.Activity.DealsOfTheDayActivity
import com.example.knockitadminapp.Model.ProductModel
import com.example.knockitadminapp.R
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject
import java.util.UUID
import javax.xml.transform.ErrorListener
import javax.xml.transform.TransformerException

class ProductsAdapter(var context: Context, var model: List<ProductModel>) :
    RecyclerView.Adapter<ProductsAdapter.viewHolder>() {

    companion object {
        var selectQtyDialog: Dialog? = null
        var price: String = ""
        var show_dialog: Boolean = false
    }
    lateinit var loadingDialog: Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View =
            LayoutInflater.from(context).inflate(R.layout.item_mini_product_view, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

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

        holder.miniProductTitle.text = model[position].productTitle
        holder.miniProductPrice.text = model[position].productPrice.toString()
        holder.miniProductCuttedPrice.text = model[position].productCuttedPrice.toString()
        holder.miniProductRatting.text = model[position].productRating
        holder.miniProductTotalRatting.text = model[position].productTotalRating
        holder.miniProductBrand.text = model[position].productBrandName
        holder.productId.text = model[position].id
        holder.deleteProduct.visibility = View.GONE

        if (model[position].productPrice.toLong() >= 800) {
            holder.miniProductDelivery.text = "Free Delivery"
        } else {
            holder.miniProductDelivery.text = "80₹ Rupees Delivery Price"
        }
        var discount =
            100 - ((model[position].productPrice.toFloat() / model[position].productCuttedPrice.toFloat()) * 100)
        holder.productDiscount.text = discount.toInt().toString() + "% OFF"
        Glide.with(context).load(model[position].productImage).into(holder.miniProductImage)

        holder.dealsOfTheDay.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Deals of the day")
            builder.setMessage("Are you sure to add ?")

            builder.setPositiveButton("Yes") { dialog, which ->
                loadingDialog.show()
                val randomString = UUID.randomUUID().toString().substring(0, 15)
                val userData: MutableMap<Any, Any?> =
                    HashMap()
                userData["id"] = randomString
                userData["productId"] = model[position].id.toString()
                userData["timeStamp"] = System.currentTimeMillis()

                FirebaseFirestore.getInstance()
                    .collection("HOME")
                    .document("HomeData")
                    .collection("Deals_of_the_day")
                    .document(randomString)
                    .set(userData)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            loadingDialog.dismiss()
                            Toast.makeText(
                                context,
                                "Successfully added",
                                Toast.LENGTH_SHORT
                            ).show()
                            var intent = Intent(context, DealsOfTheDayActivity::class.java)
                            context.startActivity(intent)
                        }
                    }

            }
            builder.setNegativeButton("No") { dialog, which ->
            }

            builder.show()
        }

        holder.addBanner.setOnClickListener {
            AllProductsActivity.addBannerDialog.show()
            AllProductsActivity.miniProductTitle.text = model[position].productTitle
            AllProductsActivity.miniProductPrice.text = model[position].productPrice.toString()
            AllProductsActivity.miniProductCuttedPrice.text = model[position].productCuttedPrice.toString()
            AllProductsActivity.miniProductRatting.text = model[position].productRating
            AllProductsActivity.miniProductTotalRatting.text = model[position].productTotalRating
            AllProductsActivity.miniProductBrand.text = model[position].productBrandName
            AllProductsActivity.productId.text = model[position].id

            if (model[position].productPrice.toLong() >= 800) {
                AllProductsActivity.miniProductDelivery.text = "Free Delivery"
            } else {
                AllProductsActivity.miniProductDelivery.text = "80₹ Rupees Delivery Price"
            }
            var discount =
                100 - ((model[position].productPrice.toFloat() / model[position].productCuttedPrice.toFloat()) * 100)
            AllProductsActivity.productDiscount.text = discount.toInt().toString() + "% OFF"
            Glide.with(context).load(model[position].productImage).into(AllProductsActivity.miniProductImage)
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


        var miniProductTitle: TextView = itemView.findViewById<TextView?>(R.id.mini_product_title)
        var miniProductPrice: TextView = itemView.findViewById<TextView?>(R.id.mini_product_price)
        var miniProductRatting: TextView = itemView.findViewById<TextView?>(R.id.mini_product_ratting_text)
        var miniProductTotalRatting: TextView = itemView.findViewById<TextView?>(R.id.mini_product_total_ratting)
        var miniProductDelivery: TextView = itemView.findViewById<TextView?>(R.id.mini_product_delivery)
        var miniProductCuttedPrice: TextView = itemView.findViewById<TextView?>(R.id.mini_product_cutted_price)
        var miniProductBrand: TextView = itemView.findViewById<TextView?>(R.id.product_brand)
        var productDiscount: TextView = itemView.findViewById<TextView?>(R.id.discount_text)
        var selectQtyText: TextView = itemView.findViewById<TextView?>(R.id.select_qty_text)
        var avlQtyText: TextView = itemView.findViewById<TextView?>(R.id.avl_qty_text)
        var qtyId: TextView = itemView.findViewById<TextView?>(R.id.qty_id)
        var productId: TextView = itemView.findViewById<TextView?>(R.id.productId)

        var addBanner: AppCompatButton = itemView.findViewById<AppCompatButton?>(R.id.add_banner)
        var dealsOfTheDay: AppCompatButton = itemView.findViewById<AppCompatButton?>(R.id.dealsOfTheDay)
        var deleteProduct: LinearLayout = itemView.findViewById<LinearLayout?>(R.id.deleteProduct)

        var miniProductImage: ImageView = itemView.findViewById<ImageView?>(R.id.mini_product_image)
        var selectQuantity: LinearLayout = itemView.findViewById<LinearLayout?>(R.id.select_qty)

    }
}