package cirqle.com.LostAndFound.Workers

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import cirqle.com.LostAndFound.FoundItemFragment
import cirqle.com.LostAndFound.Models.AddPostModel
import cirqle.com.LostAndFound.Models.AddPostResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostUploadWorker(context: Context, params: WorkerParameters): Worker(context,params)  {
    override fun doWork(): Result {
        var images:ArrayList<String>
        images= ArrayList()
        val imageUris = inputData.getStringArray("imageUris") ?: return Result.failure()
        val title=inputData.getString("title")
        val description=inputData.getString("description")
        val category=inputData.getString("category")
        val location=inputData.getString("location")
        val date=inputData.getString("date")
        val userId= Utility.getUserDetails(applicationContext)?._id
        val collegeName= Utility.getUserDetails(applicationContext)?.collegeName

        val storageRef = FirebaseStorage.getInstance().reference
        val size=imageUris.size
        var i=0
        for (uriString in imageUris) {
            val uri = Uri.parse(uriString)
            val bitmap=Utility.getImageBitmap(uri,applicationContext)
            val compressedImage = Utility.compressImage(bitmap!!)
            val imageName = "image_${System.currentTimeMillis()}.jpg"
            val imageRef = storageRef.child("images/$imageName")
            val uploadTask = imageRef.putBytes(compressedImage).addOnCompleteListener(OnCompleteListener { task ->
                if(task.isSuccessful){
                    imageRef.downloadUrl.addOnSuccessListener { url->
                        images.add(url.toString())
                        i++
                        if(i==size){
                            val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).add_lost_found_post(
                                AddPostModel(category!!,userId!!,title!!,description!!,collegeName!!,location!!,date!!,images))
                            reqCall.enqueue(object :Callback<AddPostResponseModel>{
                                override fun onResponse(call: Call<AddPostResponseModel>, response: Response<AddPostResponseModel>) {

                                    Toast.makeText(applicationContext, "uploaded", Toast.LENGTH_SHORT).show()
                                    Log.d("laf", "onResponse: "+response.body())
                                }

                                override fun onFailure(call: Call<AddPostResponseModel>, t: Throwable) {
                                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
                                    Log.d("laf", "onFailure: "+t.message)
                                }
                            })
                        }
                    }
                    Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_SHORT).show()
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