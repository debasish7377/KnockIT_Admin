package com.example.knockitadminapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.knockitadminapp.Model.BranchPaymentModel
import com.example.knockitadminapp.Model.RiderPaymentModel
import com.example.knockitadminapp.Model.RiderModel
import com.example.knockitadminapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class BranchPaymentAdapter(var context: Context, var model: ArrayList<BranchPaymentModel>) :
    RecyclerView.Adapter<BranchPaymentAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View =
            LayoutInflater.from(context).inflate(R.layout.item_rider_payment, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.bankHolderName.text = model[position].bankHolderName
        holder.bankNumber.text = model[position].bankAccountNumber
        holder.ifscCode.text = model[position].ifscCode
        holder.payment.text = "₹"+model[position].totalAmount
        holder.bankName.text = model[position].bankName

        FirebaseFirestore.getInstance()
            .collection("BRANCHES")
            .document(model[position].branchId.toString())
            .addSnapshotListener { querySnapshot: DocumentSnapshot?, e: FirebaseFirestoreException? ->
                querySnapshot?.let {
                    val riderModel = it.toObject(RiderModel::class.java)
                    if (!riderModel?.profile.equals("")) {
                        Glide.with(context).load(riderModel?.profile.toString()).placeholder(R.drawable.avatara).into(holder.profile)
                    }else{
                        Glide.with(context).load(R.drawable.avatara).into(holder.profile)
                    }
                    holder.name.text = riderModel?.name.toString()
                    holder.email.text = riderModel?.email.toString()
                    holder.number.text = riderModel?.number.toString()

                }
            }

        holder.payBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Payment")
            builder.setMessage("Are you sure to pay this rider ?")

            builder.setPositiveButton("Yes") { dialog, which ->



                val userData2: MutableMap<String, Any?> = HashMap()
                userData2["payment"] = "true"
                FirebaseFirestore.getInstance()
                    .collection("BRANCHES")
                    .document(model[position].branchId)
                    .collection("PAYMENT")
                    .document(model[position].id)
                    .set(userData2)
                    .addOnCompleteListener {

                    }

                val randomString = UUID.randomUUID().toString().substring(0, 18)
                val userData3: MutableMap<String, Any?> =
                    HashMap()
                userData3["id"] = randomString
                userData3["title"] = "Payment"
                userData3["description"] =
                    "Your Payment Successfully credited to your account"
                userData3["payment"] = model[position].totalAmount.toString() + " Payment Successful"
                userData3["timeStamp"] = System.currentTimeMillis()
                userData3["read"] = "true"
                FirebaseFirestore.getInstance()
                    .collection("BRANCHES")
                    .document(model[position].branchId)
                    .collection("MY_NOTIFICATION")
                    .document(randomString)
                    .set(userData3)
                    .addOnCompleteListener {

                    }

                FirebaseFirestore.getInstance()
                    .collection("BRANCH_PAYMENT")
                    .document(model[position].id)
                    .delete()
                    .addOnCompleteListener {
                        Toast.makeText(
                            context,
                            "Payment Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        notifyDataSetChanged()
                        model.removeAt(position)
                        notifyDataSetChanged()
                        notifyItemRemoved(position)
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

        var name: TextView = itemView.findViewById(R.id.name)
        var email: TextView = itemView.findViewById(R.id.email)
        var number: TextView = itemView.findViewById(R.id.phone)
        var bankNumber: TextView = itemView.findViewById(R.id.accountNumber)
        var bankHolderName: TextView = itemView.findViewById(R.id.accountHolderName)
        var ifscCode: TextView = itemView.findViewById(R.id.ifscCode)
        var payment: TextView = itemView.findViewById(R.id.payment)
        var bankName: TextView = itemView.findViewById(R.id.bankName)
        var profile: CircleImageView = itemView.findViewById(R.id.imageView4)
        var payBtn: TextView = itemView.findViewById(R.id.pay)

    }
}