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

class ViewPager2Adapter : RecyclerView.Adapter<ViewPager2Adapter.ViewHolder>() {


    var viewPagerItems: ArrayList<Food> = ArrayList()
    var favoriteHashMap: MutableMap<Int, Boolean> = FavoriteFood.convertToHashMap(ArrayList())
    lateinit var onFoodItemClicked: OnDishesFoodItemClicked

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.viewpager_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position1: Int) {
        val position = position1 % viewPagerItems.size
        holder.title.text = viewPagerItems[position].title
        holder.favorite.isChecked = favoriteHashMap[viewPagerItems[position].id] ?: false
        holder.newFood.isChecked = viewPagerItems[position].newFood
        Glide
            .with(holder.itemView.context)
            .load(viewPagerItems[position].img)
            .centerCrop()
            .placeholder(R.drawable.food_bg)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.viewpager_image)
        val title: TextView = itemView.findViewById(R.id.viewpager_title)
        val favorite: CheckBox = itemView.findViewById(R.id.viewpager_favourite)
        val newFood: CheckBox = itemView.findViewById(R.id.viewpager_new)
        private val favouriteContainer : View = itemView.findViewById(R.id.viewpager_favourite_container)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFoodItemClicked.itemClicked(viewPagerItems[position % viewPagerItems.size], favorite.isChecked, position)
                }
            }

            favouriteContainer.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    favorite.performClick()
                    onFoodItemClicked.favoriteClicked(viewPagerItems[position % viewPagerItems.size], favorite.isChecked, position)
                }
            }
        }
    }

    interface OnDishesFoodItemClicked {
        fun itemClicked(food: Food, favorite: Boolean, position: Int)
        fun favoriteClicked(food: Food, favorite: Boolean, position: Int)
    }
}
