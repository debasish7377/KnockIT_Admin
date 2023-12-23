package com.example.knockitadminapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.knockitadminapp.Activity.StoresActivity.Companion.storeRecyclerView
import com.example.knockitadminapp.Database.StoreDatabase
import com.example.knockitadminapp.Model.CategoryModel
import com.example.knockitadminapp.R


class SelectCategoryAdapterByStores(var context: Context, var model: List<CategoryModel>) :
    RecyclerView.Adapter<SelectCategoryAdapterByStores.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_store_category_list, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.categoryTitle.text = model[position].categoryTitle

        if (model[position].categoryTitle.equals("Foods")){
            holder.categoryTitle.text = "Restaurants"
        }

        holder.itemView.setOnClickListener {
            StoreDatabase.loadStore(context, storeRecyclerView, model[position].categoryTitle)
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var categoryTitle: TextView = itemView.findViewById<TextView?>(R.id.category_list_text)

    }
}