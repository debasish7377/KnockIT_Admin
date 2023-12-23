package com.example.knockitadminapp.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Database.ProductDatabase
import com.example.knockitadminapp.Model.ProductModel
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityAllProductsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.UUID

class AllProductsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllProductsBinding

    var orderByDialog: Dialog? = null
    lateinit var productModel: ArrayList<ProductModel>
    lateinit var servicesNotAvailableDialog: Dialog
    var updateProductImage: Boolean = false
    lateinit var productImagePath: Uri
    lateinit var loadingDialog: Dialog

    companion object {
        lateinit var addBannerDialog: Dialog
        lateinit var miniProductTitle: TextView
        lateinit var miniProductPrice: TextView
        lateinit var miniProductRatting: TextView
        lateinit var miniProductTotalRatting: TextView
        lateinit var miniProductDelivery: TextView
        lateinit var miniProductCuttedPrice: TextView
        lateinit var miniProductBrand: TextView
        lateinit var productDiscount: TextView
        lateinit var selectQtyText: TextView
        lateinit var avlQtyText: TextView
        lateinit var qtyId: TextView
        lateinit var productId: TextView
        lateinit var addBanner: TextView
        lateinit var miniProductImage: ImageView
        lateinit var selectQuantity: LinearLayout
        lateinit var bannerImage: ImageView
        lateinit var addImage: ImageView
        lateinit var okBtn: AppCompatButton
        lateinit var colorCode: EditText
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllProductsBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)
        productModel = ArrayList()
        var subCategoryTitle: String = intent?.getStringExtra("subCategoryName").toString()

        ////////////////Services Not Available Dialog
        servicesNotAvailableDialog = Dialog(this)
        servicesNotAvailableDialog.setContentView(R.layout.dialog_services_not_available)
        servicesNotAvailableDialog.setCancelable(false)
        servicesNotAvailableDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        ////////////////Services Not Available Dialog

        ////////////////loading dialog
        orderByDialog = Dialog(this)
        orderByDialog?.setContentView(R.layout.dialog_oder_by)
        orderByDialog?.setCancelable(true)
        orderByDialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        var oderByPriceLowToHigh: TextView =
            orderByDialog?.findViewById(R.id.oder_by_price_low_high)!!
        var oderByPriceHighToLow: TextView =
            orderByDialog?.findViewById(R.id.oder_by_price_high_low)!!
        var oderByRattingHighToLow: TextView =
            orderByDialog?.findViewById(R.id.oder_by_rating_high_low)!!
        var oderByRattingLowToHigh: TextView =
            orderByDialog?.findViewById(R.id.oder_by_rating_low_high)!!
        ////////////////loading dialog

        ////////////////loading dialog
        loadingDialog = Dialog(this)
        loadingDialog?.setContentView(R.layout.dialog_loading)
        loadingDialog?.setCancelable(true)
        loadingDialog.window?.setBackgroundDrawable(getDrawable(R.drawable.btn_buy_now))
        loadingDialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        ////////////////loading dialog

        ////////////////addBannerDialog dialog
        addBannerDialog = Dialog(this)
        addBannerDialog.setContentView(R.layout.dialog_add_banner)
        addBannerDialog.setCancelable(true)
        addBannerDialog.window?.setBackgroundDrawable(getDrawable(R.drawable.btn_buy_now))
        addBannerDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        miniProductTitle = addBannerDialog.findViewById<TextView?>(R.id.mini_product_title)
        miniProductPrice = addBannerDialog.findViewById<TextView?>(R.id.mini_product_price)
        miniProductRatting = addBannerDialog.findViewById<TextView?>(R.id.mini_product_ratting_text)
        miniProductTotalRatting =
            addBannerDialog.findViewById<TextView?>(R.id.mini_product_total_ratting)
        miniProductDelivery = addBannerDialog.findViewById<TextView?>(R.id.mini_product_delivery)
        miniProductCuttedPrice =
            addBannerDialog.findViewById<TextView?>(R.id.mini_product_cutted_price)
        miniProductBrand = addBannerDialog.findViewById<TextView?>(R.id.product_brand)
        productDiscount = addBannerDialog.findViewById<TextView?>(R.id.discount_text)
        selectQtyText = addBannerDialog.findViewById<TextView?>(R.id.select_qty_text)
        avlQtyText = addBannerDialog.findViewById<TextView?>(R.id.avl_qty_text)
        qtyId = addBannerDialog.findViewById<TextView?>(R.id.qty_id)
        productId = addBannerDialog.findViewById<TextView?>(R.id.productId)
        miniProductImage = addBannerDialog.findViewById<ImageView?>(R.id.mini_product_image)
        selectQuantity = addBannerDialog.findViewById<LinearLayout?>(R.id.select_qty)

        bannerImage = addBannerDialog.findViewById<ImageView?>(R.id.banner_image)
        addImage = addBannerDialog.findViewById<ImageView?>(R.id.addImage)
        okBtn = addBannerDialog.findViewById<AppCompatButton?>(R.id.okBtn)
        colorCode = addBannerDialog.findViewById<EditText?>(R.id.colorCode)
        ////////////////addBannerDialog dialog

        addImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        okBtn.setOnClickListener {
            if (!colorCode.text.toString().equals("")) {
                if (updateProductImage) {
                    loadingDialog.show()
                    addBannerDialog.dismiss()

                    var reference: StorageReference =
                        FirebaseStorage.getInstance().getReference()
                            .child("bannerImages").child(
                                System.currentTimeMillis()
                                    .toString()
                            );
                    reference.putFile(productImagePath)
                        .addOnCompleteListener {
                            reference.downloadUrl.addOnSuccessListener { bannerImage ->

                                val randomString = UUID.randomUUID().toString().substring(0, 15)
                                val userData: MutableMap<Any, Any?> =
                                    HashMap()
                                userData["id"] = randomString
                                userData["bannerBackground"] = colorCode.text.toString()
                                userData["discount"] = "0".toInt()
                                userData["bannerImage"] = bannerImage.toString()
                                userData["productId"] = productId.text.toString()
                                userData["subCategory"] = ""
                                userData["timeStamp"] = System.currentTimeMillis().toString()

                                FirebaseFirestore.getInstance()
                                    .collection("HOME")
                                    .document("HomeData")
                                    .collection("Banner")
                                    .document(randomString)
                                    .set(userData)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            loadingDialog.dismiss()
                                            Toast.makeText(
                                                this,
                                                "Successfully banner added",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                        }
                } else {
                    loadingDialog.dismiss()
                    Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
                }
            } else {
                loadingDialog.dismiss()
                Toast.makeText(this, "Enter color code", Toast.LENGTH_SHORT).show()
            }

        }

        ProductDatabase.loadAllProduct(
            this,
            binding.productRecyclerView,
            servicesNotAvailableDialog
        )

        binding.oderByImage.setOnClickListener {
            orderByDialog?.show()
        }

        oderByPriceLowToHigh.setOnClickListener {
            ProductDatabase.loadProductWithOderByPriceLowToHigh(
                this,
                binding.productRecyclerView
            )
            orderByDialog?.dismiss()
        }
        oderByPriceHighToLow.setOnClickListener {
            ProductDatabase.loadProductWithOderByPriceHighToLow(
                this,
                binding.productRecyclerView
            )
            orderByDialog?.dismiss()
        }

        oderByRattingHighToLow.setOnClickListener {
            ProductDatabase.loadProductWithOderByRattingHighToLow(
                this,
                binding.productRecyclerView
            )
            orderByDialog?.dismiss()
        }
        oderByRattingLowToHigh.setOnClickListener {
            ProductDatabase.loadProductWithOderByRattingLowToHigh(
                this,
                binding.productRecyclerView
            )
            orderByDialog?.dismiss()
        }

        binding.searchView.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            updateProductImage = true
            productImagePath = data?.data!!
            Glide.with(this).load(productImagePath).into(bannerImage)

        }
    }
}