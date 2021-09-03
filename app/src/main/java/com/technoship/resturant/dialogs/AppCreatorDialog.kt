package com.technoship.resturant.dialogs

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.technoship.resturant.R
import de.hdodenhof.circleimageview.CircleImageView


class AppCreatorDialog(private val ourContext: Context, private val data: CreatorDialogData){

    private var dialog: AlertDialog
    private val image: CircleImageView
    private val title: TextView
    private val desc: TextView
    private val closeIcon: ImageView
    private val linkedInIcon: ImageView
    private val facebookIcon: ImageView
    private val phoneIcon: ImageView

    init {
        val view = View.inflate(ourContext, R.layout.creator_dialog, null)
        image = view.findViewById(R.id.creator_dialog_image)
        title = view.findViewById(R.id.creator_dialog_title)
        desc = view.findViewById(R.id.creator_dialog_desc)
        linkedInIcon = view.findViewById(R.id.creator_dialog_linkedin)
        facebookIcon = view.findViewById(R.id.creator_dialog_facebook)
        phoneIcon = view.findViewById(R.id.creator_dialog_phone)
        closeIcon = view.findViewById(R.id.creator_dialog_close)

        val builder = AlertDialog.Builder(ourContext)

        if (data.enableDialog){
            if (data.viewImage) {
                image.visibility = View.VISIBLE
                Glide
                    .with(ourContext)
                    .load(data.img)
                    .centerCrop()
                    .into(image)
            }

            title.text = data.name
            desc.text = data.desc
            closeIcon.setOnClickListener {
                closeClicked()
            }
            if (data.viewFacebook){
                facebookIcon.visibility = View.VISIBLE
                facebookIcon.setOnClickListener {
                    facebookClicked()
                }
            }
            if (data.viewLinkedIn){
                linkedInIcon.visibility = View.VISIBLE
                linkedInIcon.setOnClickListener {
                    linkedInClicked()
                }
            }
            if (data.viewPhone){
                phoneIcon.visibility = View.VISIBLE
                phoneIcon.setOnClickListener {
                    phoneClicked()
                }
            }
        }

        builder.setView(view)
        dialog = builder.create()
    }

    private fun closeClicked() {
        dialog.dismiss()
    }

    private fun phoneClicked() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${data.phone}")
        ourContext.startActivity(intent)
    }

    private fun linkedInClicked() {
//        873428977845634897
        ourContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=${data.linkedInId}")))
    }

    private fun facebookClicked() {
        var uri = Uri.parse("https://www.facebook.com/${data.facebookId}")
        try {
            val applicationInfo: ApplicationInfo =
                ourContext.packageManager.getApplicationInfo("com.facebook.katana", 0)
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://profile/${data.facebookId}")
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
        }
        ourContext.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    fun show(){
        dialog.show()
    }

    class CreatorDialogData(
        val enableDialog: Boolean,
        val name: String,
        val desc: String,
        val img: String,
        val phone: String,
        val facebookId: String,
        val linkedInId: String,
        val viewFacebook: Boolean,
        val viewLinkedIn: Boolean,
        val viewPhone: Boolean,
        val viewImage: Boolean
    )
}
