package it.giovanni.kotlin.fragments.homepage

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.giovanni.kotlin.BuildConfig
import it.giovanni.kotlin.fragments.HomeFragment
import it.giovanni.kotlin.fragments.MainFragment
import it.giovanni.kotlin.utils.Utils.Companion.getRoundBitmap
import it.giovanni.kotlin.R
import it.giovanni.kotlin.utils.Utils.Companion.convertDpToPixel
import it.giovanni.kotlin.utils.Utils.Companion.getHashKey
import it.giovanni.kotlin.utils.Utils.Companion.getVersionNameLong
import kotlinx.android.synthetic.main.home_page_layout.*
import java.text.SimpleDateFormat
import java.util.*

class HomePageFragment : HomeFragment() {

    /*
    - Click on Gradle (from right side panel)
    - Click on your project
    - Click on Tasks
    - Click on Android
    - Double click on signingReport
    You will get SHA1 and MD5 in Run Tab:

    Variant: debug
    Config: debug
    Store: /Users/Giovanni/.android/debug.keystore
    Alias: AndroidDebugKey
    MD5: C3:9B:CE:AC:C2:C5:4B:4C:6C:24:56:F3:17:73:37:C1
    SHA1: 03:29:32:E7:87:94:51:CA:67:F5:33:0E:53:50:BD:69:66:2F:F0:B0
    SHA-256: ED:C1:9D:E9:CD:57:86:E6:1B:83:B0:28:39:99:32:0C:FF:A1:C0:25:68:DA:E4:95:3A:CD:94:DA:65:73:D8:37
    Valid until: mercoledì 13 febbraio 2047
    */

    private val GALLERY_CODE = 201
    private val DELAY_TIME: Long = 3000
    private var viewFragment: View? = null

    override fun getLayout(): Int {
        return R.layout.home_page_layout
    }

    override fun getTitle(): Int {
        return NO_TITLE
    }

    companion object {
        var caller: MainFragment? = null
        fun newInstance(c: MainFragment): HomePageFragment {
            caller = c
            return HomePageFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = super.onCreateView(inflater, container, savedInstanceState)
        return viewFragment
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = Date()

        val dayOfWeek = SimpleDateFormat("EEEE").format(date).substring(0, 1).toUpperCase() +
                SimpleDateFormat("EEEE").format(date).substring(1, SimpleDateFormat("EEEE").format(date).length)
        day.text = dayOfWeek

        val currentMonth = SimpleDateFormat("dd MMMM").format(date).substring(0, 3) +
                SimpleDateFormat("dd MMMM").format(date).substring(3, 4).toUpperCase() +
                SimpleDateFormat("dd MMMM").format(date).substring(4, SimpleDateFormat("dd MMMM").format(date).length)
        month.text = currentMonth

        year.text = SimpleDateFormat("yyyy").format(date)

        Handler().postDelayed({
            val avatar: Bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.giovanni)
            val roundAvatar: Bitmap = getRoundBitmap(avatar, avatar.width)
            ico_avatar.setImageBitmap(roundAvatar)
        }, DELAY_TIME)

        ico_avatar.setOnClickListener {
            pickFromGallery()
        }

        val pixel = convertDpToPixel(context!!, 24F)
        Log.i("HOMEPAGETAG", "pixel: $pixel")

        val versionName = BuildConfig.VERSION_NAME
        Log.i("HOMEPAGETAG", "versionName: " + getVersionNameLong(versionName))

        val hashKey = getHashKey(context!!)
        Log.i("HOMEPAGETAG", "Hash Key: $hashKey")
    }

    private fun pickFromGallery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, GALLERY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) run {
            try {
                if (data.data != null) {
                    val avatar: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, data.data)
                    val roundAvatar: Bitmap = getRoundBitmap(avatar, avatar.width)
                    ico_avatar.setImageBitmap(roundAvatar)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}