package com.devs.carpoolrouteplanner.ui.qrinvite

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
import androidx.lifecycle.lifecycleScope
import com.devs.carpoolrouteplanner.R
import com.devs.carpoolrouteplanner.utils.getConfigValue
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
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
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invite_qr, container, false)
        //code
        val buttonShare: Button = btnShare
        val txtShareText: EditText = shareText
        val btnReloadQr: Button  = btnReloadQr
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
        val txtShareText: EditText = shareText
        val imageView: ImageView = imgQr
        imageView.setImageResource(ic_launcher_background);
        txtShareText.setText("loading...")
        val myurl = context?.getConfigValue("backend_url")
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

}


