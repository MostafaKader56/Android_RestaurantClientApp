package com.technoship.resturant.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.technoship.resturant.R
import com.technoship.resturant.model.FavoriteFood
import com.technoship.resturant.model.Food
import com.technoship.resturant.viewmodel.FoodViewModel

class DetailsFragment : Fragment() {
    private lateinit var food: Food
    private var favorite: Boolean = false

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.let {
            food = it.food
            favorite = it.favorite
        }
    }

    private lateinit var imageView: ImageView
    private lateinit var favoriteContainer: View
    private lateinit var foodFavorite: CheckBox
    private lateinit var foodNew: View
    private lateinit var foodTitle: TextView
    private lateinit var foodDescription: TextView
    private lateinit var twoPricesBlock: View
    private lateinit var threePricesBlock: View
    private lateinit var onePriceTextView: TextView
    private lateinit var twoPricesSmall: TextView
    private lateinit var twoPricesLarge: TextView
    private lateinit var threePricesSingle: TextView
    private lateinit var threePricesDouble: TextView
    private lateinit var threePricesTriple: TextView
    private lateinit var backArrow: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View = inflater.inflate(R.layout.fragment_details, container, false)
        onePriceTextView = view.findViewById(R.id.onePrice_price)
        favoriteContainer = view.findViewById(R.id.food_favourite_container)
        imageView = view.findViewById(R.id.food_image)
        foodFavorite = view.findViewById(R.id.food_favourite)
        foodNew = view.findViewById(R.id.food_new)
        foodTitle = view.findViewById(R.id.food_title)
        foodDescription = view.findViewById(R.id.food_description)
        onePriceTextView = view.findViewById(R.id.onePrice_price)
        twoPricesBlock = view.findViewById(R.id.twoPrices_block)
        threePricesBlock = view.findViewById(R.id.threePrices_block)
        twoPricesSmall = view.findViewById(R.id.twoPrices_small)
        twoPricesLarge = view.findViewById(R.id.twoPrices_large)
        threePricesSingle = view.findViewById(R.id.threePrices_single)
        threePricesDouble = view.findViewById(R.id.threePrices_double)
        threePricesTriple = view.findViewById(R.id.threePrices_triple)
        backArrow = view.findViewById(R.id.backArrow)
        return view
    }

    private lateinit var foodViewModel: FoodViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide
            .with(requireContext())
            .load(food.img)
            .into(imageView)

        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)

        if (food.newFood) foodNew.visibility = View.VISIBLE
        foodFavorite.isChecked = favorite
        favoriteContainer.setOnClickListener {
            favorite = !favorite
            foodFavorite.isChecked = favorite
            foodViewModel.setFavoriteFoods(FavoriteFood(food.id, favorite))
        }
        foodTitle.text = food.title
        foodDescription.text = food.description

        when(food.priceKind){
            Food.HEADER_NONE ->{
                // not initialized
            }
            Food.HEADER_WITHOUT -> {
                // one price without header
                onePriceTextView.text = food.priceString
                onePriceTextView.visibility = View.VISIBLE
            }
            Food.HEADER_LARGE_SMALL ->{
                // two price with header large and small
                val pricesValuesList:List<String> = food.priceString.split(Food.FOOD_PRICE_STING_SEGMENTATION.toRegex())
                twoPricesSmall.text = pricesValuesList[0]
                twoPricesLarge.text = pricesValuesList[1]
                twoPricesBlock.visibility = View.VISIBLE
            }
            Food.HEADER_SINGLE_DOUBLE_TRIPLE ->{
                // three price with header single, double and triple
                val pricesValuesList:List<String> = food.priceString.split(Food.FOOD_PRICE_STING_SEGMENTATION.toRegex())
                threePricesSingle.text = pricesValuesList[0]
                threePricesDouble.text = pricesValuesList[1]
                threePricesTriple.text = pricesValuesList[2]
                threePricesBlock.visibility = View.VISIBLE
            }
            else -> {
                // error
                onePriceTextView.text = getString(R.string.detailsFragment_loading_price_error_tip)
                onePriceTextView.visibility = View.VISIBLE
            }
        }

        backArrow.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }
}