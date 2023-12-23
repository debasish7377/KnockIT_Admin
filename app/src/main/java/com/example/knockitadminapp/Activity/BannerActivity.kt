package com.example.knockitadminapp.Activity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Database.BannerDatabase
import com.example.knockitadminapp.Database.CategoryDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityAllProductsBinding
import com.example.knockitadminapp.databinding.ActivityBannerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class BannerActivity : AppCompatActivity() {
    lateinit var binding: ActivityBannerBinding

    lateinit var addBannerDialog: Dialog
    lateinit var loadingDialog: Dialog
    var updateProductImage: Boolean = false
    lateinit var productImagePath: Uri

    companion object {
        lateinit var categoryDialog: Dialog
        lateinit var subCategoryDialog: Dialog
        lateinit var bannerImage: ImageView
        lateinit var addImage: ImageView
        lateinit var okBtn: AppCompatButton
        lateinit var colorCode: EditText
        lateinit var discount: EditText
        lateinit var selectSubCategory: TextView
        lateinit var selectCategory: TextView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBannerBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

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
        addBannerDialog.setContentView(R.layout.dialog_add_discount_banner)
        addBannerDialog.setCancelable(true)
        addBannerDialog.window?.setBackgroundDrawable(getDrawable(R.drawable.btn_buy_now))
        addBannerDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        bannerImage = addBannerDialog.findViewById<ImageView?>(R.id.banner_image)
        addImage = addBannerDialog.findViewById<ImageView?>(R.id.addImage)
        okBtn = addBannerDialog.findViewById<AppCompatButton?>(R.id.okBtn)
        colorCode = addBannerDialog.findViewById<EditText?>(R.id.colorCode)
        selectSubCategory = addBannerDialog.findViewById<TextView?>(R.id.subcategory)
        selectCategory = addBannerDialog.findViewById<TextView?>(R.id.category)
        discount = addBannerDialog.findViewById<EditText?>(R.id.discount)
        ////////////////addBannerDialog dialog

        ////////////////category dialog
        categoryDialog = Dialog(this)
        categoryDialog.setContentView(R.layout.dialog_category)
        categoryDialog.setCancelable(false)
        categoryDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        var categoryRecyclerView: RecyclerView =
            categoryDialog.findViewById(R.id.categoryRecyclerView)!!
        CategoryDatabase.loadSelectCategoryByBanner(this, categoryRecyclerView)
        ////////////////category dialog

        ////////////////subCategory dialog
        subCategoryDialog = Dialog(this)
        subCategoryDialog.setContentView(R.layout.dialog_sub_category)
        subCategoryDialog.setCancelable(true)
        subCategoryDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        var subCategoryRecyclerView: RecyclerView =
            subCategoryDialog.findViewById(R.id.subCategoryRecyclerView)!!
        ////////////////subCategory dialog

        selectCategory.setOnClickListener {
            categoryDialog.show()
        }

        selectSubCategory.setOnClickListener {
            subCategoryDialog.show()
            Toast.makeText(
                this,
                "if sub category not available then first add sub category",
                Toast.LENGTH_LONG
            ).show()
            CategoryDatabase.loadSelectSubCategoryByBanner(
                this,
                subCategoryRecyclerView,
                selectCategory.text.toString()
            )
        }

        BannerDatabase.loadBanners(this, binding.bannerRecyclerView)

        binding.addBanner.setOnClickListener {
            addBannerDialog.show()
        }

        addImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        okBtn.setOnClickListener {
            if (!colorCode.text.toString().equals("")) {
                if (!selectCategory.text.toString().equals("")) {
                    if (!selectSubCategory.text.toString().equals("")) {
                        if (!discount.text.toString().equals("")) {

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

                                            val randomString =
                                                UUID.randomUUID().toString().substring(0, 15)
                                            val userData: MutableMap<Any, Any?> =
                                                HashMap()
                                            userData["id"] = randomString
                                            userData["bannerBackground"] = colorCode.text.toString()
                                            userData["discount"] = discount.text.toString().toLong()
                                            userData["bannerImage"] = bannerImage.toString()
                                            userData["productId"] = ""
                                            userData["subCategory"] =
                                                selectSubCategory.text.toString()
                                            userData["timeStamp"] =
                                                System.currentTimeMillis().toString()

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
                            Toast.makeText(this, "Enter Discount Percentage", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        loadingDialog.dismiss()
                        Toast.makeText(this, "Select Sub Category", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    loadingDialog.dismiss()
                    Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show()
                }
            } else {
                loadingDialog.dismiss()
                Toast.makeText(this, "Enter color code", Toast.LENGTH_SHORT).show()
            }

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