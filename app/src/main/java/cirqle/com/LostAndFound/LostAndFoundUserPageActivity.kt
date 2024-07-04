package cirqle.com.LostAndFound

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import cirqle.com.BuyAndSell.Adapters.ViewPagerPicassoImageAdapter
import cirqle.com.BuyAndSell.Models.BuySellresponseModel
import cirqle.com.Chat.ChattingPageActivity
import cirqle.com.Chat.Models.ChatResponseModel
import cirqle.com.LostAndFound.Models.AddPostResponseModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LostAndFoundUserPageActivity : AppCompatActivity() {
    private lateinit var viewpager_imageshow:ViewPager2
    private lateinit var title_tv:TextView
    private lateinit var location_tv:TextView
    private lateinit var date_tv:TextView
    private lateinit var category_tv:TextView
    private lateinit var phone_tv:TextView
    private lateinit var description_tv:TextView
    private lateinit var call_btn:AppCompatButton
    private lateinit var name_tv:TextView
    private lateinit var chat_btn:ImageView
    private val REQUEST_CALL_PHONE=1
    private lateinit var iCall:Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_and_found_user_page)
        init()

        val UserId= Utility.getUserDetails(this@LostAndFoundUserPageActivity)?._id!!
        val postDetails=intent.getParcelableExtra<AddPostResponseModel>("postDetails")
        val images=intent.getStringArrayListExtra("images") as ArrayList<String>
        if(postDetails!=null){
            title_tv.text=postDetails.title.toString()
            location_tv.text=postDetails.location.toString()
            date_tv.text=postDetails.date.toString()

            category_tv.text=postDetails.category.toString()
            phone_tv.text=postDetails.user.phone.toString()
            description_tv.text=postDetails.description.toString()
            name_tv.text=postDetails.user.name.toString()

            val adapter = ViewPagerPicassoImageAdapter(this, images )
            viewpager_imageshow.adapter = adapter
        }
        val phoneNumber = postDetails?.user?.phone.toString() // Replace with the phone number you want to call
        iCall = Intent(Intent.ACTION_VIEW,Uri.parse("tel:$phoneNumber"))
        call_btn.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                // Permission is already granted, proceed with the call
                startActivity(iCall)
            } else {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE)
            }

        }

        chat_btn.setOnClickListener{
            val getmessageService = BuilderRetrofit.builService(ApiInterface::class.java)
            val reqCall=getmessageService.accessChat(UserId,postDetails?.user?._id.toString())
            reqCall.enqueue(object: Callback<ChatResponseModel> {
                override fun onResponse(call: Call<ChatResponseModel>, response: Response<ChatResponseModel>) {
                    val intent= Intent(this@LostAndFoundUserPageActivity, ChattingPageActivity::class.java)

                    intent.putExtra("postUserId",postDetails?.user?._id.toString())
                    intent.putExtra("chatId",response.body()?._id.toString())
                    intent.putExtra("userName",postDetails?.user?.userName.toString())
                    startActivity(intent)
                }

                override fun onFailure(call: Call<ChatResponseModel>, t: Throwable) {
                    Toast.makeText(this@LostAndFoundUserPageActivity, "failed to access chat", Toast.LENGTH_SHORT).show()
                    Log.d("accessChat", "onFailure: "+t.message)
                }
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==REQUEST_CALL_PHONE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the call
                startActivity(iCall)
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun init(){
        viewpager_imageshow=findViewById(R.id.viewpager_imageshow)
        title_tv=findViewById(R.id.title_tv)
        location_tv=findViewById(R.id.location_tv)
        date_tv=findViewById(R.id.date_tv)
        category_tv=findViewById(R.id.category_tv)
        phone_tv=findViewById(R.id.phone_tv)
        description_tv=findViewById(R.id.description_tv)
        call_btn=findViewById(R.id.call_btn)
        name_tv=findViewById(R.id.name_tv)
        chat_btn=findViewById(R.id.chat_btn)

    }
}