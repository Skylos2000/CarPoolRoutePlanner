package com.devs.carpoolrouteplanner.ui.qrinvite

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.ui.AccountSignIn
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.devs.carpoolrouteplanner.utils.httpClient
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.request.forms.submitForm
import io.ktor.http.*
import kotlinx.android.synthetic.main.fragment_invite_qr.*
import kotlinx.coroutines.launch



/**
 * A simple [Fragment] subclass.
 * Use the [InviteQrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InviteQrFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        //code
        val v:View =  inflater.inflate(R.layout.fragment_invite_qr, container, false)

        val buttonShare: Button = v.findViewById(R.id.btnShare)
        val txtShareText: EditText = v.findViewById(R.id.shareText)
        val btnReloadQr: Button  = v.findViewById(R.id.btnReloadQr)



        buttonShare.setOnClickListener {
            val shareIntent = Intent()
            val shareText = txtShareText.text.toString()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                shareText)
            startActivity(Intent.createChooser(shareIntent, "Send To"))

        }

        var gid = activity?.intent?.getStringExtra("groupId") //TODO
        if (gid == null) gid = "123"

        loadQR(gid,v)

        btnReloadQr.setOnClickListener{
            loadQR(gid,v)
        }

        return v
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

    fun loadQR(gid: String,view: View) {
        val txtShareText: EditText = view.findViewById(R.id.shareText)
        val imageView: ImageView = view.findViewById(R.id.imgQr)
        imageView.setImageResource(R.drawable.ic_launcher_background);
        txtShareText.setText("loading...")
        val myurl = context?.getConfigValue("backend_url")
        lifecycleScope.launch {

//            val client = HttpClient(CIO) {
//                install(Auth ) {
//                    basic {
//                        credentials {
//                            BasicAuthCredentials(username = AccountSignIn.creds[0],
//                                password = AccountSignIn.creds[1])
//                        }
//
//                    }
//                }
//            }

            try {
                val response: String = httpClient.post(myurl + "/groups/invites/get_invite") {
                    body = gid.toInt()
                }
                val data = response
                Toast.makeText(activity?.applicationContext,
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
                Toast.makeText(activity?.applicationContext,
                    "Exception: No group with gid 123 or ${E.message}. Loading dummy data.",
                    Toast.LENGTH_LONG).show()
                txtShareText.setText("Please join my group using this invite code: invitecode or click on this link: https://coolrouteplanner.test/join/invitecode")
                qrToImageView(imageView,"https://coolrouteplanner.test/join/invitecode")

            }

        }
    }

}


