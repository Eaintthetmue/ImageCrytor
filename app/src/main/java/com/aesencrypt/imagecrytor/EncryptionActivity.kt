package com.aesencrypt.imagecrytor

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_encryption.*
import pl.aprilapps.easyphotopicker.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec


class EncryptionActivity : AppCompatActivity() {

    private lateinit var easyImage: EasyImage
    private lateinit var file: File
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

                else -> {
                    encrypt()
                }
            }
        }


        btnSelectImageFromGallery.setOnClickListener {
            easyImage.openGallery(this)
        }
    }


    fun encrypt() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            try {
                val picDir = File(getExternalFilesDir(null).toString() + "/ImageCryptor/")
                if (!picDir.exists()) {
                    picDir.mkdir()
                }

                val originalFile = FileInputStream(file.absolutePath)
                // This stream write the encrypted text. This stream will be wrapped by
                // another stream.
                val encryptedFile = FileOutputStream("$picDir/1.encrypted")

                // Length is 16 byte
                val key = SecretKeySpec(
                    "MyDifficultPassw".toByteArray(),
                    "AES"
                )
                val aes = Cipher.getInstance("AES")
                aes.init(Cipher.ENCRYPT_MODE, key)
                val out = CipherOutputStream(encryptedFile, aes)
                out.write(originalFile.readBytes())
                out.flush()
                out.close()
                Toast.makeText(this, "Successfully imported", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(this, "Unable to write", Toast.LENGTH_SHORT).show()
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
                    file = imageFiles[0].file
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
