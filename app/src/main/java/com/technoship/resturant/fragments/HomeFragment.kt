package com.technoship.resturant.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.technoship.resturant.fragments.MainFragmentDirections
import com.technoship.resturant.R
import com.technoship.resturant.adapter.CategoriesAdapter
import com.technoship.resturant.adapter.PopularAdapter
import com.technoship.resturant.adapter.ViewPager2Adapter
import com.technoship.resturant.model.Category
import com.technoship.resturant.model.FavoriteFood
import com.technoship.resturant.model.Food
import com.technoship.resturant.model.Thingy
import com.technoship.resturant.viewmodel.FoodViewModel
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var categoryRecycler: RecyclerView
    private lateinit var popularRecycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        viewPager2 = view.findViewById(R.id.view_pager)
        categoryRecycler = view.findViewById(R.id.category_recycler)
        popularRecycler = view.findViewById(R.id.popular_recycler)
        popularRecycler.isNestedScrollingEnabled = false
        return view
    }

    private lateinit var foodViewModel:FoodViewModel
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = (parentFragment as NavHostFragment).requireParentFragment().requireView().findNavController()
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)

        foodViewModel.getFoodsDishes().observeOnce(viewLifecycleOwner, {
            if (it != null && it.isNotEmpty()){
                viewPager2Adapter.viewPagerItems = shuffleViewPagerItems(ArrayList(it))
                viewPager2Adapter.notifyDataSetChanged()
            }
        })

        foodViewModel.getFoodsPopular().observe(viewLifecycleOwner, {
            if (it != null && it.isNotEmpty()){
                popularAdapter.popularItems = it
                popularAdapter.notifyDataSetChanged()
            }
        })

        foodViewModel.readFavouriteHashMap().observe(viewLifecycleOwner, {
            if (it != null){
                popularAdapter.favoriteHashMap = FavoriteFood.convertToHashMap(it)
                popularAdapter.notifyDataSetChanged()
                viewPager2Adapter.favoriteHashMap = popularAdapter.favoriteHashMap
                viewPager2Adapter.notifyItemChanged(currentItem)
                viewPager2Adapter.notifyItemChanged(currentItem-1)
                viewPager2Adapter.notifyItemChanged(currentItem+1)
            }
        })

        val items:ArrayList<Food> = ArrayList()
        items.add(Food())

        viewPager2Adapter = ViewPager2Adapter()
        viewPager2Adapter.viewPagerItems = items
        viewPager2.adapter = viewPager2Adapter
        viewPager2Adapter.onFoodItemClicked = object : ViewPager2Adapter.OnDishesFoodItemClicked {
            override fun itemClicked(food: Food, favorite: Boolean, position: Int) {
                foodClicked(food, favorite)
            }

            override fun favoriteClicked(
                food: Food,
                favorite: Boolean,
                position: Int,
            ) {
                foodViewModel.setFavoriteFoods(FavoriteFood(food.id, favorite))
            }
        }
        currentItem = Integer.MAX_VALUE / 2
        viewPager2.setCurrentItem(currentItem, false)
        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentItem = position
            }
        })

        categoriesAdapter = CategoriesAdapter(Category.getArray(requireContext()))
        categoryRecycler.adapter = categoriesAdapter
        categoriesAdapter.onCategoryClicked = object : CategoriesAdapter.OnCategoryClicked{
            override fun itemClicked(category: Category, position: Int) {
                navController.navigate(MainFragmentDirections.actionMainFragmentToCategoryFragment(category))
            }
        }
        foodViewModel.getCategoriesItemsCount().observeOnce(viewLifecycleOwner, {
            Category.convertToHashMap(categoriesAdapter.categories, Thingy.convertToHashMap(it))
            categoriesAdapter.notifyDataSetChanged()
        })
        categoryRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        categoryRecycler.setHasFixedSize(true)

        popularAdapter = PopularAdapter()
        popularRecycler.adapter = popularAdapter
        popularAdapter.favoriteHashMap = FavoriteFood.convertToHashMap(ArrayList())
        popularAdapter.onFoodItemClicked = object : PopularAdapter.OnPopularFoodItemClicked {
            override fun itemClicked(food: Food, favorite: Boolean, position: Int) {
                foodClicked(food, favorite)
            }

            override fun favoriteClicked(
                food: Food,
                favorite: Boolean,
                position: Int,
            ) {
                foodViewModel.setFavoriteFoods(FavoriteFood(food.id, favorite))
            }
        }
        popularRecycler.layoutManager = GridLayoutManager(requireContext(),2)
        popularRecycler.setHasFixedSize(true)
    }

    private fun foodClicked(food: Food, favorite: Boolean) {
        navController.navigate(
            MainFragmentDirections
            .actionMainFragmentToDetailsFragment(food, favorite)
        )
    }

    private fun shuffleViewPagerItems(list1: List<Food>) : ArrayList<Food> {
        val list = ArrayList(list1)
        for (i in 0 until list.size - 1) {
            val j = i + Random.nextInt(list.size - i)
            val temp = list[i]
            list[i] = list[j]
            list[j] = temp
        }
        return list
    }

    private var currentItem: Int = 0
    private lateinit var viewPager2Adapter: ViewPager2Adapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var popularAdapter : PopularAdapter
    val delay:Long = 4500
    private val mainHandler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            viewPager2.setCurrentItem(currentItem+1,true)
            mainHandler.postDelayed(this, delay)
        }
    }

    private fun <T>LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>){
        observe(lifecycleOwner, object : Observer<T>{
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }

        })
    }

    override fun onStop() {
        super.onStop()
        mainHandler.removeCallbacks(runnable)
    }

    override fun onStart() {
        super.onStart()
        mainHandler.postDelayed(runnable, delay)
    }
}