package com.technoship.resturant.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technoship.resturant.fragments.MainFragmentDirections
import com.technoship.resturant.R
import com.technoship.resturant.adapter.SearchHistoryAdapter
import com.technoship.resturant.model.Food
import com.technoship.resturant.model.SearchedFood
import com.technoship.resturant.viewmodel.FoodViewModel

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editText: EditText
    private lateinit var historyTip: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view:View = inflater.inflate(R.layout.fragment_search, container, false)
        editText = view.findViewById(R.id.search_edit)
        historyTip = view.findViewById(R.id.historyTip)
        recyclerView = view.findViewById(R.id.search_recycler)
        recyclerView.isNestedScrollingEnabled = false
        return view
    }

    private lateinit var adapter: SearchHistoryAdapter
    private lateinit var foodViewModel: FoodViewModel
    private var firstOpen: Boolean = true
    private lateinit var navController: NavController
    private lateinit var navController2: NavController

    private var searchedHashMap: MutableMap<Int, Boolean> = SearchedFood.convertToHashMap(ArrayList())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = (parentFragment as NavHostFragment).requireParentFragment().requireView().findNavController()
        navController2 = Navigation.findNavController(view)

        adapter = SearchHistoryAdapter()
        adapter.onFoodItemClicked = object : SearchHistoryAdapter.OnSearchFoodItemClicked {
            override fun itemClicked(food: Food, position: Int) {
                if (searchedHashMap[food.id] != true){
                    foodViewModel.setSearchedFoods(SearchedFood(food.id, true))
                }
                foodViewModel.getFoodFavorite(food.id).observeOnce(viewLifecycleOwner, {
                    navController.navigate(
                        MainFragmentDirections.actionMainFragmentToDetailsFragment(food, it ?: false)
                    )
                })
            }

            override fun itemLongClicked(food: Food, position: Int, view: View) {
                if (searchedHashMap[food.id] != true) return
                val popup = PopupMenu(requireContext(), view)
                popup.menuInflater.inflate(R.menu.searched_item, popup.menu)
                popup.setOnMenuItemClickListener {
                    foodViewModel.setSearchedFoods(SearchedFood(food.id, false))
                    adapter.searchItems.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    true
                }
                popup.show()
            }
        }

        var searchedItems:ArrayList<Food> = ArrayList()
        adapter.searchItems = searchedItems
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)

        foodViewModel.getSearchedFoods().observe(viewLifecycleOwner, {
            searchedItems = it as ArrayList<Food>
            if (firstOpen) {
                adapter.searchItems = searchedItems
                adapter.notifyDataSetChanged()
                firstOpen = false
            }
        })

        foodViewModel.getSearchedFoods().observeOnce(viewLifecycleOwner, {
            adapter.searchItems = it as ArrayList<Food>
            adapter.notifyDataSetChanged()
        })

        foodViewModel.readSearchedHashMap().observe(viewLifecycleOwner, {
            if (it != null){
                searchedHashMap = SearchedFood.convertToHashMap(it)
            }
        })

        editText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val search :String = p0.toString()
                if (search.isEmpty()){
                    historyTip.visibility = View.VISIBLE
                    adapter.searchItems = searchedItems
                    adapter.notifyDataSetChanged()
                }
                else{
                    historyTip.visibility = View.GONE
                    foodViewModel.searchForFoods("%"+p0.toString()+"%")
                        .observeOnce(viewLifecycleOwner, {
                            adapter.searchItems = it as ArrayList<Food>
                            adapter.notifyDataSetChanged()
                        })
                }
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })
    }

    private fun onBackPressed() {
        if (editText.text.isEmpty()) {
            navController2.popBackStack()
        }
        else editText.setText("")
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
