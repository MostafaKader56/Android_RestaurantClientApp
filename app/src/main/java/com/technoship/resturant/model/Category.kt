package com.technoship.resturant.model

import android.content.Context
import com.technoship.resturant.R
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val kind : Int,
    val img:Int,
    val title:String,
    var items:Int = 0
) : Parcelable {

    companion object {
        fun convertToHashMap(list: List<Category>, map:MutableMap<Int, Int>): List<Category> {
            for (i in list) {
                if (map[i.kind] != null) i.items = map[i.kind]!!
            }
            return list
        }

        fun getArray(context:Context):ArrayList<Category>{
            val categories:ArrayList<Category> = ArrayList()
            categories.add(Category(Food.SANDWICH, R.drawable.sandwich, context.getString(R.string.category_title_sandwich)))
            categories.add(Category(Food.BURGER, R.drawable.burger, context.getString(R.string.category_title_burgers)))
            categories.add(Category(Food.MEAL, R.drawable.meal, context.getString(R.string.category_title_meals)))
            categories.add(Category(Food.HEALTH_MEAL, R.drawable.health_meal, context.getString(R.string.category_title_healthMeal)))
            categories.add(Category(Food.WRAPS, R.drawable.wrap, context.getString(R.string.category_title_wraps)))
            categories.add(Category(Food.RIZO, R.drawable.rizo, context.getString(R.string.category_title_rizo)))
            categories.add(Category(Food.POTATOES, R.drawable.potatoes, context.getString(R.string.category_title_potatoes)))
            categories.add(Category(Food.SAUCE, R.drawable.sauce, context.getString(R.string.category_title_sauces)))
            categories.add(Category(Food.ADDITIONS, R.drawable.additions, context.getString(R.string.category_title_additions)))
            categories.add(Category(Food.STRIPS_MEAL, R.drawable.strips_meal, context.getString(R.string.category_title_stripsMeals)))
            categories.add(Category(Food.KIDS_MEAL, R.drawable.kids_meal, context.getString(R.string.category_title_kidsMeals)))
            return categories
        }
    }
}
