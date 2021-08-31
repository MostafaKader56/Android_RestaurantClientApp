package com.technoship.resturant.repo

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.core.content.pm.PackageInfoCompat.getLongVersionCode
import com.google.firebase.messaging.FirebaseMessaging
import com.technoship.resturant.dialogs.AppCreatorDialog
import com.technoship.resturant.utile.Constants
import com.technoship.resturant.viewmodel.SettingsViewModel

class SettingsRepository(private val application: Application) {
    private val pref: SharedPreferences = application.getSharedPreferences(MY_PREF_NAME, Context.MODE_PRIVATE)

    fun setModeKind(kind: Int) {
        val editor:SharedPreferences.Editor = pref.edit()
        editor.putInt(UI_MODE_ID, kind)
        editor.apply()
    }

    fun getModeKind(): Int {
        if (!pref.contains(UI_MODE_ID)){ FirebaseMessaging.getInstance().subscribeToTopic(Constants.FCM_ALL_USERS_TOPIC) }
        return pref.getInt(UI_MODE_ID, MODE_NIGHT_FOLLOW_SYSTEM)
    }

    fun getVersionCode(): Long {
        var pInfo: PackageInfo? = null
        try { pInfo = application.packageManager.getPackageInfo(application.packageName, 0) }
        catch (e: PackageManager.NameNotFoundException) { }
        return getLongVersionCode(pInfo!!)
    }

    fun checkAppVersionLocally(): Int {
        val currentAppVersion :Long = getVersionCode()

        if (pref.getInt(APP_VERSION_MUST_UPDATE_ID, -1) == -1)
            return SettingsViewModel.APP_VERSION_FIRST_OPEN

        return when {
            pref.getLong(APP_VERSION_MUST_UPDATE_ID, 0) >= currentAppVersion -> {
                SettingsViewModel.APP_VERSION_MUST_UPDATE
            }
            pref.getLong(APP_VERSION_ASK_4_UPDATE_ID, 0) >= currentAppVersion -> {
                SettingsViewModel.APP_VERSION_ASK_FOR_UPDATE
            }
            else -> {
                SettingsViewModel.APP_VERSION_OK
            }
        }

    }

    fun setAppVersionLocally(mustUpdate: Long, ask4Update: Long) {
        val editor:SharedPreferences.Editor = pref.edit()
        editor.putLong(APP_VERSION_MUST_UPDATE_ID, mustUpdate)
        editor.putLong(APP_VERSION_ASK_4_UPDATE_ID, ask4Update)
        editor.apply()
    }

    fun setAppCreatorData(
        appCreatorEnabled: Boolean,
        appCreatorName: String,
        appCreatorDescription: String,
        appCreatorImage: String,
        appCreatorPhone: String,
        appCreatorFbId: String,
        appCreatorLinkedinId: String,
        appCreatorPhoneEnabled: Boolean,
        appCreatorFacebookEnabled: Boolean,
        appCreatorLinkedinEnabled: Boolean,
        appCreatorImageEnabled: Boolean,
    ) {
        val editor:SharedPreferences.Editor = pref.edit()
        editor.putBoolean(APP_CREATOR_ENABLED, appCreatorEnabled)
        editor.putString(APP_CREATOR_NAME, appCreatorName)
        editor.putString(APP_CREATOR_DESCRIPTION, appCreatorDescription)
        editor.putString(APP_CREATOR_IMAGE, appCreatorImage)
        editor.putString(APP_CREATOR_PHONE, appCreatorPhone)
        editor.putString(APP_CREATOR_FACEBOOK_ID, appCreatorFbId)
        editor.putString(APP_CREATOR_LINKEDIN_ID, appCreatorLinkedinId)
        editor.putBoolean(APP_CREATOR_PHONE_ENABLED, appCreatorPhoneEnabled)
        editor.putBoolean(APP_CREATOR_FACEBOOK_ENABLED, appCreatorFacebookEnabled)
        editor.putBoolean(APP_CREATOR_LINKEDIN_ENABLED, appCreatorLinkedinEnabled)
        editor.putBoolean(APP_CREATOR_IMAGE_ENABLED, appCreatorImageEnabled)
        editor.apply()
    }

    fun getAppCreatorData() :AppCreatorDialog.CreatorDialogData{
        return AppCreatorDialog.CreatorDialogData(
            pref.getBoolean(APP_CREATOR_ENABLED, false),
            pref.getString(APP_CREATOR_NAME, "")!!,
            pref.getString(APP_CREATOR_DESCRIPTION, "")!!,
            pref.getString(APP_CREATOR_IMAGE, "")!!,
            pref.getString(APP_CREATOR_PHONE, "")!!,
            pref.getString(APP_CREATOR_FACEBOOK_ID, "")!!,
            pref.getString(APP_CREATOR_LINKEDIN_ID, "")!!,
            pref.getBoolean(APP_CREATOR_PHONE_ENABLED, false),
            pref.getBoolean(APP_CREATOR_FACEBOOK_ENABLED, false),
            pref.getBoolean(APP_CREATOR_LINKEDIN_ENABLED, false),
            pref.getBoolean(APP_CREATOR_IMAGE_ENABLED, false)
        )
    }

        companion object {
        private const val MY_PREF_NAME:String = "MY_PREF_NAME"
        private const val UI_MODE_ID:String = "UI_MODE_ID"
        private const val APP_VERSION_MUST_UPDATE_ID:String = "APP_VERSION_MUST_UPDATE_ID"
        private const val APP_VERSION_ASK_4_UPDATE_ID:String = "APP_VERSION_ASK_4_UPDATE_ID"

        private const val APP_CREATOR_ENABLED:String = "AppCreator_enabled"
        private const val APP_CREATOR_NAME:String = "AppCreator_name"
        private const val APP_CREATOR_DESCRIPTION:String = "AppCreator_description"
        private const val APP_CREATOR_IMAGE:String = "AppCreator_image"
        private const val APP_CREATOR_PHONE:String = "AppCreator_phone"
        private const val APP_CREATOR_FACEBOOK_ID:String = "AppCreator_facebookId"
        private const val APP_CREATOR_LINKEDIN_ID:String = "AppCreator_linkedinId"
        private const val APP_CREATOR_PHONE_ENABLED:String = "AppCreator_phoneEnabled"
        private const val APP_CREATOR_FACEBOOK_ENABLED:String = "AppCreator_facebookEnabled"
        private const val APP_CREATOR_LINKEDIN_ENABLED:String = "AppCreator_linkedinEnabled"
        private const val APP_CREATOR_IMAGE_ENABLED:String = "AppCreator_imageEnabled"
    }
}
