package com.technoship.resturant.fragments

import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import com.technoship.resturant.fragments.MainFragmentDirections
import com.technoship.resturant.R
import com.technoship.resturant.adapter.PopularAdapter
import com.technoship.resturant.model.FavoriteFood
import com.technoship.resturant.model.Food
import com.technoship.resturant.viewmodel.FoodViewModel

class FavoriteFragment : Fragment() {

    private lateinit var favoriteRecycler: RecyclerView
    private lateinit var emptyAnimation: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View = inflater.inflate(R.layout.fragment_favorite, container, false)
        favoriteRecycler = view.findViewById(R.id.favorite_recycler)
        favoriteRecycler.isNestedScrollingEnabled = false
        emptyAnimation = view.findViewById(R.id.favouriteFragment_emptyLottie)
        return view
    }

    private lateinit var adapter: PopularAdapter
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = (parentFragment as NavHostFragment).requireParentFragment().requireView().findNavController()
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)

        foodViewModel.getFoodsFavorite().observe(viewLifecycleOwner, {
            itemsSizeChanged(it.size)
            adapter.popularItems = it
            adapter.notifyDataSetChanged()
        })

        foodViewModel.readFavouriteHashMap().observeOnce(viewLifecycleOwner, {
            if (it != null){
                adapter.favoriteHashMap = FavoriteFood.convertToHashMap(it)
            }
        })

        adapter = PopularAdapter()
        adapter.onFoodItemClicked = object :
            PopularAdapter.OnPopularFoodItemClicked {
            override fun itemClicked(food: Food, favorite: Boolean, position: Int) {
                navController.navigate(
                    MainFragmentDirections.
                    actionMainFragmentToDetailsFragment(food, favorite)
                )
            }

            override fun favoriteClicked(
                food: Food,
                favorite: Boolean,
                position: Int
            ) {
                foodViewModel.setFavoriteFoods(FavoriteFood(food.id, favorite))
            }
        }
        favoriteRecycler.adapter = adapter
        favoriteRecycler.layoutManager =
            GridLayoutManager(requireContext(),2)
        favoriteRecycler.setHasFixedSize(true)
    }

    private fun itemsSizeChanged(size: Int) = when (size) {
        0 -> {
            emptyAnimation.visibility = View.VISIBLE
            favoriteRecycler.visibility = View.GONE
        }
        else -> {
            emptyAnimation.visibility = View.GONE
            favoriteRecycler.visibility = View.VISIBLE
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