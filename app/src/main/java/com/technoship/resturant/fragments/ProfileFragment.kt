package com.technoship.resturant.fragments

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.technoship.resturant.R
import com.technoship.resturant.dialogs.AppCreatorDialog
import com.technoship.resturant.viewmodel.SettingsViewModel

class ProfileFragment : Fragment() {

    private lateinit var darkModeSwitch: SwitchMaterial
    private lateinit var profileAppCreator: TextView
    private lateinit var landPhoneBtn: View
    private lateinit var phoneBtn1: View
    private lateinit var phoneBtn2: View
    private lateinit var googleMapsBtn: View
    private lateinit var facebookPageBtn: View
    private lateinit var shareAppBtn: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)
        darkModeSwitch = view.findViewById(R.id.dark_mode_switch)
        profileAppCreator = view.findViewById(R.id.profile_app_creator)
        landPhoneBtn = view.findViewById(R.id.profile_landPhoneBtn)
        phoneBtn1 = view.findViewById(R.id.profile_phoneBtn1)
        phoneBtn2 = view.findViewById(R.id.profile_phoneBtn2)
        googleMapsBtn = view.findViewById(R.id.profile_googleMapsBtn)
        facebookPageBtn = view.findViewById(R.id.profile_facebookPageBtn)
        shareAppBtn = view.findViewById(R.id.profile_shareAppBtn)
        when (requireContext().resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {darkModeSwitch.isChecked = true}
        }
        return view
    }

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        val appCreator = settingsViewModel.getAppCreatorData()

        if (appCreator.enableDialog){
            profileAppCreator.visibility = View.VISIBLE
            val appCreatorDialog = AppCreatorDialog(requireContext(), settingsViewModel.getAppCreatorData())
            profileAppCreator.setOnClickListener { appCreatorDialog.show() }
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                setDefaultNightMode(MODE_NIGHT_YES)
                settingsViewModel.setModeKind(MODE_NIGHT_YES)
            }
            else{
                setDefaultNightMode(MODE_NIGHT_NO)
                settingsViewModel.setModeKind(MODE_NIGHT_NO)
            }
        }

        // Buttons on Click Listeners
        landPhoneBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:0963339997")
            requireContext().startActivity(intent)
        }
        phoneBtn1.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:01050108006")
            requireContext().startActivity(intent)
        }
        phoneBtn2.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:01050108007")
            requireContext().startActivity(intent)
        }
        googleMapsBtn.setOnClickListener {
            val uri = "geo:0,0?q=MOODS+Restaurant+%26+Cafe(MOODS Restaurant & Cafe)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            requireContext().startActivity(intent)
        }
        facebookPageBtn.setOnClickListener {
            var uri = Uri.parse("https://www.facebook.com/MoodsQena")
            try {
                val applicationInfo: ApplicationInfo =
                    requireContext().packageManager.getApplicationInfo("com.facebook.katana", 0)
                if (applicationInfo.enabled) {
                    uri = Uri.parse("fb://page/101196401435847")
                }
            } catch (ignored: PackageManager.NameNotFoundException) {
            }
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
        shareAppBtn.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "https://play.google.com/store/apps/details?id=" + requireContext().packageName
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share app")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "share vai"))
        }
    }
}