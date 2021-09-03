package com.technoship.resturant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.technoship.resturant.dialogs.AppCreatorDialog
import com.technoship.resturant.repo.RemoteConfigRepository
import com.technoship.resturant.repo.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val remoteConfigRepository = RemoteConfigRepository()
    private val settingsRepository: SettingsRepository = SettingsRepository(application)
    private val defaultsRate: MutableMap<String, Any> = hashMapOf(
        MUST_UPDATE_VERSION to 0,
        ASK_UPDATE_VERSION to 0,
        APP_CREATOR_ENABLED to false,
        APP_CREATOR_NAME to "",
        APP_CREATOR_DESCRIPTION to "",
        APP_CREATOR_IMAGE to "",
        APP_CREATOR_PHONE to "",
        APP_CREATOR_FACEBOOK_ID to "",
        APP_CREATOR_LINKEDIN_ID to "",
        APP_CREATOR_PHONE_ENABLED to false,
        APP_CREATOR_FACEBOOK_ENABLED to false,
        APP_CREATOR_LINKEDIN_ENABLED to false,
        APP_CREATOR_IMAGE_ENABLED to false
    )

    fun setModeKind(kind: Int){
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.setModeKind(kind)
        }
    }

    fun getModeKind():Int{
        return settingsRepository.getModeKind()
    }

    fun checkAppVersion(onCheckAppVersion:OnCheckAppVersion){
        remoteConfigRepository.getRemoteInfoData(defaultsRate, 3600 /* DONE: 3600 */,object :OnRemoteConfig{
            override fun onSuccess(remoteValues: MutableMap<String, FirebaseRemoteConfigValue>) {

                // App Version
                val mustUpdate :Long = remoteValues[MUST_UPDATE_VERSION]?.asLong()!!
                val ask4Update :Long = remoteValues[ASK_UPDATE_VERSION]?.asLong()!!
                val currentAppVersion :Long = settingsRepository.getVersionCode()

                when {
                    mustUpdate >= currentAppVersion -> {
                        onCheckAppVersion.onCheckAppVersion(APP_VERSION_MUST_UPDATE)
                    }
                    ask4Update >= currentAppVersion -> {
                        onCheckAppVersion.onCheckAppVersion(APP_VERSION_ASK_FOR_UPDATE)
                    }
                    else -> {
                        onCheckAppVersion.onCheckAppVersion(APP_VERSION_OK)
                    }
                }
                settingsRepository.setAppVersionLocally(mustUpdate, currentAppVersion)

                // App Creator
                val appCreatorEnabled = remoteValues[APP_CREATOR_ENABLED]?.asBoolean()
                val appCreatorName = remoteValues[APP_CREATOR_NAME]?.asString()
                val appCreatorDescription =
                    remoteValues[APP_CREATOR_DESCRIPTION]?.asString()?.replace("\\n", "\n")
                val appCreatorImage = remoteValues[APP_CREATOR_IMAGE]?.asString()
                val appCreatorPhone = remoteValues[APP_CREATOR_PHONE]?.asString()
                val appCreatorFbId = remoteValues[APP_CREATOR_FACEBOOK_ID]?.asString()
                val appCreatorLinkedinId = remoteValues[APP_CREATOR_LINKEDIN_ID]?.asString()
                val appCreatorPhoneEnabled = remoteValues[APP_CREATOR_PHONE_ENABLED]?.asBoolean()
                val appCreatorFacebookEnabled = remoteValues[APP_CREATOR_FACEBOOK_ENABLED]?.asBoolean()
                val appCreatorLinkedinEnabled = remoteValues[APP_CREATOR_LINKEDIN_ENABLED]?.asBoolean()
                val appCreatorImageEnabled = remoteValues[APP_CREATOR_IMAGE_ENABLED]?.asBoolean()

                // DONE: add setter and getter to saving app creator in settings repo
                settingsRepository.setAppCreatorData(appCreatorEnabled!!, appCreatorName!!, appCreatorDescription!!,
                    appCreatorImage!!, appCreatorPhone!!, appCreatorFbId!!, appCreatorLinkedinId!!, appCreatorPhoneEnabled!!,
                    appCreatorFacebookEnabled!!, appCreatorLinkedinEnabled!!, appCreatorImageEnabled!!)
            }

            override fun onFail() {
                onCheckAppVersion.onCheckAppVersion(settingsRepository.checkAppVersionLocally())
            }
        })
    }

    fun getAppCreatorData() :AppCreatorDialog.CreatorDialogData{
        return settingsRepository.getAppCreatorData()
    }

    interface OnCheckAppVersion{
        fun onCheckAppVersion(status:Int)
    }

    interface OnRemoteConfig{
        fun onSuccess(remoteValues:MutableMap<String, FirebaseRemoteConfigValue>)
        fun onFail()
    }

    companion object {
        const val APP_VERSION_OK:Int = 1
        const val APP_VERSION_ASK_FOR_UPDATE:Int = 2
        const val APP_VERSION_MUST_UPDATE:Int = 3
        const val APP_VERSION_FIRST_OPEN:Int = 4
        
        const val MUST_UPDATE_VERSION:String = "MustUpdateVersion"
        const val ASK_UPDATE_VERSION:String = "Ask4UpdateVersion"
        const val APP_CREATOR_ENABLED:String = "AppCreator_enabled"
        const val APP_CREATOR_NAME:String = "AppCreator_name"
        const val APP_CREATOR_DESCRIPTION:String = "AppCreator_description"
        const val APP_CREATOR_IMAGE:String = "AppCreator_image"
        const val APP_CREATOR_PHONE:String = "AppCreator_phone"
        const val APP_CREATOR_FACEBOOK_ID:String = "AppCreator_facebookId"
        const val APP_CREATOR_LINKEDIN_ID:String = "AppCreator_linkedinId"
        const val APP_CREATOR_PHONE_ENABLED:String = "AppCreator_phoneEnabled"
        const val APP_CREATOR_FACEBOOK_ENABLED:String = "AppCreator_facebookEnabled"
        const val APP_CREATOR_LINKEDIN_ENABLED:String = "AppCreator_linkedinEnabled"
        const val APP_CREATOR_IMAGE_ENABLED:String = "AppCreator_imageEnabled"
    }
}