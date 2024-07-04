package cirqle.com.BuyAndSell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import cirqle.com.BuyAndSell.Adapters.ImageShowViewPagerAdapter
import cirqle.com.BuyAndSell.Adapters.ViewPagerPicassoImageAdapter
import cirqle.com.BuyAndSell.Models.BuySellresponseModel
import cirqle.com.BuyAndSell.Models.GetPostResponseModel
import cirqle.com.Chat.ChattingPageActivity
import cirqle.com.Chat.Models.ChatResponseModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPostPageActivity : AppCompatActivity() {
    private lateinit var price_tv:TextView
    private lateinit var title_tv:TextView
    private lateinit var location_tv:TextView
    private lateinit var date_tv:TextView
    private lateinit var negotiable_tv:TextView
    private lateinit var phone_tv:TextView
    private lateinit var description_tv:TextView
    private lateinit var name_tv:TextView
    private lateinit var chat_btn:ImageView
    private lateinit var viewpager_imageshow:ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_post_page)
        init()
        val UserId= Utility.getUserDetails(this@UserPostPageActivity)?._id!!
        val postDetails=intent.getParcelableExtra<BuySellresponseModel>("postDetails")
        val images=intent.getStringArrayListExtra("images") as ArrayList<String>
        if(postDetails!=null){
            val rupeeSymbol = "\u20B9"
            price_tv.text=rupeeSymbol+postDetails.price.toString()
            title_tv.text=postDetails.title.toString()
            location_tv.text=postDetails.owner.hostelName.toString()

            negotiable_tv.text=postDetails.negotiable.toString()
            phone_tv.text=postDetails.owner.phone.toString()
            description_tv.text=postDetails.description.toString()
            name_tv.text=postDetails.owner.name.toString()


            val adapter = ViewPagerPicassoImageAdapter(this, images )
            viewpager_imageshow.adapter = adapter

        }

        chat_btn.setOnClickListener{

            val getmessageService = BuilderRetrofit.builService(ApiInterface::class.java)
            val reqCall=getmessageService.accessChat(UserId,postDetails?.owner?._id.toString())
            reqCall.enqueue(object: Callback<ChatResponseModel>{
                override fun onResponse(call: Call<ChatResponseModel>, response: Response<ChatResponseModel>) {
                    val intent=Intent(this@UserPostPageActivity,ChattingPageActivity::class.java)

                    intent.putExtra("postUserId",postDetails?.owner?._id.toString())
                    intent.putExtra("chatId",response.body()?._id.toString())
                    intent.putExtra("userName",postDetails?.owner?.userName.toString())
                    startActivity(intent)
                }

                override fun onFailure(call: Call<ChatResponseModel>, t: Throwable) {
                    Log.d("accessChat", "onFailure: "+t.message)
                }
            })

        }
    }

    fun init(){
        price_tv=findViewById(R.id.price_tv)
        title_tv=findViewById(R.id.title_tv)
        location_tv=findViewById(R.id.location_tv)
        date_tv=findViewById(R.id.date_tv)
        negotiable_tv=findViewById(R.id.negotiable_tv)
        phone_tv=findViewById(R.id.phone_tv)
        description_tv=findViewById(R.id.description_tv)
        name_tv=findViewById(R.id.name_tv)
        chat_btn=findViewById(R.id.chat_btn)
        viewpager_imageshow=findViewById(R.id.viewpager_imageshow)
    }
}