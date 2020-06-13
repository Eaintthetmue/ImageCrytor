package com.aesencrypt.imagecrytor

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_encryption.*
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec


class EncryptionActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encryption)

        val actionbar = supportActionBar
        actionbar!!.title = "Encryption Activity"
        actionbar.setDisplayHomeAsUpEnabled(true)

        btnEncryptKey.setOnClickListener {
            when {
                edEncryptText.text.isNullOrBlank() -> {
                    Toast.makeText(
                        applicationContext,
                        "Please enter encryption text",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
                    tvEncryptOutput.text = ""
                    val encryptionText = encrypt(
                        edEncryptText.text.toString(),
                        edEncryptKey.text.toString(),
                        applicationContext
                    )
                    tvEncryptOutput.text = encryptionText
                    btnCopy.visibility = View.VISIBLE
                }
            }
        }

        btnCopy.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                val clipboard =
                    getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
                clipboard.text = tvEncryptOutput.text
                Toast.makeText(applicationContext, "Copied to Clipboard!", Toast.LENGTH_SHORT)
                    .show()

            } else {

                val clipboard =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", tvEncryptOutput.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(applicationContext, "Copied to Clipboard!", Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return true
    }


    private fun encrypt(value: String, password: String, c: Context?): String? {
        var crypted = ""
        crypted = try {
            val cleartext = value.toByteArray(charset("UTF-8"))
            val key = SecretKeySpec(password.toByteArray(), "DES")
            val cipher: Cipher = Cipher.getInstance("DES/ECB/ZeroBytePadding")
            // Initialize the cipher for decryption
            cipher.init(Cipher.ENCRYPT_MODE, key)
            Base64.encodeToString(cipher.doFinal(cleartext), Base64.DEFAULT)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return "Encrypt Error"
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            return "Encrypt Error"
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
            return "Encrypt Error"
        } catch (e: BadPaddingException) {
            e.printStackTrace()
            return "Encrypt Error"
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
            return "Encrypt Error"
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return "Encrypt Error"
        } catch (e: Exception) {
            e.printStackTrace()
            return "Encrypt Error"
        }
        return crypted
    }


}
