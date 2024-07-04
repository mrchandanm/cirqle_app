package cirqle.com.SeniorCirqle.Workers

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import cirqle.com.DateSpark.Models.DateSparkResponseModel
import cirqle.com.DateSpark.Models.DateSparkUserModel
import cirqle.com.SeniorCirqle.DoubtCirqleInterface
import cirqle.com.SeniorCirqle.Models.AddDoubtModel
import cirqle.com.SeniorCirqle.Models.AddDoubtResponseModel
import cirqle.com.SeniorCirqle.Models.OptionModel
import cirqle.com.SeniorCirqle.SeniorCirqleHomeActivity
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDoubtWorker(context: Context, params: WorkerParameters ): Worker(context,params) {
    override fun doWork(): Result {
        var images:ArrayList<String>
        images= ArrayList()

        val imageUris = inputData.getStringArray("imageUris") ?: return Result.failure()
        val question=inputData.getString("question")
        val category=inputData.getString("category")
        val isAnonymos=inputData.getString("isAnonymos")
        val optionsJson = inputData.getString("options")
        val optionsList = Gson().fromJson(optionsJson, Array<OptionModel>::class.java)?.toMutableList() ?: emptyList()
        val options:ArrayList<OptionModel> = ArrayList(optionsList)
        val storageRef = FirebaseStorage.getInstance().reference
        val size=imageUris.size
        var i=0
        if(imageUris.isEmpty()){
            val collegeName= Utility.getUserDetails(applicationContext)?.collegeName!!
            val user= Utility.getUserDetails(applicationContext)?._id!!
            val addDoubts = BuilderRetrofit.builService(ApiInterface::class.java)
            val reqCall=addDoubts.add_doubts(AddDoubtModel(question!!,category!!, isAnonymos!!,user,collegeName,options,images))
            reqCall.enqueue(object :Callback<AddDoubtResponseModel>{
                override fun onResponse(call: Call<AddDoubtResponseModel>, response: Response<AddDoubtResponseModel>) {
                    Log.d("doubts", "onResponse: "+response.body())
                    Toast.makeText(applicationContext, "Uploaded Succesfully", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<AddDoubtResponseModel>, t: Throwable) {
                    Log.d("doubts", "onResponse: "+t.message)
                }
            })
        }
        else {
            for (uriString in imageUris) {
                val uri = Uri.parse(uriString)
                val bitmap=Utility.getImageBitmap(uri,applicationContext)
                val compressedImage = Utility.compressImage(bitmap!!)
                val imageName = "image_${System.currentTimeMillis()}.jpg"
                val imageRef = storageRef.child("images/$imageName")

                val uploadTask =
                    imageRef.putBytes(compressedImage).addOnCompleteListener(OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            imageRef.downloadUrl.addOnSuccessListener { url ->
                                images.add(url.toString())
                                i++
                                if (i == size) {
                                    val collegeName =
                                        Utility.getUserDetails(applicationContext)?.collegeName!!
                                    val user = Utility.getUserDetails(applicationContext)?._id!!
                                    val addDoubts =
                                        BuilderRetrofit.builService(ApiInterface::class.java)
                                    val reqCall = addDoubts.add_doubts(
                                        AddDoubtModel(
                                            question!!,
                                            category!!,
                                            isAnonymos!!,
                                            user,
                                            collegeName,
                                            options,
                                            images
                                        )
                                    )
                                    reqCall.enqueue(object : Callback<AddDoubtResponseModel> {
                                        override fun onResponse(
                                            call: Call<AddDoubtResponseModel>,
                                            response: Response<AddDoubtResponseModel>
                                        ) {
                                            Log.d("doubts", "onResponse: " + response.body())
                                            Toast.makeText(
                                                applicationContext,
                                                "Uploaded Succesfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        override fun onFailure(
                                            call: Call<AddDoubtResponseModel>,
                                            t: Throwable
                                        ) {
                                            Log.d("doubts", "onResponse: " + t.message)
                                        }
                                    })
                                }
                            }
                            Log.d("upload", "doWork: " + task.result)
                        } else {
                            Log.d("upload", "doWork: " + task.result)
                        }
                    })

            }
        }

        return Result.success()
    }
}