package com.technoship.resturant.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.technoship.resturant.R
import com.technoship.resturant.viewmodel.SettingsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var settingsViewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        AppCompatDelegate.setDefaultNightMode(settingsViewModel.getModeKind())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
