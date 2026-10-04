package com.app_rickmorty.data.local



import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Convierte la foto elegida por el usuario en un String Base64 pequeño,
 * listo para guardar como campo de un documento de Firestore.
 */
object ImageUtils {

    private const val MAX_SIZE_PX = 512 // lado máximo del cuadrado de la foto
    private const val JPEG_QUALITY = 70

    fun uriToBase64(context: Context, uri: Uri): String? {
        val bitmap = uriToBitmap(context, uri) ?: return null
        val resized = resizeBitmap(bitmap, MAX_SIZE_PX)
        val output = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)
        return Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)
    }

    fun base64ToBitmap(base64: String): Bitmap? {
        return try {
            val bytes = Base64.decode(base64, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            null
        }
    }

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream)
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (width <= maxSize && height <= maxSize) return bitmap

        val ratio = width.toFloat() / height.toFloat()
        val (newWidth, newHeight) = if (width > height) {
            maxSize to (maxSize / ratio).toInt()
        } else {
            (maxSize * ratio).toInt() to maxSize
        }
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}