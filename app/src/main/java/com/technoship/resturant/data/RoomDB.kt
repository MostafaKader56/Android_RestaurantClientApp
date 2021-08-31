package com.technoship.resturant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.technoship.resturant.model.FavoriteFood
import com.technoship.resturant.model.Food
import com.technoship.resturant.model.SearchedFood

@Database(entities = [Food::class, FavoriteFood::class, SearchedFood::class], version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase(){

    abstract fun foodDao() : FoodDao

    companion object{
        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getDatabase(context: Context): RoomDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "RoomDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
