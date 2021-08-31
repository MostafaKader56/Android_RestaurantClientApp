package com.technoship.resturant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.technoship.resturant.R
import com.technoship.resturant.model.Category

class CategoriesAdapter(val categories: List<Category>) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>(){

    lateinit var onCategoryClicked : OnCategoryClicked

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false))
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.ViewHolder, position: Int) {
        holder.image.setImageResource(categories[position].img)
        holder.title.text = categories[position].title
        holder.items.text = holder.itemView.context.getString(R.string.category_items, categories[position].items.toString())
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.category_image)
        val title: TextView = itemView.findViewById(R.id.category_title)
        val items: TextView = itemView.findViewById(R.id.category_items)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onCategoryClicked.itemClicked(categories[position], position)
                }
            }
        }
    }

    interface OnCategoryClicked {
        fun itemClicked(category: Category, position: Int)
    }
}