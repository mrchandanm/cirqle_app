package cirqle.com.DateSpark.Workers


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

import cirqle.com.DateSpark.Models.DateSparkResponseModel
import cirqle.com.DateSpark.Models.DateSparkUserModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Array.get

class UploadUserWorker(context:Context, params:WorkerParameters):Worker(context,params) {
    override fun doWork(): Result {
        var images:ArrayList<String>
        images= ArrayList()
        val imageUris = inputData.getStringArray("imageUris") ?: return Result.failure()
        val relationshipType=inputData.getString("relationshipType")
        val storageRef = FirebaseStorage.getInstance().reference
        val size=imageUris.size
        var i=0
        for (uriString in imageUris) {
            val uri = Uri.parse(uriString)
            val bitmap=Utility.getImageBitmap(uri,applicationContext)
            val compressedImage = Utility.compressImage(bitmap!!)
            val imageName = "image_${System.currentTimeMillis()}.jpg"
            val imageRef = storageRef.child("images/$imageName")
            val uploadTask = imageRef.putBytes(compressedImage).addOnCompleteListener(OnCompleteListener {task ->
                if(task.isSuccessful){
                    imageRef.downloadUrl.addOnSuccessListener { url->
                        images.add(url.toString())
                        i++
                        if(i==size){
                            val collegeName= Utility.getUserDetails(applicationContext)?.collegeName!!
                            val gender= Utility.getUserDetails(applicationContext)?.gender!!
                            val user= Utility.getUserDetails(applicationContext)?._id!!
                            val registerUser = BuilderRetrofit.builService(ApiInterface::class.java)
                            val reqCall=registerUser.datespark_register(DateSparkUserModel(user, relationshipType!!,gender,collegeName,images))
                            reqCall.enqueue(object:Callback<DateSparkResponseModel>{
                                override fun onResponse(
                                    call: Call<DateSparkResponseModel>,
                                    response: Response<DateSparkResponseModel>
                                ) {
                                    Log.d("upload", "onResponse: "+response.body())
                                    Toast.makeText(applicationContext, "upload Succesful", Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(call: Call<DateSparkResponseModel>, t: Throwable) {
                                    Log.d("upload", "onResponse: "+t.message)
                                    Toast.makeText(applicationContext, "upload failed", Toast.LENGTH_SHORT).show()
                                }

                            })
                        }
                    }
                    Log.d("upload", "doWork: "+task.result)
                }
                else{
                    Log.d("upload", "doWork: "+task.result)
                }
            })

        }

        return Result.success()
    }
}