package com.example.knockitadminapp.Database

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.knockitadminapp.Adapter.BranchProductsAdapter
import com.example.knockitadminapp.Adapter.RidersVerificationAdapter
import com.example.knockitadminapp.Adapter.StoreAdapter
import com.example.knockitadminapp.Adapter.StoreVerificationAdapter
import com.example.knockitadminapp.Model.BranchModel
import com.example.knockitadminapp.Model.ProductModel
import com.example.knockitadminapp.Model.RiderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class StoreDatabase {

    companion object {
        @SuppressLint("SuspiciousIndentation")
        fun loadStore(context: Context, storeRecyclerView: RecyclerView, category: String) {
            var storeModel: ArrayList<BranchModel> = ArrayList<BranchModel>()
            var storeAdapter = StoreAdapter(context!!, storeModel)
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            storeRecyclerView.layoutManager = layoutManager
            storeRecyclerView.adapter = storeAdapter
            var stores: ArrayList<BranchModel> = ArrayList<BranchModel>()

                        FirebaseFirestore.getInstance()
                            .collection("BRANCHES")
                            .orderBy("timeStamp", Query.Direction.ASCENDING)
                            .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                                querySnapshot?.let {
                                    stores.clear()
                                    for (document in it) {
                                        val model = document.toObject(BranchModel::class.java)
                                        stores.add(model)

                                        storeModel.clear()
                                        for (p in stores) {
                                            if (!p.storeId.equals(FirebaseAuth.getInstance().uid.toString())) {
                                                if (category.equals(p.storeCategory)) {
                                                    storeModel.add(p)
                                                } else if (category.equals("All")) {
                                                    storeModel.add(p)
                                                }
                                            }

                                        }
                                        storeAdapter.notifyDataSetChanged()

                            }
                    }
                }

//            FirebaseFirestore.getInstance()
//                .collection("PRODUCTS")
//                .document(productId)
//                .collection("productSize")
//                .orderBy("timeStamp", Query.Direction.DESCENDING)
//                .get().addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
//                    for (snapshot in queryDocumentSnapshots) {
//                        val model: SelectQtyModel = snapshot.toObject(SelectQtyModel::class.java)
//                        qtyModel.add(model)
//                    }
//                    qtyAdapter.notifyDataSetChanged()
//                })
        }

        fun loadProduct(
            context: Context,
            productRecyclerView: RecyclerView,
            storeId: String
        ) {
            var productModel: ArrayList<ProductModel> = ArrayList<ProductModel>()
            var productAdapter = BranchProductsAdapter(context!!, productModel)
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            productRecyclerView.layoutManager = layoutManager
            productRecyclerView.adapter = productAdapter
            val products = ArrayList<ProductModel>()

            FirebaseFirestore.getInstance()
                .collection("PRODUCTS")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    querySnapshot?.let {
                        products.clear()
                        for (document in it) {
                            val model = document.toObject(ProductModel::class.java)
                            products.add(model)

                            productModel.clear()
                            for (p in products) {
                                if (p.storeId.equals(storeId)) {
                                    productModel.add(p)
                                }

                            }
                            productAdapter.notifyDataSetChanged()
                        }
                    }
                }
        }

        fun loadPendingStores(context: Context, storeRecyclerView: RecyclerView, category: String) {
            var storeModel: ArrayList<BranchModel> = ArrayList<BranchModel>()
            var storeAdapter = StoreVerificationAdapter(context!!, storeModel)
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            storeRecyclerView.layoutManager = layoutManager
            storeRecyclerView.adapter = storeAdapter
            var stores: ArrayList<BranchModel> = ArrayList<BranchModel>()

            FirebaseFirestore.getInstance()
                .collection("BRANCHES")
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    querySnapshot?.let {
                        stores.clear()
                        for (document in it) {
                            val model = document.toObject(BranchModel::class.java)
                            stores.add(model)

                            storeModel.clear()
                            for (p in stores) {
                                if (!p.storeId.equals(FirebaseAuth.getInstance().uid.toString())) {
                                    if (category.equals(p.storeCategory)) {
                                        storeModel.add(p)
                                    } else if (category.equals("All")) {
                                        if (!p.storeVerification.equals("Verified")){
                                            storeModel.add(p)
                                        }

                                    }
                                }

                            }
                            storeAdapter.notifyDataSetChanged()

                        }
                    }
                }
        }

    }
}