package com.technoship.resturant.repo

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.technoship.resturant.viewmodel.SettingsViewModel

class RemoteConfigRepository {
    private val remoteConfig : FirebaseRemoteConfig = Firebase.remoteConfig
    private var isOpening = false

    fun getRemoteInfoData(defaultsRate: MutableMap<String, Any>, seconds:Long ,onRemoteConfig:SettingsViewModel.OnRemoteConfig) {
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = seconds
            }
        )
        remoteConfig.setDefaultsAsync(defaultsRate)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (isOpening) return@addOnCompleteListener
            isOpening = true
            if (task.isSuccessful) {
                onRemoteConfig.onSuccess(remoteConfig.all)
            } else {
                onRemoteConfig.onFail()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isOpening) {
                isOpening = true
                onRemoteConfig.onFail()
            }
        }, 4500)
    }
}