package com.cyclehub.ui.invoice

import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.cyclehub.databinding.FragmentInvoiceBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class InvoiceFragment : Fragment() {
    private lateinit var binding: FragmentInvoiceBinding
    val args: InvoiceFragmentArgs by navArgs()

    companion object {
        const val REQUEST_PERMISSION = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvoiceBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webview.apply {
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            loadUrl("https://l0hzhr3dwe.execute-api.ap-south-1.amazonaws.com/default/pdf?order_id=${args.orderId}")
        }
        binding.savePdf.setOnClickListener {
            saveMediaToStorage()
        }
    }



    fun saveMediaToStorage() {
        val b = Bitmap.createBitmap(
            binding.webview.width,
            binding.webview.height, Bitmap.Config.ARGB_8888
        );
        val c = Canvas(b)
        binding.webview.draw(c);
        val d = BitmapDrawable(resources, b)
        //Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            context?.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/eCycleHub"
                    )
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/eCycleHub")
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            d.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(requireContext(), "Saved to Photos/eCycleHub", Toast.LENGTH_SHORT).show()
        }
    }

}