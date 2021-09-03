package com.technoship.resturant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.technoship.resturant.R
import com.technoship.resturant.model.FavoriteFood
import com.technoship.resturant.model.Food

class PopularAdapter : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    var popularItems: List<Food> = ArrayList()
    var favoriteHashMap: MutableMap<Int, Boolean> =FavoriteFood.convertToHashMap(ArrayList())
    lateinit var onFoodItemClicked: OnPopularFoodItemClicked

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.popular_item, parent, false))
    }

    override fun onBindViewHolder(holder: PopularAdapter.ViewHolder, position: Int) {
        holder.title.text = popularItems[position].title
        holder.favorite.isChecked = favoriteHashMap[popularItems[position].id] ?: false
        holder.newFood.isChecked = popularItems[position].newFood
        Glide
            .with(holder.itemView.context)
            .load(popularItems[position].img)
            .centerCrop()
            .placeholder(R.drawable.food_bg)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return popularItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.popular_image)
        val title: TextView = itemView.findViewById(R.id.popular_title)
        val favorite: CheckBox = itemView.findViewById(R.id.popular_favourite)
        val newFood: CheckBox = itemView.findViewById(R.id.popular_new)
        private val favouriteContainer : View = itemView.findViewById(R.id.popular_favourite_container)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFoodItemClicked.itemClicked(popularItems[position], favorite.isChecked, position)
                }
            }

            favouriteContainer.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    favorite.performClick()
                    onFoodItemClicked.favoriteClicked(popularItems[position], favorite.isChecked, position)
                }
            }
        }
    }

    interface OnPopularFoodItemClicked {
        fun itemClicked(food: Food, favorite: Boolean, position: Int)
        fun favoriteClicked(food: Food, favorite: Boolean, position: Int)
    }
}
