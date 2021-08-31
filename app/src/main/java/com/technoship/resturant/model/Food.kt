package com.technoship.resturant.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "food_table")
@Parcelize
data class Food(
    var category:Int,
    var order:Int,
    var title: String,
    var description: String,
    var popular: Boolean,
    var newFood: Boolean,
    var dishes: Boolean,
    var img:String,
    var priceKind: Int,
    var priceString: String,
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var visible:Boolean) : Parcelable {

    constructor(): this(NONE, 0, "", "", false, false, false, "", 0, "", 0, true)

    companion object {
        const val NONE:Int = 0
        const val SANDWICH:Int = 1
        const val BURGER:Int = 2
        const val MEAL:Int = 3
        const val HEALTH_MEAL:Int = 4
        const val WRAPS:Int = 5
        const val RIZO:Int = 6
        const val POTATOES:Int = 7
        const val SAUCE:Int = 8
        const val ADDITIONS:Int = 9
        const val STRIPS_MEAL:Int = 10
        const val KIDS_MEAL:Int = 11

        const val HEADER_NONE:Int = 0
        const val HEADER_WITHOUT:Int = 1
        const val HEADER_LARGE_SMALL:Int = 2
        const val HEADER_SINGLE_DOUBLE_TRIPLE:Int = 3
        const val FOOD_PRICE_STING_SEGMENTATION:String = "#"
    }
}
