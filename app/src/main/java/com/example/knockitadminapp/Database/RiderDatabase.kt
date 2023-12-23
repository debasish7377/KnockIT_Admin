package com.example.knockitadminapp.Database

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.knockitadminapp.Adapter.AllRidersAdapter
import com.example.knockitadminapp.Adapter.BranchProductsAdapter
import com.example.knockitadminapp.Adapter.RiderAdapter
import com.example.knockitadminapp.Adapter.RiderDeliveryProductsAdapter
import com.example.knockitadminapp.Adapter.RidersVerificationAdapter
import com.example.knockitadminapp.Adapter.UsersAdapter
import com.example.knockitadminapp.Model.BannerModel
import com.example.knockitadminapp.Model.BranchModel
import com.example.knockitadminapp.Model.ProductModel
import com.example.knockitadminapp.Model.RiderModel
import com.example.knockitadminapp.Model.UserModel
import com.example.knockitbranchapp.Model.MyOderModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class RiderDatabase {

    companion object {

        fun loadRider(context: Context, riderRecyclerView: RecyclerView) {
            var riderModel: ArrayList<RiderModel> = ArrayList<RiderModel>()
            var riderAdapter = RiderAdapter(context!!, riderModel)
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            riderRecyclerView.layoutManager = layoutManager
            riderRecyclerView.adapter = riderAdapter
            var riderItems: ArrayList<RiderModel> = ArrayList<RiderModel>()

            FirebaseFirestore.getInstance()
                .collection("RIDERS")
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    querySnapshot?.let {
                        riderItems.clear()
                        for (document in it) {
                            val model = document.toObject(RiderModel::class.java)
                            riderItems.add(model)

                            ////// Location wise rider Display
                            FirebaseFirestore.getInstance().collection("BRANCHES")
                                .document(FirebaseAuth.getInstance().uid.toString())
                                .get()
                                .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
                                    val model: BranchModel? =
                                        documentSnapshot.toObject(BranchModel::class.java)

                                    riderModel.clear()
                                    for (p in riderItems) {
                                        if (p.city.equals(model?.city)) {
                                            if (p.connectWithStore.equals("")) {
                                                riderModel.add(p)
                                            }
                                        }
                                    }
                                    riderAdapter.notifyDataSetChanged()
                                })
                            ////// Location wise rider Display
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

        fun loadAllRiders(context: Context, ridersRecyclerView: RecyclerView) {
            var ridersModel: ArrayList<RiderModel> = ArrayList<RiderModel>()
            var usersAdapter = AllRidersAdapter(context!!, ridersModel)
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            ridersRecyclerView.layoutManager = layoutManager
            ridersRecyclerView.adapter = usersAdapter

//            FirebaseFirestore.getInstance()
//                .collection("RIDERS")
//                .orderBy("timeStamp", Query.Direction.DESCENDING)
//                .get()
//                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
//                    for (snapshot in queryDocumentSnapshots) {
//                        val bannerModel1: RiderModel = snapshot.toObject(RiderModel::class.java)
//                        ridersModel.add(bannerModel1)
//                    }
//                    bannerAdapter.notifyDataSetChanged()
//                })

            FirebaseFirestore.getInstance()
                .collection("RIDERS")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    querySnapshot?.let {
                        ridersModel.clear()
                        for (document in it) {
                            val model = document.toObject(RiderModel::class.java)
                            //coupons.add(model)

                            ridersModel.add(model)
//                            for (p in coupons) {
//
//                                usersModel.add(p)
//
//                                usersAdapter.notifyDataSetChanged()
//
//                            }

                        }
                        usersAdapter.notifyDataSetChanged()
                    }

                }
        }

        fun loadPendingRiders(context: Context, ridersRecyclerView: RecyclerView) {
            var ridersModel: ArrayList<RiderModel> = ArrayList<RiderModel>()
            var usersAdapter = RidersVerificationAdapter(context!!, ridersModel)
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            ridersRecyclerView.layoutManager = layoutManager
            ridersRecyclerView.adapter = usersAdapter
            var riders: ArrayList<RiderModel> = ArrayList<RiderModel>()

//            FirebaseFirestore.getInstance()
//                .collection("RIDERS")
//                .orderBy("timeStamp", Query.Direction.DESCENDING)
//                .get()
//                .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
//                    for (snapshot in queryDocumentSnapshots) {
//                        val bannerModel1: RiderModel = snapshot.toObject(RiderModel::class.java)
//                        ridersModel.add(bannerModel1)
//                    }
//                    bannerAdapter.notifyDataSetChanged()
//                })

            FirebaseFirestore.getInstance()
                .collection("RIDERS")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    querySnapshot?.let {
                        ridersModel.clear()
                        for (document in it) {
                            val model = document.toObject(RiderModel::class.java)
                            riders.add(model)

                            for (p in riders) {

                                if (!p.driverAccount.equals("Verified")){
                                    ridersModel.add(p)
                                }
                            }
                            usersAdapter.notifyDataSetChanged()

                        }
                    }

                }
        }

        fun loadRiderDeliveryProduct(
            context: Context,
            productRecyclerView: RecyclerView,
            storeId: String
        ) {
            var productModel: ArrayList<MyOderModel> = ArrayList<MyOderModel>()
            var productAdapter = RiderDeliveryProductsAdapter(context!!, productModel)
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            productRecyclerView.layoutManager = layoutManager
            productRecyclerView.adapter = productAdapter
            val products = ArrayList<MyOderModel>()

            FirebaseFirestore.getInstance()
                .collection("ORDER")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    querySnapshot?.let {
                        products.clear()
                        for (document in it) {
                            val model = document.toObject(MyOderModel::class.java)
                            products.add(model)

                            productModel.clear()
                            for (p in products) {
                                if (p.riderId.equals(storeId)) {
                                    productModel.add(p)
                                }

                            }
                            productAdapter.notifyDataSetChanged()
                        }
                    }
                }
        }

    }
}