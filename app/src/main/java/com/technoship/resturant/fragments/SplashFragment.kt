package com.technoship.resturant.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.technoship.resturant.R
import com.technoship.resturant.viewmodel.FoodViewModel
import com.technoship.resturant.viewmodel.SettingsViewModel

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        startProgress = view.findViewById(R.id.startProgress)
        startDetails = view.findViewById(R.id.startDetails)
        return view
    }

    private lateinit var startDetails: TextView
    private lateinit var startProgress: ProgressBar
    private lateinit var navController:NavController
    private lateinit var foodViewModel:FoodViewModel
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)

        startDetails.text = getString(R.string.splashFragment_check_version_tip)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        settingsViewModel.checkAppVersion(object :SettingsViewModel.OnCheckAppVersion{
            override fun onCheckAppVersion(status: Int) {
                when (status) {
                    SettingsViewModel.APP_VERSION_MUST_UPDATE -> {
                        startProgress.visibility = View.INVISIBLE
                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.splashFragment_alert))
                            .setMessage(getString(R.string.splashFragment_mustUpdataMessage))
                            .setCancelable(false)
                            .setNegativeButton(getString(R.string.splashFragment_exit)) { _, _ -> }
                            .setPositiveButton(getString(R.string.splashFragment_update)) { _, _ ->
                                val appPackageName = requireContext().packageName
                                try {
                                    startActivity(Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$appPackageName")))
                                } catch (anfe: ActivityNotFoundException) {
                                    startActivity(Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                                }
                            }
                            .setOnDismissListener {
                                requireActivity().onBackPressed()
                            }
                            .create().show()
                    }
                    SettingsViewModel.APP_VERSION_ASK_FOR_UPDATE -> {
                        startProgress.visibility = View.INVISIBLE
                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.splashFragment_alert))
                            .setMessage(getString(R.string.splashFragment_shouldUpdataMessage))
                            .setCancelable(false)
                            .setNegativeButton("Discard") { _, _ ->
                                versionCheckPassed()
                                startProgress.visibility = View.VISIBLE
                            }
                            .setPositiveButton(getString(R.string.splashFragment_update)) { _, _ ->
                                val appPackageName = requireContext().packageName
                                try {
                                    startActivity(Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$appPackageName")))
                                } catch (anfe: ActivityNotFoundException) {
                                    startActivity(Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                                }
                                requireActivity().onBackPressed()
                            }
                            .create().show()
                    }
                    SettingsViewModel.APP_VERSION_FIRST_OPEN -> {
                        startProgress.visibility = View.INVISIBLE
                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.splashFragment_alert))
                            .setMessage(getString(R.string.splashFragment_firestOpenMessage))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.splashFragment_ok)) { _, _ -> }
                            .setOnDismissListener {
                                requireActivity().onBackPressed()
                            }
                            .create().show()
                    }
                    SettingsViewModel.APP_VERSION_OK -> {
                        versionCheckPassed()
                    }
                }
            }
        })
    }

    private fun versionCheckPassed(){
        startDetails.text = getString(R.string.splashFragment_loading_data)
        foodViewModel.getFoodsFromFirestoreToRoomDB(object:FoodViewModel.OnCallFinishedWithErrorOrWithout{
            override fun callback() {
                startDetails.text = getString(R.string.splashFragment_opening)
                Handler(Looper.getMainLooper()).postDelayed({
                    navController.navigate(SplashFragmentDirections.actionSplashFragment2ToMainFragment())
                }, 1500)
            }
        })
    }

}

//        val items:ArrayList<Food> = ArrayList()
//        items.add(Food(Food.SANDWICH, 0,"Mood Waffle", "",true, true, true, "https://firebasestorage.googleapis.com/v0/b/gleaming-nomad-321522.appspot.com/o/test%2Fsandwich%2Fmood%20waffle.jpg?alt=media&token=f25dd357-dbef-4d28-ba13-3d28a983f249"))
//        items.add(Food(Food.SANDWICH, 0,"Salami", "",true, true, false, "https://firebasestorage.googleapis.com/v0/b/gleaming-nomad-321522.appspot.com/o/test%2Fsandwich%2Fsalami.jpg?alt=media&token=6360fbed-e961-45ff-97a3-6424cb28613e"))
//        items.add(Food(Food.SANDWICH, 0,"Fajita Fried", "",true, true, true, "https://firebasestorage.googleapis.com/v0/b/gleaming-nomad-321522.appspot.com/o/test%2Fsandwich%2Ffajita%20fried.jpg?alt=media&token=35e799f6-ac02-4acc-baa6-0a73e57797b6"))
//        items.add(Food(Food.SANDWICH, 0,"Grilly", "",true, true, false, "https://firebasestorage.googleapis.com/v0/b/gleaming-nomad-321522.appspot.com/o/test%2Fsandwich%2Fgrilly.jpg?alt=media&token=d79df6c0-9d60-4624-86bc-e46f6a8e3c08"))
//        items.add(Food(Food.SANDWICH, 0,"Mini Classic", "",true, true, true, "https://firebasestorage.googleapis.com/v0/b/gleaming-nomad-321522.appspot.com/o/test%2Fsandwich%2F1.jpg?alt=media&token=d56dbf61-c377-4888-a84c-28fb3d54e133"))
//        items.add(Food(Food.SANDWICH, 0,"Classic", "",true, true, false, "https://firebasestorage.googleapis.com/v0/b/gleaming-nomad-321522.appspot.com/o/test%2Fsandwich%2Fclassic.jpg?alt=media&token=5bd794f9-897e-4f8e-8b68-cda9c863c0f8"))
//        items.add(Food(Food.SANDWICH, 0,"Chicken Ranchy", "",true, true, true, "https://firebasestorage.googleapis.com/v0/b/gleaming-nomad-321522.appspot.com/o/test%2Fsandwich%2F2.jpg?alt=media&token=318d1da9-7934-4ff9-8998-7da251cdd3b2"))
//        items.add(Food(Food.SANDWICH, 0,"Boom Mozzarela", "",true, true, false, "https://firebasestorage.googleapis.com/v0/b/gleaming-nomad-321522.appspot.com/o/test%2Fsandwich%2F4.jpg?alt=media&token=e5c150e0-75c4-4476-ad8f-2143a820aabd"))
//        items.add(Food(Food.SANDWICH, 0,"Moods Fire", "",true, true, true, "https://firebasestorage.googleapis.com/v0/b/gleaming-nomad-321522.appspot.com/o/test%2Fsandwich%2Fss.png?alt=media&token=685a1c9d-18d2-4ddf-aa6b-2a33e9a74dbb"))
//        items.add(Food(Food.SANDWICH, 0,"Chicken Turkey", "",true, true, false, "https://firebasestorage.googleapis.com/v0/b/gleaming-nomad-321522.appspot.com/o/test%2Fsandwich%2Fchicken%20turky.jpg?alt=media&token=7600d645-3361-401e-b670-b9e12b51268d"))
//        foodViewModel.addFoodsToLocal(items)