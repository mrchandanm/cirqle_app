package cirqle.com.Utils

import android.util.Log
import cirqle.com.Authentication.Models.FcmResponseModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("fcmm", "onMessageReceived: "+message.notification?.body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("fcm", "onNewToken: "+token)
        val userId=Utility.getUserDetails(applicationContext)?._id
        val reqCall= BuilderRetrofit.builService(ApiInterface::class.java).update_fcm(userId!!,token)
        reqCall.enqueue(object :Callback<FcmResponseModel>{
            override fun onResponse(call: Call<FcmResponseModel>, response: Response<FcmResponseModel>) {
                Log.d("ufcm", "onResponse: "+response.body())
            }

            override fun onFailure(call: Call<FcmResponseModel>, t: Throwable) {
                Log.d("ufcm", "onResponse: "+t.message)
            }

        })
    }
}