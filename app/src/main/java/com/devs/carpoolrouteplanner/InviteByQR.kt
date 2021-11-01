package com.devs.carpoolrouteplanner

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.launch

class InviteByQR : AppCompatActivity() {
/*

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_by_qr)
        val buttonShare: Button = findViewById(R.id.btnShare)
        val txtShareText: EditText = findViewById(R.id.shareText)
        val btnReloadQr: Button  = findViewById(R.id.btnReloadQr)
        var gid = intent.getStringExtra("GID")
        if (gid == null) gid = "0"

        loadQR(gid)


        buttonShare.setOnClickListener {
            val shareIntent = Intent()
            val shareText = txtShareText.text.toString()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                shareText)
            startActivity(Intent.createChooser(shareIntent, "Send To"))

        }

        btnReloadQr.setOnClickListener{
            loadQR(gid)
        }

    }


    fun qrToImageView(imageView:ImageView,content:String){

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, imageView.height, imageView.width)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        imageView.setImageBitmap(bitmap)
    }

    fun loadQR(gid: String) {
        val txtShareText: EditText = findViewById(R.id.shareText)
        val imageView:ImageView = findViewById(R.id.imgQr)
        imageView.setImageResource(R.drawable.ic_launcher_background);
        txtShareText.setText("loading...")
        val myurl = getConfigValue("backend_url")
        lifecycleScope.launch {

            val client = HttpClient(CIO) {
                install(Auth) {
                    basic {
                        credentials {
                            BasicAuthCredentials(username = AccountSignIn.creds[0],
                                password = AccountSignIn.creds[1])
                        }

                    }
                }
            }

            try {
                val response: String = client.submitForm(
                    url = myurl + "create_invite/",
                    formParameters = Parameters.build {
                        append("gid", gid)
                    }
                )
                val data = response
                Toast.makeText(applicationContext,
                    data,
                    Toast.LENGTH_LONG).show()
                var inviteCode = data;
//                    inviteOthers.isClickable = true

                var dummyLink = "https://coolrouteplanner.test/join/${inviteCode}";
                val text =
                    "Please join my group using this invite code: ${inviteCode} or click on this link: {$dummyLink}"
//
                txtShareText.setText(text)
                qrToImageView(imageView,dummyLink)
            } catch (E: Exception) {
                Toast.makeText(applicationContext,
                    "Exception: No group with gid 123 or ${E.message}. Loading dummy data.",
                    Toast.LENGTH_LONG).show()
                txtShareText.setText("Please join my group using this invite code: invitecode or click on this link: https://coolrouteplanner.test/join/invitecode")
                qrToImageView(imageView,"https://coolrouteplanner.test/join/invitecode")

            }

        }
    }

 */
}

