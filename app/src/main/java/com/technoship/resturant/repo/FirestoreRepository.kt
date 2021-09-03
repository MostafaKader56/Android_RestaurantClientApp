package com.technoship.resturant.repo

import android.os.Handler
import android.os.Looper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.technoship.resturant.response.FoodFirebaseResponse
import com.technoship.resturant.viewmodel.FoodViewModel

class FirestoreRepository {
    private val firestore : FirebaseFirestore = Firebase.firestore
    private var isOpening = false

    fun getFoodsFromFirestore(onFoodsLoadedFormFirestore: FoodViewModel.OnFoodsLoadedFormFirestore) {
        firestore.collection(DATA_FIRESTORE_ID).document(DATA_FIRESTORE_ID)
            .get().addOnCompleteListener{task ->
                if (isOpening) return@addOnCompleteListener
                isOpening = true
                if (task.isSuccessful){
                    val it = task.result
                    if(it.exists() && it != null){
                        val value : FoodFirebaseResponse = it.toObject(FoodFirebaseResponse::class.java)!!
                        onFoodsLoadedFormFirestore.onSuccess(value.data)
                    }
                    else{
                        onFoodsLoadedFormFirestore.onFail()
                    }
                }
                else{
                    onFoodsLoadedFormFirestore.onFail()
                }
            }
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isOpening) {
                isOpening = true
                onFoodsLoadedFormFirestore.onFail()
            }
        }, 4500)
    }

    companion object{
        private const val DATA_FIRESTORE_ID = "data"
    }

}