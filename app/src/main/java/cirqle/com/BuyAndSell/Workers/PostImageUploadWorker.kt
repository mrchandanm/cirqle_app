package cirqle.com.BuyAndSell.Workers

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Worker
import androidx.work.WorkerParameters
import cirqle.com.BuyAndSell.Models.BuyAndSellPostModel
import cirqle.com.BuyAndSell.Models.PostResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage
import org.bson.types.ObjectId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostImageUploadWorker(context:Context, params:WorkerParameters):Worker(context,params) {
    override fun doWork(): Result {
        var images:ArrayList<String>
        images= ArrayList()
        val imageUris = inputData.getStringArray("imageUris") ?: return Result.failure()
        val price=Integer.valueOf(inputData.getString("price")!!)
        val title=inputData.getString("title")
        val description=inputData.getString("description")
        val category=inputData.getString("category")
        val negotiable=inputData.getString("negotiable")
        val owner=inputData.getString("owner")
        val collegeName=inputData.getString("collegeName")

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
                            val postService = BuilderRetrofit.builService(ApiInterface::class.java)
                            val reqCall=postService.PostAds(BuyAndSellPostModel(price,title!!,description!!,category!!,negotiable!!,owner!!,collegeName!!,images))
                            reqCall.enqueue(object : Callback<PostResponseModel>{
                                override fun onResponse(call: Call<PostResponseModel>, response: Response<PostResponseModel>
                                ) {
                                    if(response.isSuccessful){
                                        Log.d("upload", "onResponse: "+response.body())
                                        Toast.makeText(applicationContext, "Uploaded Succesful", Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        Log.d("failed", "onResponse: "+response.body())
                                    }

                                }

                                override fun onFailure(call: Call<PostResponseModel>, t: Throwable
                                ) {
                                    Log.d("upload", "onFailure: "+t.message)
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