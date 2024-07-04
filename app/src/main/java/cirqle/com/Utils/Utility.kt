package cirqle.com.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import cirqle.com.Authentication.Models.UserModel
import cirqle.com.CirqleStore.Models.CartModelToSaveLocally
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object Utility {
    fun getTimeAgoString(createdAt: Date): String {
        val createdAtDateTime = createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val currentDateTime = LocalDateTime.now()
        val duration = Duration.between(createdAtDateTime, currentDateTime)

        return when {
            duration.seconds < 60 -> "few seconds ago"
            duration.toMinutes() == 1L -> "1 minute ago"
            duration.toMinutes() < 60 -> "${duration.toMinutes()} minutes ago"
            duration.toHours() == 1L -> "1 hour ago"
            duration.toHours() < 24 -> "${duration.toHours()} hours ago"
            duration.toDays() == 1L -> "1 day ago"
            else -> "${duration.toDays()} days ago"
        }
    }
    fun saveObjectLocally( context :Context, key:String, obj:Any){
        val gson = Gson()
        val json = gson.toJson(obj)
        val sharedPreferences = context.getSharedPreferences("cirqle", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, json)
        editor.apply()
    }
    fun getObjectLocally(context: Context, key: String, type: Type): Any? {
        val sharedPreferences = context.getSharedPreferences("cirqle", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(key, null)
        val gson = Gson()
        return gson.fromJson(json,type)
    }

    fun getUserDetails(context: Context):UserModel?{
        val sharedPreferences = context.getSharedPreferences("cirqle", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("userDetails", null)
        val gson = Gson()
        return gson.fromJson(json,UserModel::class.java)
    }
    fun getCarts(context: Context):CartModelToSaveLocally?{
        val sharedPreferences = context.getSharedPreferences("cirqle", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("cart", null)
        val gson = Gson()
        return gson.fromJson(json,CartModelToSaveLocally::class.java)
    }
    fun saveStringLocally(context: Context,key:String,data:String){
        val sharedPreferences = context.getSharedPreferences("cirqle", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, data)
        editor.apply()
    }
    fun getStringLocally(context: Context,key:String):String?{
        val sharedPreferences = context.getSharedPreferences("cirqle", Context.MODE_PRIVATE)
        val data = sharedPreferences.getString(key, null)
        return data
    }
    fun deleteDataLocally(context: Context,key:String){
        val sharedPreferences = context.getSharedPreferences("cirqle", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
    fun compressImage(image: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream) // adjust quality as needed
        return byteArrayOutputStream.toByteArray()
    }

     fun getImageBitmap(imageUri: Uri, context: Context): Bitmap? {
        return context.contentResolver.openInputStream(imageUri)?.use {
            BitmapFactory.decodeStream(it)
        }
    }
}
