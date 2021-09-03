package com.technoship.resturant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SearchedFood_table")
class SearchedFood(
    @PrimaryKey(autoGenerate = true)
    var foodId :Int,
    var searched: Boolean){

    companion object {
        fun convertToHashMap(list: List<SearchedFood>): MutableMap<Int, Boolean> {
            val map: MutableMap<Int, Boolean> = HashMap()
            for (i in list) map[i.foodId] = i.searched
            return map
        }
    }
}
