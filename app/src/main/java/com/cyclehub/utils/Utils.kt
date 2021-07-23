package com.cyclehub.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.TypedValue
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.cyclehub.R
import com.cyclehub.model.LocationData
import java.io.IOException
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.*


object Utils {
    internal fun getPixels(context: Context, valueInDp: Int): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            valueInDp.toFloat(),
            r.displayMetrics
        )
        return px.toInt()
    }

    internal fun getPixels(context: Context, valueInDp: Float): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, r.displayMetrics)
        return px.toInt()
    }

    internal fun getPixelsSp(context: Context, valueInSp: Int): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            valueInSp.toFloat(),
            r.displayMetrics
        )
        return px.toInt()
    }

    internal fun getPixelsSp(context: Context, valueInSp: Float): Int {
        val r = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, valueInSp, r.displayMetrics)
        return px.toInt()
    }

    fun getIpv4HostAddress(): String {
        NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
            networkInterface.inetAddresses?.toList()?.find {
                !it.isLoopbackAddress && it is Inet4Address
            }?.let { return it.hostAddress }
        }
        return ""
    }

    fun getAddress(context: Context?, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: Address?
        var addressText = ""
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                address = addresses[0]
                addressText = address.getAddressLine(0)
            } else {
                addressText = ""
            }
        }
        return addressText
    }

    suspend fun getBitmap(context: Context, uri: Uri): Bitmap {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(uri)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    fun geoAddress(context: Context, lattitude: Double, longitude: Double): LocationData? {
        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())
        var locationData: LocationData? = null
        try {
            addresses = geocoder.getFromLocation(lattitude, longitude, 1)
            if (addresses.isNotEmpty()) {
                locationData = LocationData(
                    addresses[0].getAddressLine(0),
                    addresses[0].locality,
                    addresses[0].adminArea,
                    addresses[0].postalCode,
                    addresses[0].countryName, lattitude.toString(), longitude.toString()
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return locationData
    }

    fun showEmptyView(
        recyclerView: RecyclerView,
        textView: AppCompatTextView,
        message: String,
        color: Int = R.color.textOnBoarding
    ) {
        recyclerView.isVisible = false
        textView.isVisible = true
        textView.text = message
        textView.setTextColor(ContextCompat.getColor(textView.context, color))
    }

    fun triggerRebirth(context: Context, myClass: Class<*>?) {
        val intent = Intent(context, myClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        Runtime.getRuntime().exit(0)
    }

    fun boldTitleString(boldStr: String, normalStr: String) =
        buildSpannedString {
            bold { append("$boldStr") }
            append("$normalStr")
        }

    fun showImage(context: Context, imageUri: String) {
        val builder = Dialog(context)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        builder.setOnDismissListener {
            //nothing;
        }
        val imageView = ImageView(context)
        imageView.load(imageUri)
        builder.addContentView(
            imageView, RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        builder.show()
    }


}