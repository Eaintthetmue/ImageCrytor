package com.aesencrypt.imagecrytor

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_encryption.*
import pl.aprilapps.easyphotopicker.*


class EncryptionActivity : AppCompatActivity() {

    private lateinit var easyImage: EasyImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encryption)

        val actionbar = supportActionBar
        actionbar!!.title = "Encryption Activity"
        actionbar.setDisplayHomeAsUpEnabled(true)

        easyImage = EasyImage.Builder(this@EncryptionActivity)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY).build()

        btnEncryptKey.setOnClickListener {
            when {

                edEncryptKey.text.isNullOrBlank() -> {
                    Toast.makeText(
                        applicationContext,
                        "Please enter password key",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                edEncryptKey.text!!.length != 8 -> {
                    Toast.makeText(
                        applicationContext,
                        "Password key should be 8 character only",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        btnSelectImageFromGallery.setOnClickListener {
            easyImage.openGallery(this)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onMediaFilesPicked(
                    imageFiles: Array<MediaFile>,
                    source: MediaSource
                ) {
                    Glide.with(this@EncryptionActivity)
                        .load(imageFiles[0].file)
                        .into(ivImage)
                    ivImage.visibility = View.VISIBLE
                    edEncryptKey.visibility = View.VISIBLE
                    btnEncryptKey.visibility = View.VISIBLE
                }

                override fun onImagePickerError(
                    error: Throwable,
                    source: MediaSource
                ) {
                    error.printStackTrace()
                }

                override fun onCanceled(source: MediaSource) { //Not necessary to remove any files manually anymore
                }
            })
    }


}
