package com.example.knockitadminapp.Database

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.knockitadminapp.Adapter.BranchPaymentAdapter
import com.example.knockitadminapp.Adapter.RiderPaymentAdapter
import com.example.knockitadminapp.Model.BranchPaymentModel
import com.example.knockitadminapp.Model.RiderPaymentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class PaymentDatabase {

    companion object {
        fun loadRiderPayment(context: Context, riderPaymentRecyclerView: RecyclerView) {
            var ridersModel: ArrayList<RiderPaymentModel> = ArrayList<RiderPaymentModel>()
            var ridersAdapter = RiderPaymentAdapter(context!!, ridersModel)
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            riderPaymentRecyclerView.layoutManager = layoutManager
            riderPaymentRecyclerView.adapter = ridersAdapter
            var coupons: ArrayList<RiderPaymentModel> = ArrayList<RiderPaymentModel>()

            FirebaseFirestore.getInstance()
                .collection("RIDER_PAYMENT")
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    querySnapshot?.let {
                        coupons.clear()
                        ridersModel.clear()
                        for (document in it) {
                            val model = document.toObject(RiderPaymentModel::class.java)
                            coupons.add(model)

                            ridersModel.clear()
                            for (p in coupons) {

                                ridersModel.add(p)

                            }
                            ridersAdapter.notifyDataSetChanged()

                        }

                    }
                }
        }

        fun loadBranchPayment(context: Context, branchPaymentRecyclerView: RecyclerView) {
            var branchModel: ArrayList<BranchPaymentModel> = ArrayList<BranchPaymentModel>()
            var branchAdapter = BranchPaymentAdapter(context!!, branchModel)
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            branchPaymentRecyclerView.layoutManager = layoutManager
            branchPaymentRecyclerView.adapter = branchAdapter
            var coupons: ArrayList<BranchPaymentModel> = ArrayList<BranchPaymentModel>()

            FirebaseFirestore.getInstance()
                .collection("BRANCH_PAYMENT")
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {

                        val model = document.toObject(BranchPaymentModel::class.java)
                        coupons.add(model)

                        branchModel.add(model)
                        for (p in coupons) {
                            if (!p.branchId.equals(FirebaseAuth.getInstance().uid)) {
                                //branchModel.add(p)
                            }

                        }
                        branchAdapter.notifyDataSetChanged()

                    }

                }
        }
    }
}