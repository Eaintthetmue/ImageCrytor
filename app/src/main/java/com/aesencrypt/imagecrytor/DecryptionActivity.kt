package com.aesencrypt.imagecrytor

import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_decryption.*
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec

class DecryptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decryption)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Decryption Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        btnDecryptKey.setOnClickListener {
            when {
                edDecryptText.text.isNullOrBlank() -> {
                    Toast.makeText(
                        applicationContext,
                        "Please enter decryption text",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                edDecryptKey.text.isNullOrBlank() -> {
                    Toast.makeText(
                        applicationContext,
                        "Please enter password key",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                edDecryptKey.text!!.length != 8 -> {
                    Toast.makeText(
                        applicationContext,
                        "Password key should be 8 character only",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    tvDecryptOutput.text = ""
                    val encryptionText = decrypt(
                        edDecryptText.text.toString(),
                        edDecryptKey.text.toString(),
                        applicationContext
                    )
                    tvDecryptOutput.text = encryptionText
                }
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return true
    }


    fun decrypt(value: String, password: String, c: Context?): String? {
        val coded: String
        coded = if (value.startsWith("code==")) {
            value.substring(6, value.length).trim { it <= ' ' }
        } else {
            value.trim { it <= ' ' }
        }
        var result: String? = null
        result = try { // Decoding base64
            val bytesDecoded = Base64.decode(
                coded.toByteArray(charset("UTF-8")),
                Base64.DEFAULT
            )
            val key =
                SecretKeySpec(password.toByteArray(), "DES")
            val cipher =
                Cipher.getInstance("DES/ECB/ZeroBytePadding")
            // Initialize the cipher for decryption
            cipher.init(Cipher.DECRYPT_MODE, key)
            // Decrypt the text
            val textDecrypted = cipher.doFinal(bytesDecoded)
            String(textDecrypted)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return "Decrypt Error"
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
            return "Decrypt Error"
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
            return "Decrypt Error"
        } catch (e: BadPaddingException) {
            e.printStackTrace()
            return "Decrypt Error"
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
            return "Decrypt Error"
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return "Decrypt Error"
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return "Decrypt Error"
        }
        return result
    }
}
