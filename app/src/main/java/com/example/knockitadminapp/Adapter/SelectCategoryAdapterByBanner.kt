package com.example.knockitbranchapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.knockitadminapp.Activity.BannerActivity
import com.example.knockitadminapp.Activity.CategoryActivity
import com.example.knockitadminapp.Model.CategoryModel
import com.example.knockitadminapp.R


class SelectCategoryAdapterByBanner(var context: Context, var model: List<CategoryModel>) :
    RecyclerView.Adapter<SelectCategoryAdapterByBanner.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.item_select_category, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.categoryTitle.text = model[position].categoryTitle

        holder.itemView.setOnClickListener {
            BannerActivity.selectCategory.text = model[position].categoryTitle.toString()
            BannerActivity.categoryDialog.dismiss()
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var categoryTitle: TextView = itemView.findViewById<TextView?>(R.id.category_title)

    }
}