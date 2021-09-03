package com.technoship.resturant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteFood_table")
class FavoriteFood(
    @PrimaryKey(autoGenerate = true)
    var foodId :Int,
    var favorite: Boolean){

    companion object {
        fun convertToHashMap(list: List<FavoriteFood>): MutableMap<Int, Boolean> {
            val map: MutableMap<Int, Boolean> = HashMap()
            for (i in list) map[i.foodId] = i.favorite
            return map
        }
    }
}
