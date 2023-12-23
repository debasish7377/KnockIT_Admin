package com.example.knockitadminapp.Database

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.knockitadminapp.Adapter.CouponsAdapter
import com.example.knockitadminapp.Adapter.UsersAdapter
import com.example.knockitadminapp.Model.CouponsModel
import com.example.knockitadminapp.Model.UserModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class UsersDatabase {

    companion object {
        fun loadUsers(context: Context, usersRecyclerView: RecyclerView) {
            var usersModel: ArrayList<UserModel> = ArrayList<UserModel>()
            var usersAdapter = UsersAdapter(context!!, usersModel)
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            usersRecyclerView.layoutManager = layoutManager
            usersRecyclerView.adapter = usersAdapter
            var coupons: ArrayList<UserModel> = ArrayList<UserModel>()

            FirebaseFirestore.getInstance()
                .collection("USERS")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    querySnapshot?.let {
                        usersModel.clear()
                        for (document in it) {
                            val model = document.toObject(UserModel::class.java)
                            //coupons.add(model)

                            usersModel.add(model)
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
        }
    }
}