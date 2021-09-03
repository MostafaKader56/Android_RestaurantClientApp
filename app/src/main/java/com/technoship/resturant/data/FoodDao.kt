package com.technoship.resturant.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.technoship.resturant.model.FavoriteFood
import com.technoship.resturant.model.Food
import com.technoship.resturant.model.SearchedFood
import com.technoship.resturant.model.Thingy
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Update
    suspend fun updateFood(food: Food)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFood(food: Food)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun addFoods(foods: List<Food>)

    @Query("SELECT * FROM food_table WHERE visible ORDER BY `order` DESC")
    fun readAllFoods(): LiveData<List<Food>>

//    @Query("SELECT * FROM food_table WHERE id IN (SELECT foodId FROM FavoriteFood_table WHERE favorite)")
    @Query("SELECT * FROM food_table INNER JOIN FavoriteFood_table ON food_table.id = FavoriteFood_table.foodId AND FavoriteFood_table.favorite")
    fun readAllFavorite(): LiveData<List<Food>>

    @Query("SELECT * FROM food_table WHERE visible AND popular ORDER BY `order` DESC")
    fun readAllPopular(): LiveData<List<Food>>

    @Query("SELECT * FROM food_table WHERE visible AND dishes ORDER BY `order` DESC")
    fun readAllDishes(): LiveData<List<Food>>

//    @Query("SELECT * FROM food_table WHERE visible AND `popular`")
    @Query("SELECT * FROM food_table INNER JOIN SearchedFood_table ON food_table.id = SearchedFood_table.foodId AND SearchedFood_table.searched")
    fun readAllSearched(): LiveData<List<Food>>

    @Query("SELECT COUNT(*) FROM food_table WHERE visible AND category = :wantedCategory")
    fun readCategoryItems(wantedCategory: Int): LiveData<Int>

    @Query("DELETE FROM food_table")
    fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFavoriteFoods(value :FavoriteFood)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setSearchedFoods(value :SearchedFood)

    @Query("SELECT * FROM food_table WHERE title LIKE :searchQuery")
    fun searchForFoods(searchQuery: String): Flow<List<Food>>

    @Query("SELECT category, COUNT(category) as count FROM food_table GROUP BY category;")
    fun selectCategoriesItems(): LiveData<List<Thingy>>

    @Transaction
    suspend fun clearAndAddNewFoods(foods: List<Food>) {
        nukeTable()
        addFoods(foods)
    }

    @Query("SELECT * FROM FavoriteFood_table")
    fun readFavouriteHashMap(): LiveData<List<FavoriteFood>>

    @Query("SELECT * FROM SearchedFood_table")
    fun readSearchedHashMap(): LiveData<List<SearchedFood>>

    @Query("SELECT favorite FROM FavoriteFood_table WHERE foodId = :foodId")
    fun readIsFavouriteFood(foodId:Int): LiveData<Boolean>

    @Query("SELECT * FROM food_table WHERE visible AND category = :categoryKind ORDER BY `order` DESC")
    fun getCategoryFoods(categoryKind: Int): LiveData<List<Food>>

    /*
    var favorite: Boolean,
    val popular: Boolean,
    val new: Boolean,
    val dishes: Boolean,
    val searched: Boolean
     */

}