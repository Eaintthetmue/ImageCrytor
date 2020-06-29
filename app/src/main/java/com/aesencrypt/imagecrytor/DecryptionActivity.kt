package com.aesencrypt.imagecrytor

import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.image_item.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec

class DecryptionActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decryption)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Decryption Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)


        ivImage.setOnClickListener(this)

    }

    private fun decrypt() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            try {
                var file = File(getExternalFilesDir(null).toString() + "/ImageCryptor/1.encrypted")
                if (file.exists()) {
                    val picDir = File(getExternalFilesDir(null).toString() + "/ImageCryptor/")
                    val encryptedFile = FileInputStream(file.absolutePath)
                    // This stream write the encrypted text. This stream will be wrapped by
                    // another stream.
                    val decryptedFile = FileOutputStream("$picDir/1.jpg")

                    val sks = SecretKeySpec(
                        "MyDifficultPassw".toByteArray(),
                        "AES"
                    )

                    val cipher = Cipher.getInstance("AES")
                    cipher.init(Cipher.DECRYPT_MODE, sks)
                    val cis = CipherInputStream(encryptedFile, cipher)
                    var b: Int
                    val d = ByteArray(8)
                    while (cis.read(d).also { b = it } != -1) {
                        decryptedFile.write(d, 0, b)
                    }
                    decryptedFile.flush()
                    decryptedFile.close()
                    cis.close()
                    Toast.makeText(this, "Successfully decrypted", Toast.LENGTH_SHORT).show()
                    ivLock.visibility = View.GONE
                    file = File(getExternalFilesDir(null).toString() + "/ImageCryptor/1.jpg")
                    Glide.with(this).load(file).into(ivImage)
                    tvTitle.text = "Click on image \nfor encrypt the image"
                } else {
                    Toast.makeText(this, "Please first encrypt the image", Toast.LENGTH_SHORT)
                        .show()
                }
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

    override fun onClick(p0: View?) {
        if (ivLock.visibility == View.VISIBLE) {
            decrypt()
        } else {
            encrypt()
        }
    }

    fun encrypt() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            try {
                val file = File(getExternalFilesDir(null).toString() + "/ImageCryptor/1.jpg")
                if (file.exists()) {
                    val picDir = File(getExternalFilesDir(null).toString() + "/ImageCryptor/")
                    val decryptedFile = FileInputStream(file.absolutePath)
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
                    out.write(decryptedFile.readBytes())
                    out.flush()
                    out.close()
                    Toast.makeText(this, "Successfully encrypted", Toast.LENGTH_SHORT).show()

                    ivLock.visibility = View.VISIBLE
                    ivImage.setImageDrawable(null)
                    tvTitle.text = "Click on lock icon\nfor decrypt the image"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(this, "Unable to write", Toast.LENGTH_SHORT).show()
        }
    }


}
