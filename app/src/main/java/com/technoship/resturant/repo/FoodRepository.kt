package com.technoship.resturant.repo

import androidx.lifecycle.LiveData
import com.technoship.resturant.data.FoodDao
import com.technoship.resturant.model.FavoriteFood
import com.technoship.resturant.model.Food
import com.technoship.resturant.model.SearchedFood
//import com.technoship.resturant.model.NotRefreshedData
import com.technoship.resturant.model.Thingy
import kotlinx.coroutines.flow.Flow

class FoodRepository(private val foodDao: FoodDao) {
    val readAllDishes: LiveData<List<Food>> = foodDao.readAllDishes()
    val readAllPopular: LiveData<List<Food>> = foodDao.readAllPopular()
    val readAllFood: LiveData<List<Food>> = foodDao.readAllFoods()
    val readAllSearched: LiveData<List<Food>> = foodDao.readAllSearched()

    fun readAllFavorite():LiveData<List<Food>> {
        return foodDao.readAllFavorite()
    }

    suspend fun addFoodToLocal(food: Food){
        foodDao.addFood(food)
    }

    suspend fun addFoodsToLocal(foods: List<Food>){
        foodDao.clearAndAddNewFoods(foods)
    }

    fun removeAllFoods(){
        foodDao.nukeTable()
    }

    fun setFavoriteFoods(value: FavoriteFood) {
        foodDao.setFavoriteFoods(value)
    }

    fun setSearchedFoods(value: SearchedFood) {
        foodDao.setSearchedFoods(value)
    }


    fun searchForFoods(searchQuery: String): Flow<List<Food>> {
        return foodDao.searchForFoods(searchQuery)
    }

    fun getCategoriesItemsCount(): LiveData<List<Thingy>> {
        return foodDao.selectCategoriesItems()
    }

    fun readFavouriteHashMap(): LiveData<List<FavoriteFood>> {
        return foodDao.readFavouriteHashMap()
    }

    fun readSearchedHashMap(): LiveData<List<SearchedFood>> {
        return foodDao.readSearchedHashMap()
    }

    fun readIsFavouriteFood(id: Int): LiveData<Boolean> {
        return foodDao.readIsFavouriteFood(id)
    }

    fun getCategoryFoods(categoryKind: Int): LiveData<List<Food>> {
        return foodDao.getCategoryFoods(categoryKind)
    }
}