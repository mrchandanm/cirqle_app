package cirqle.com.Chat

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.Authentication.Models.UserModel
import cirqle.com.Chat.Adapters.MessageAdapter
import cirqle.com.Chat.Models.ChatModel
import cirqle.com.Chat.Models.MessageResponseModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import cirqle.com.customLayout.CircularImageView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import io.socket.client.IO
import io.socket.emitter.Emitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException

class ChattingPageActivity : AppCompatActivity() {
    private lateinit var message_Rv:RecyclerView
    private lateinit var name_tv:TextView
    private lateinit var send_message_et:EditText
    private lateinit var send_btn:LottieAnimationView
    private lateinit var adapter:MessageAdapter
    private lateinit var profilePicIv:CircularImageView
    private  var messageList= mutableListOf<MessageResponseModel>()

    private lateinit var mSocket:io.socket.client.Socket
    private  var gson:Gson=Gson()
    private lateinit var chatId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_page)


//        var messageList = mutableListOf<MessageResponseModel>()
        init()
        FirebaseMessaging.getInstance().token.addOnCompleteListener{task->

            Log.d("token", "onCreate: "+task.result)

        }





        val userName=intent.getStringExtra("userName")
         chatId=intent.getStringExtra("chatId")!!
        val profilePic=intent.getStringExtra("profilePic")
        name_tv.text=userName.toString()
        profilePicIv=findViewById(R.id.profilePic)
        if(!profilePic.isNullOrBlank()) {
            Picasso.get().load(profilePic).into(profilePicIv)
        }

        val userId= Utility.getUserDetails(this@ChattingPageActivity)?._id!!
        adapter = MessageAdapter(messageList,userId )

        try {
            mSocket = IO.socket("https://cirqle-backend.onrender.com")
        } catch (e: URISyntaxException) {
        }
        mSocket.connect()
        mSocket.emit("join-chat", chatId)


        val getmessageService = BuilderRetrofit.builService(ApiInterface::class.java)
        val reqCall=getmessageService.getallMessages(chatId!!)
        reqCall.enqueue(object: Callback<ArrayList<MessageResponseModel>>{
            override fun onResponse(call: Call<ArrayList<MessageResponseModel>>, response: Response<ArrayList<MessageResponseModel>>) {
                messageList=response.body()!!
                val layoutManager = LinearLayoutManager(this@ChattingPageActivity)
                layoutManager.stackFromEnd = true
                message_Rv.layoutManager = layoutManager
                 adapter = MessageAdapter(messageList,userId )

                message_Rv.adapter = adapter
            }

            override fun onFailure(call: Call<ArrayList<MessageResponseModel>>, t: Throwable) {
                Log.d("getallmessages", "onFailure: "+t.message)
            }
        })


        mSocket.on("message received", data)



       send_btn.setOnClickListener{
           val message=send_message_et.text.toString()
           send_message_et.text.clear()
           send_btn.playAnimation()
           val user=Utility.getUserDetails(this@ChattingPageActivity)
           var usersList:ArrayList<UserModel> = ArrayList()
           usersList.add(user!!)
           usersList.add(user!!)
           messageList.add(MessageResponseModel(user!!,message, ChatModel(usersList,message)))
           adapter.notifyDataSetChanged()
           // Scroll to the last item
           message_Rv.scrollToPosition(adapter.itemCount - 1)

           val sendmessageService = BuilderRetrofit.builService(ApiInterface::class.java)
           val sendreqCall=sendmessageService.sendMessage(message,chatId,userId)
           sendreqCall.enqueue(object :Callback<MessageResponseModel>{
               override fun onResponse(call: Call<MessageResponseModel>, response: Response<MessageResponseModel>
               ) {
                   if (response.isSuccessful) {
                       var res=Gson().toJson(response.body(),MessageResponseModel::class.java)
                       mSocket.emit("newMessage",res)

//                       // Add the new message to the list
//                       val message = response.body()
//                       message?.let {
//                           messageList.add(it)
//                           // Notify the adapter that the dataset has changed
//                           adapter.notifyDataSetChanged()
//                           // Scroll to the last item
//                           message_Rv.scrollToPosition(adapter.itemCount - 1)
//                       }
                   } else {
                       // Handle unsuccessful response
                   }
               }

               override fun onFailure(call: Call<MessageResponseModel>, t: Throwable) {
                   Log.d("send", "onFailure: "+t.message)
               }
           })
       }





    }

    val data = Emitter.Listener { args ->
        runOnUiThread {
            val data = args[0] as String
            Log.d("data2", ": "+data)
           val jsonObject = JsonParser.parseString(data).asJsonObject
           val messageResponseModel = Gson().fromJson(jsonObject , MessageResponseModel::class.java)
            messageResponseModel?.let {
                messageList.add(it)
                // Notify the adapter that the dataset has changed
                adapter.notifyDataSetChanged()
                // Scroll to the last item
                message_Rv.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
    fun init(){
        message_Rv=findViewById(R.id.message_Rv)
        name_tv=findViewById(R.id.name_tv)
        send_message_et=findViewById(R.id.send_message_et)
        send_btn=findViewById(R.id.send_btn)
    }

    override fun onBackPressed() {
        mSocket.emit("leave-room",chatId)
        super.onBackPressed()
    }
}