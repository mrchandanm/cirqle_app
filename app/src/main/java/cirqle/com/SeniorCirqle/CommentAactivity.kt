package cirqle.com.SeniorCirqle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.Chat.Models.MessageResponseModel
import cirqle.com.R
import cirqle.com.SeniorCirqle.Adapters.CommentAdapter
import cirqle.com.SeniorCirqle.Models.AddCommentModel
import cirqle.com.SeniorCirqle.Models.AddCommentResponseModel
import cirqle.com.SeniorCirqle.Models.GetCommentResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class CommentAactivity : AppCompatActivity() {
    private lateinit var comment_rv:RecyclerView
    private lateinit var comment_et:EditText
    private lateinit var send_btn:AppCompatButton
    private lateinit var adapter:CommentAdapter
    private  var commentList=mutableListOf<AddCommentResponseModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_aactivity)
        init()

        adapter=CommentAdapter(this@CommentAactivity,commentList)
        val postId=intent.getStringExtra("postId")
        val userId=intent.getStringExtra("userId")



        val reqCall = BuilderRetrofit.builService(ApiInterface::class.java).get_comments(postId!!)
        reqCall.enqueue(object :Callback<GetCommentResponseModel>{
            override fun onResponse(call: Call<GetCommentResponseModel>, response: Response<GetCommentResponseModel>) {
                Toast.makeText(this@CommentAactivity, "done", Toast.LENGTH_SHORT).show()
                Log.d("comment", "onResponse: "+response.body()?.comment)
                commentList= response.body()?.comment!!
               adapter= CommentAdapter(this@CommentAactivity,commentList!!)
                val layoutManager = LinearLayoutManager(this@CommentAactivity)
                layoutManager.stackFromEnd = true
                comment_rv.layoutManager=layoutManager
                comment_rv.adapter=adapter
            }

            override fun onFailure(call: Call<GetCommentResponseModel>, t: Throwable) {
                Toast.makeText(this@CommentAactivity, "done", Toast.LENGTH_SHORT).show()
                Log.d("comment", "onResponse: "+t.message)
            }
        })

        send_btn.setOnClickListener{
            val comment=comment_et.text.toString()
            comment_et.text.clear()
            val reqCall = BuilderRetrofit.builService(ApiInterface::class.java).add_comment(
                AddCommentModel(postId!!,userId!!,comment))
            reqCall.enqueue(object:Callback<AddCommentResponseModel>{
                override fun onResponse(call: Call<AddCommentResponseModel>, response: Response<AddCommentResponseModel>) {
                    if(response.isSuccessful){
                        val cmnt = response.body()
                        cmnt?.let {
                            commentList.add(it)
                            // Notify the adapter that the dataset has changed
                            adapter.notifyDataSetChanged()
                            // Scroll to the last item
                            comment_rv.scrollToPosition(adapter.itemCount - 1)
                        }
                        Toast.makeText(this@CommentAactivity, "comment send", Toast.LENGTH_SHORT).show()
                        Log.d("comment", "onResponse: "+response.body())
                    }
                    else{
                        Toast.makeText(this@CommentAactivity, "response failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddCommentResponseModel>, t: Throwable) {
                    Toast.makeText(this@CommentAactivity, "failed", Toast.LENGTH_SHORT).show()
                    Log.d("comment", "onResponse: "+t.message)
                }
            })

        }



    }
    fun init(){
        comment_rv=findViewById(R.id.comment_rv)
        comment_et=findViewById(R.id.comment_et)
        send_btn=findViewById(R.id.send_btn)
    }
}