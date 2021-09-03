package com.technoship.resturant.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.technoship.resturant.R
import com.technoship.resturant.adapter.CategoriesAdapter
import com.technoship.resturant.adapter.PopularAdapter
import com.technoship.resturant.model.Category
import com.technoship.resturant.model.FavoriteFood
import com.technoship.resturant.model.Food
import com.technoship.resturant.model.Thingy
import com.technoship.resturant.viewmodel.FoodViewModel

class CategoryFragment : Fragment() {
    private lateinit var passeCategory: Category

    private val args: CategoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.let {
            passeCategory = it.category
        }
    }

    private lateinit var categoryRecycler: RecyclerView
    private lateinit var itemsRecycler: RecyclerView
    private lateinit var categoryTitle: TextView
    private lateinit var backArrow: View
    private lateinit var emptyAnimation: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        categoryRecycler = view.findViewById(R.id.category_recycler)
        itemsRecycler = view.findViewById(R.id.items_recycler)
        itemsRecycler.isNestedScrollingEnabled = false
        categoryTitle = view.findViewById(R.id.currentCategoryTitle)
        backArrow = view.findViewById(R.id.backArrow)
        emptyAnimation = view.findViewById(R.id.categoryFragment_emptyLottie)
        return view
    }

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var itemsAdapter : PopularAdapter
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)

        categoryTitle.text = passeCategory.title

        foodViewModel.getCategoryFoods(passeCategory.kind).observeOnce(viewLifecycleOwner, {
            itemsSizeChanged(it.size)
            itemsAdapter.popularItems = it
            itemsAdapter.notifyDataSetChanged()
        })

        categoriesAdapter = CategoriesAdapter(Category.getArray(requireContext()))
        categoryRecycler.adapter = categoriesAdapter
        categoriesAdapter.onCategoryClicked = object : CategoriesAdapter.OnCategoryClicked{
            override fun itemClicked(category: Category, position: Int) {
                passeCategory = category
                foodViewModel.getCategoryFoods(category.kind)
                categoryTitle.text = category.title
                foodViewModel.getCategoryFoods(category.kind).observeOnce(viewLifecycleOwner, {
                    itemsSizeChanged(it.size)
                    itemsAdapter.popularItems = it
                    itemsAdapter.notifyDataSetChanged()
                })
            }
        }
        foodViewModel.getCategoriesItemsCount().observeOnce(viewLifecycleOwner, {
            Category.convertToHashMap(categoriesAdapter.categories, Thingy.convertToHashMap(it))
            categoriesAdapter.notifyDataSetChanged()
        })
        categoryRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        categoryRecycler.setHasFixedSize(true)

        itemsAdapter = PopularAdapter()
        itemsRecycler.adapter = itemsAdapter
        itemsAdapter.favoriteHashMap = FavoriteFood.convertToHashMap(ArrayList())
        itemsAdapter.onFoodItemClicked = object : PopularAdapter.OnPopularFoodItemClicked {
            override fun itemClicked(food: Food, favorite: Boolean, position: Int) {
                navController.navigate(
                    CategoryFragmentDirections.actionCategoryFragmentToDetailsFragment(food, favorite)
                )
            }

            override fun favoriteClicked(
                food: Food,
                favorite: Boolean,
                position: Int,
            ) {
                foodViewModel.setFavoriteFoods(FavoriteFood(food.id, favorite))
            }
        }
        itemsRecycler.layoutManager = GridLayoutManager(requireContext(),2)
        itemsRecycler.setHasFixedSize(true)

        foodViewModel.readFavouriteHashMap().observe(viewLifecycleOwner, {
            if (it != null){
                itemsAdapter.favoriteHashMap = FavoriteFood.convertToHashMap(it)
                itemsAdapter.notifyDataSetChanged()
            }
        })

        backArrow.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun itemsSizeChanged(size: Int) = when (size) {
        0 -> {
            emptyAnimation.visibility = View.VISIBLE
            itemsRecycler.visibility = View.GONE
        }
        else -> {
            itemsRecycler.visibility = View.VISIBLE
            emptyAnimation.visibility = View.GONE
        }
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>){
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }

        })
    }
}