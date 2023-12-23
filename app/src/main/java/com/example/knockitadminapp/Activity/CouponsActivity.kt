package com.example.knockitadminapp.Activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.knockitadminapp.Database.CouponsDatabase
import com.example.knockitadminapp.R
import com.example.knockitadminapp.databinding.ActivityBannerBinding
import com.example.knockitadminapp.databinding.ActivityCouponsBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.security.auth.Subject

class CouponsActivity : AppCompatActivity() {

    lateinit var binding: ActivityCouponsBinding
    lateinit var couponDialog: Dialog
    lateinit var selectCouponsTypeDialog: Dialog
    lateinit var loadingDialog: Dialog
    lateinit var selectCouponsType: TextView
    lateinit var price: EditText
    lateinit var productAbovePrice: EditText
    lateinit var validDate: EditText
    lateinit var subject: EditText
    lateinit var okBtn: AppCompatButton
    lateinit var discount: TextView
    lateinit var flatOff: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCouponsBinding.inflate(layoutInflater)
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
        couponDialog = Dialog(this)
        couponDialog.setContentView(R.layout.dialog_add_coupons)
        couponDialog.setCancelable(true)
        couponDialog.window?.setBackgroundDrawable(getDrawable(R.drawable.btn_buy_now))
        couponDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        selectCouponsType = couponDialog.findViewById<TextView?>(R.id.selectCouponsType)
        price = couponDialog.findViewById<EditText?>(R.id.price)
        productAbovePrice = couponDialog.findViewById<EditText?>(R.id.productAbovePrice)
        validDate =
            couponDialog.findViewById<EditText?>(R.id.validTime)
        subject = couponDialog.findViewById<EditText?>(R.id.subject)
        okBtn =
            couponDialog.findViewById<AppCompatButton?>(R.id.okBtn)
        ////////////////addBannerDialog dialog

        ////////////////addBannerDialog dialog
        selectCouponsTypeDialog = Dialog(this)
        selectCouponsTypeDialog.setContentView(R.layout.dialog_coupon_type)
        selectCouponsTypeDialog.setCancelable(true)
        selectCouponsTypeDialog.window?.setBackgroundDrawable(getDrawable(R.drawable.btn_buy_now))
        selectCouponsTypeDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        flatOff = selectCouponsTypeDialog.findViewById<TextView?>(R.id.flatOff)
        discount = selectCouponsTypeDialog.findViewById<TextView?>(R.id.discount)
        ////////////////addBannerDialog dialog

        binding.addCoupons.setOnClickListener {
            couponDialog.show()
        }

        selectCouponsType.setOnClickListener {
            selectCouponsTypeDialog.show()
        }
        flatOff.setOnClickListener {
            selectCouponsType.text = "Flat Off"
            selectCouponsTypeDialog.dismiss()
            price.hint = "Flat off price"
        }
        discount.setOnClickListener {
            selectCouponsType.text = "Discount"
            selectCouponsTypeDialog.dismiss()
            price.hint = "Discount Price"
        }

        var validDt = System.currentTimeMillis() + 155520000
        val sdf = SimpleDateFormat("dd/MM/yy hh:mm a")
        val netDate = Date(validDt)
        val date = sdf.format(netDate)
        validDate.text = Editable.Factory.getInstance().newEditable(date)

        okBtn.setOnClickListener {
            if (!selectCouponsType.text.equals("")){
                if (!price.text.equals("")){
                    if (!productAbovePrice.text.equals("")){
                        if (!validDate.text.equals("")){
                            if (!subject.text.equals("")){
                                loadingDialog.show()
                                couponDialog.dismiss()

                                val dateTime = validDate.text.toString()

                                val simpleDateFormat = SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault())
                                val date = simpleDateFormat.parse(dateTime)
                                val timestamp = date?.time

                                val randomString = UUID.randomUUID().toString().substring(0, 15)
                                val userData: MutableMap<Any, Any?> =
                                    HashMap()
                                userData["id"] = randomString
                                userData["price"] = price.text.toString().toInt()
                                userData["productAbovePrice"] = productAbovePrice.text.toString().toInt()
                                userData["subject"] = subject.text.toString()
                                userData["timeStamp"] = System.currentTimeMillis()
                                userData["title"] = selectCouponsType.text.toString()
                                userData["validTime"] = timestamp

                                FirebaseFirestore.getInstance()
                                    .collection("Coupons")
                                    .document(randomString)
                                    .set(userData)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            loadingDialog.dismiss()
                                            Toast.makeText(
                                                this,
                                                "Successfully coupon added",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                            }else{
                                Toast.makeText(this, "Enter details", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this, "Enter valid date", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, "Enter product above price", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Enter price", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Select coupon type", Toast.LENGTH_SHORT).show()
            }

        }

        CouponsDatabase.loadCoupons(this, binding.couponsRecyclerView)
    }
}