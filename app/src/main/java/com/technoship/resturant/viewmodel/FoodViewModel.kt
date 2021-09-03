package com.technoship.resturant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.technoship.resturant.data.RoomDB
import com.technoship.resturant.model.FavoriteFood
import com.technoship.resturant.model.Food
import com.technoship.resturant.model.SearchedFood
//import com.technoship.resturant.model.NotRefreshedData
import com.technoship.resturant.model.Thingy
import com.technoship.resturant.repo.FirestoreRepository
import com.technoship.resturant.repo.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodViewModel(application: Application) : AndroidViewModel(application) {

    private val foodRepository: FoodRepository
    private val firestoreRepository: FirestoreRepository

    init {
        val foodDao = RoomDB.getDatabase(application).foodDao()
        foodRepository = FoodRepository(foodDao)
        firestoreRepository = FirestoreRepository()
    }

//    fun addFoodToLocal(food: Food){
//        viewModelScope.launch(Dispatchers.IO) {
//            foodRepository.addFoodToLocal(food)
//        }
//    }

    fun addFoodsToLocal(foods: List<Food>){
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.addFoodsToLocal(foods)
        }
    }

//    fun removeLocalFoods(){
//        viewModelScope.launch(Dispatchers.IO) {
//            foodRepository.removeAllFoods()
//        }
//    }

    fun setFavoriteFoods(value :FavoriteFood){
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.setFavoriteFoods(value)
        }
    }

    fun setSearchedFoods(value:SearchedFood){
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.setSearchedFoods(value)
        }
    }

    fun getFoodsFavorite(): LiveData<List<Food>> {
        return foodRepository.readAllFavorite()
    }

//    fun getAllFoods(): LiveData<List<Food>> {
//        return foodRepository.readAllFood
//    }

    fun getFoodsPopular(): LiveData<List<Food>> {
        return foodRepository.readAllPopular
    }

    fun getFoodsDishes(): LiveData<List<Food>> {
        return foodRepository.readAllDishes
    }

    fun searchForFoods(searchQuery: String): LiveData<List<Food>> {
        return foodRepository.searchForFoods(searchQuery).asLiveData()
    }

    fun getSearchedFoods(): LiveData<List<Food>>  {
        return foodRepository.readAllSearched
    }

    fun getCategoriesItemsCount(): LiveData<List<Thingy>>  {
        return foodRepository.getCategoriesItemsCount()
    }

    interface OnFoodsLoadedFormFirestore{
        fun onSuccess(foods: List<Food>)
        fun onFail()
    }

    fun getFoodsFromFirestoreToRoomDB(onCallFinishedWithErrorOrWithout:OnCallFinishedWithErrorOrWithout){
        firestoreRepository.getFoodsFromFirestore(object: OnFoodsLoadedFormFirestore{
            override fun onSuccess(foods: List<Food>) {
                addFoodsToLocal(foods)
                onCallFinishedWithErrorOrWithout.callback()
            }

            override fun onFail() {
                onCallFinishedWithErrorOrWithout.callback()
            }
        })
    }

    fun readFavouriteHashMap(): LiveData<List<FavoriteFood>>  {
        return foodRepository.readFavouriteHashMap()
    }

    fun readSearchedHashMap(): LiveData<List<SearchedFood>>  {
        return foodRepository.readSearchedHashMap()
    }

    fun getFoodFavorite(id:Int): LiveData<Boolean> {
        return foodRepository.readIsFavouriteFood(id)
    }

    fun getCategoryFoods(categoryKind: Int): LiveData<List<Food>>{
        return foodRepository.getCategoryFoods(categoryKind)
    }

    interface OnCallFinishedWithErrorOrWithout{
        fun callback()
    }

}