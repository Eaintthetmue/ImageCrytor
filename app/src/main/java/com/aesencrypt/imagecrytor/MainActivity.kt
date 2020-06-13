package com.aesencrypt.imagecrytor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aesencrypt.imagecrytor.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnEncryptKey.setOnClickListener {
            startActivity(Intent(this, EncryptionActivity::class.java))
        }

        btnDecryptionKey.setOnClickListener {
            startActivity(Intent(this, DecryptionActivity::class.java))
        }

    }
}
