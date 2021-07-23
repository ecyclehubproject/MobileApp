package com.cyclehub.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.TransitionManager
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.cyclehub.R
import com.cyclehub.db.AppDatabase
import com.cyclehub.other.CircleCropTransformation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream


fun FragmentActivity.addFragment(container: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .add(container, fragment)
        .commitAllowingStateLoss()
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun AppCompatImageView.loadSvg(url: Int) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}

fun AppCompatImageView.loadSvgCircle(url: Int) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvgCircle.context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(R.drawable.ic_logo_without_text_svg)
        .data(url).error(R.drawable.ic_logo_without_text_svg)
        .transformations(
            CircleCropTransformation(
                CircleCropTransformation.Stroke(
                    1f,
                    R.color.textOnBoarding
                ),


                )
        )
        .target(this)
        .build()
    imageLoader.enqueue(request)
}

fun AppCompatImageView.loadSvgFromUrlGreenBorder(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvgFromUrlGreenBorder.context)) }
        .build()


    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(R.drawable.ic_logo_without_text_svg)
        .data(url).error(R.drawable.ic_logo_without_text_svg)
        .transformations(
            CircleCropTransformation(
                CircleCropTransformation.Stroke(
                    1f,
                    R.color.textOnBoarding
                )
            )
        ).size(50, 50)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}

fun AppCompatImageView.loadCircleImage(url: Any) {
    val imageLoader = ImageLoader.Builder(this.context)
        .build()
    this.scaleType = ImageView.ScaleType.CENTER
    this.setPadding(8, 8, 8, 8)
    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(R.drawable.ic_logo_without_text_svg)
        .data(url).error(R.drawable.ic_logo_without_text_svg)
        .transformations(
            CircleCropTransformation(
                CircleCropTransformation.Stroke(
                    1f,
                    R.color.textOnBoarding
                )
            )
        ).size(50, 50)
        .target(this)
        .build()
    imageLoader.enqueue(request)
}

fun AppCompatImageView.loadSvgFromUrl(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvgFromUrl.context)) }
        .build()
    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .scale(Scale.FILL)
        .data(url).error(R.drawable.ic_placeholder_logo).apply {
            transformations(CircleCropTransformation())
        }
        .transformations(CircleCropTransformation())
        .target(this)
        .build()

    imageLoader.enqueue(request)
}
fun AppCompatImageView.loadAnyUrl(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadAnyUrl.context)) }
        .build()
    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .scale(Scale.FILL)
        .data(url).error(R.mipmap.ic_launcher)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}
fun Drawable.tint(context: Context, @ColorRes color: Int) {
    DrawableCompat.setTint(this, ContextCompat.getColor(context, color))
}

fun View.snackMessage(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun TextView.colorSpannableStringWithUnderLineOneStart(
    prefixString: String,
    postfixString: String,
    callback: (Int) -> Unit
) {
    val spanTxt = SpannableStringBuilder()
    spanTxt.append("$prefixString ")
    spanTxt.append("$postfixString")
    spanTxt.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            callback(0)
            widget.invalidate()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = ContextCompat.getColor(context, R.color.buttonOnBoarding)
            ds.isUnderlineText = true
        }
    }, 0, prefixString.length, spanTxt.length)
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spanTxt, TextView.BufferType.SPANNABLE)
}

fun TextView.colorSpannableStringWithGrayOneStart(
    prefixString: String,
    postfixString: String,
    callback: (Int) -> Unit
) {
    val spanTxt = SpannableStringBuilder()
    spanTxt.append("$prefixString")
    spanTxt.append("$postfixString")
    spanTxt.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            callback(0)
            widget.invalidate()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = ContextCompat.getColor(context, R.color.colorBlack)
        }
    }, prefixString.length, spanTxt.length, 0)
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spanTxt, TextView.BufferType.SPANNABLE)
}

fun TextView.colorSpannableStringWithUnderLineOneEnd(
    prefixString: String,
    postfixString: String,
    callback: (Int) -> Unit
) {
    val spanTxt = SpannableStringBuilder()
    spanTxt.append("$prefixString")
    spanTxt.append("$postfixString")
    spanTxt.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            callback(0)
            widget.invalidate()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = ContextCompat.getColor(context, R.color.buttonOnBoarding)
            ds.isUnderlineText = true
        }
    }, prefixString.length, spanTxt.length, 0)
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spanTxt, TextView.BufferType.SPANNABLE)
}

fun Context.fetchColor(id: Int): Int = ContextCompat.getColor(this, id)

fun String.toEditable(): Editable? = Editable.Factory.getInstance().newEditable(this)

fun Bitmap.bitMapToString(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val b: ByteArray = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

val itemOnClick: (View, Int, Int) -> Unit = { view, position, type ->
}

fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(it, adapterPosition, itemViewType)
    }
    return this
}

fun BottomNavigationView.show() {
    try {
        if (visibility == View.VISIBLE) return

        val parent = parent as ViewGroup
        // View needs to be laid out to create a snapshot & know position to animate. If view isn't
        // laid out yet, need to do this manually.
        if (!isLaidOut) {
            measure(
                View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.AT_MOST)
            )
            layout(parent.left, parent.height - measuredHeight, parent.right, parent.height)
        }

        val drawable = BitmapDrawable(context.resources, drawToBitmap())
        drawable.setBounds(left, parent.height, right, parent.height + height)
        parent.overlay.add(drawable)
        ValueAnimator.ofInt(parent.height, top).apply {
            startDelay = 100L
            duration = 300L
            interpolator = AnimationUtils.loadInterpolator(
                context,
                android.R.interpolator.linear_out_slow_in
            )
            addUpdateListener {
                val newTop = it.animatedValue as Int
                drawable.setBounds(left, newTop, right, newTop + height)
            }
            doOnEnd {
                parent.overlay.remove(drawable)
                visibility = View.VISIBLE
            }
            start()
        }
    } catch (e: Exception) {
    }
}

/**
 * Potentially animate hiding a [BottomNavigationView].
 *
 * Abruptly changing the visibility leads to a re-layout of main content, animating
 * `translationY` leaves a gap where the view was that content does not fill.
 *
 * Instead, take a snapshot, instantly hide the view (so content lays out to fill), then animate
 * out the snapshot.
 */
fun BottomNavigationView.hide() {
    if (visibility == View.GONE) return

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    val parent = parent as ViewGroup
    drawable.setBounds(left, top, right, bottom)
    parent.overlay.add(drawable)
    visibility = View.GONE
    ValueAnimator.ofInt(top, parent.height).apply {
        startDelay = 100L
        duration = 200L
        interpolator = AnimationUtils.loadInterpolator(
            context,
            android.R.interpolator.fast_out_linear_in
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
        }
        start()
    }
}

fun View.setOnSingleClickListener(skipDurationMillis: Long = 250, action: () -> Unit) {
    var isEnabled = true
    this.setOnClickListener {
        if (isEnabled) {
            action()
            isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({ isEnabled = true }, skipDurationMillis)
        }
    }
}

fun View.slideVisibility(visibility: Boolean, durationTime: Long = 300) {
    val transition = Slide(Gravity.BOTTOM)
    transition.apply {
        duration = durationTime
        addTarget(this@slideVisibility)
    }
    TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
    this.isVisible = visibility
}

fun Fragment.emptyDatabase() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        withContext(Dispatchers.IO) {
            AppDatabase.getDatabase(requireActivity()).clearAllTables()
        }
    }
}

fun rotateImageIfRequired(context: Context, img: Bitmap, selectedImage: Uri): Bitmap? {
    val input: InputStream? = context.contentResolver.openInputStream(selectedImage)
    val ei: ExifInterface = if (Build.VERSION.SDK_INT > 23) input?.let { ExifInterface(it) }!! else selectedImage.getPath()?.let { ExifInterface(it) }!!
    return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
        else -> img
    }
}
private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
}


