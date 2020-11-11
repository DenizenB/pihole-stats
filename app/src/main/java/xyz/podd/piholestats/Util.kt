package xyz.podd.piholestats

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.util.TypedValue
import java.security.MessageDigest

fun String.toSHA256(): String {
    val hash = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
    return hash.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

fun Number.dpToPx(context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics)

// https://proandroiddev.com/complex-ui-animation-on-android-8f7a46f4aec4
inline fun getValueAnimator(
    forward: Boolean = true,
    duration: Long,
    interpolator: TimeInterpolator,
    crossinline updateListener: (progress: Float) -> Unit
): ValueAnimator {
    val animator = when (forward) {
        true -> ValueAnimator.ofFloat(0f, 1f)
        else -> ValueAnimator.ofFloat(1f, 0f)
    }

    animator.addUpdateListener { updateListener(it.animatedValue as Float) }
    animator.duration = duration
    animator.interpolator = interpolator
    return animator
}