package com.technoship.resturant.model

class Thingy {
    var category = 0
    var count = 0

    companion object {
        fun convertToHashMap(list: List<Thingy>): MutableMap<Int, Int> {
            val map: MutableMap<Int, Int> = HashMap()
            for (i in list) map[i.category] = i.count
            return map
        }
    }
}