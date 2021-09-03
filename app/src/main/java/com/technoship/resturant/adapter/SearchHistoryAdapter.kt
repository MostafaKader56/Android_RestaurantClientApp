package com.technoship.resturant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.technoship.resturant.R
import com.technoship.resturant.model.Food
import de.hdodenhof.circleimageview.CircleImageView

class SearchHistoryAdapter : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    lateinit var searchItems: ArrayList<Food>
    lateinit var onFoodItemClicked: OnSearchFoodItemClicked


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: SearchHistoryAdapter.ViewHolder, position: Int) {
        holder.image.setImageResource(R.drawable.test_food)
        holder.title.text = searchItems[position].title
        Glide
            .with(holder.itemView.context)
            .load(searchItems[position].img)
            .centerCrop()
            .placeholder(R.drawable.food_bg)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return searchItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: CircleImageView = itemView.findViewById(R.id.search_item_image)
        val title: TextView = itemView.findViewById(R.id.search_item_title)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFoodItemClicked.itemClicked(searchItems[position], position)
                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFoodItemClicked.itemLongClicked(searchItems[position], position, title)
                }
                return@setOnLongClickListener true
            }
        }
    }

    interface OnSearchFoodItemClicked {
        fun itemClicked(food: Food, position: Int)
        fun itemLongClicked(food: Food, position: Int, view: View)
    }
}
