package cirqle.com.SeniorCirqle.Adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import cirqle.com.BuyAndSell.Adapters.ViewPagerPicassoImageAdapter
import cirqle.com.Chat.ChattingPageActivity
import cirqle.com.Chat.Models.ChatResponseModel
import cirqle.com.FullImageViewctivity
import cirqle.com.R
import cirqle.com.SeniorCirqle.CommentAactivity
import cirqle.com.SeniorCirqle.Models.AddDoubtResponseModel
import cirqle.com.SeniorCirqle.Models.DeleteResponseModel
import cirqle.com.SeniorCirqle.Models.GetCommentResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AskDoubtsPostAdapter(private var context: Context, private var list:MutableList<AddDoubtResponseModel>): RecyclerView.Adapter<AskDoubtsPostAdapter.ViewHolder>(),Filterable {
    val userId= Utility.getUserDetails(context)?._id!!
    private var filteredList: kotlin.collections.List<AddDoubtResponseModel> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(cirqle.com.R.layout.layout_ask_doubt_post_rv,parent,false))
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var likeCount=filteredList[position].likes.size
        if(filteredList[position].user._id==userId){
            holder.more_btn.visibility=View.VISIBLE
        }
        holder.more_btn.setOnClickListener{
            showPopupMenu(holder.more_btn, holder.adapterPosition)
        }
        val pos=holder.adapterPosition
        holder.name_tv.text=if(filteredList[position].isAnonymos=="No") filteredList[position].user.name.toString()
        else "Anonymous"
        holder.passout_tv.text = filteredList[position].user.passoutYear.toString() + " passout"
        holder.likes_tv.text=filteredList[position].likes.size.toString()
        if(filteredList[pos].likes.contains(userId)){
            holder.unlike_tv.visibility=View.GONE
            holder.liked_tv.visibility=View.VISIBLE
        }
        holder.like_unlike_layout.setOnClickListener {
            if(holder.unlike_tv.visibility==View.VISIBLE){
                holder.unlike_tv.visibility=View.GONE
                holder.liked_tv.visibility=View.VISIBLE

                val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).like(filteredList[position]._id,userId,"like")
                reqCall.enqueue(object:Callback<AddDoubtResponseModel>{
                    override fun onResponse(
                        call: Call<AddDoubtResponseModel>,
                        response: Response<AddDoubtResponseModel>
                    ) {
                        likeCount++
                        holder.likes_tv.text=likeCount.toString()
                        Log.d("like", "onResponse: "+response.body())
                    }

                    override fun onFailure(call: Call<AddDoubtResponseModel>, t: Throwable) {
                        Log.d("like", "onFailure: "+t.message)
                    }

                })
            }
            else{
                holder.unlike_tv.visibility=View.VISIBLE
                holder.liked_tv.visibility=View.GONE
                val unlike=BuilderRetrofit.builService(ApiInterface::class.java).like(filteredList[position]._id,userId,"dislike")
                unlike.enqueue(object:Callback<AddDoubtResponseModel>{
                    override fun onResponse(
                        call: Call<AddDoubtResponseModel>,
                        response: Response<AddDoubtResponseModel>
                    ) {
                        likeCount--
                        holder.likes_tv.text=likeCount.toString()
                        Log.d("like", "onResponse: "+response.body())
                    }

                    override fun onFailure(call: Call<AddDoubtResponseModel>, t: Throwable) {
                        Log.d("like", "onFailure: "+t.message)
                    }

                })
            }
        }



        val reqCall = BuilderRetrofit.builService(ApiInterface::class.java).get_comments(filteredList[pos]._id)
        reqCall.enqueue(object :Callback<GetCommentResponseModel>{
            override fun onResponse(call: Call<GetCommentResponseModel>, response: Response<GetCommentResponseModel>) {
                Log.d("comment", "onResponse: "+response.body()?.comment)
                holder.write_answer_btn.text=response.body()?.comment?.size.toString()
            }

            override fun onFailure(call: Call<GetCommentResponseModel>, t: Throwable) {
                Log.d("comment", "onResponse: "+t.message)
            }
        })

        if(filteredList[pos].user.profilePic!="") {
            Picasso.get().load(filteredList[position].user.profilePic.toString())
                .into(holder.profilePic)
        }
        val shortq = if (filteredList[position].question.toString().length > 30) {
            filteredList[position].question.toString().substring(0, 30) + "..."
        } else {
            holder.see_more_btn.visibility=View.GONE
            filteredList[position].question.toString()
        }
        holder.short_question_tv.text=shortq
        holder.full_question_tv.text=filteredList[position].question
        if(filteredList[position].images.size>0) {
            val adapter = ViewPagerPicassoImageAdapter(context, filteredList[position].images)
            holder.viewpager.adapter = adapter
        }else{
            holder.viewpager.visibility=View.GONE
        }

        holder.viewpager.setOnClickListener{
            val intent=Intent(context,FullImageViewctivity::class.java)
            intent.putExtra("images",filteredList[pos].images)
            context.startActivity(intent)
        }
        var count=0
        val nnlist=filteredList.getOrNull(position)
        if(nnlist!=null) {
            for (item in nnlist.options) {
                if (item.votes != null) {
                    count += item.votes.size
                }
            }
        }
        val option_rv_adapter=AskDoubtsOptionsAdapter(context,filteredList[position].options,count,filteredList[position]._id)
        holder.options_rv.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        holder.options_rv.adapter=option_rv_adapter

        holder.see_more_btn.setOnClickListener{
            holder.short_question_tv.visibility=View.GONE
            holder.full_question_tv.visibility=View.VISIBLE
            holder.see_more_btn.visibility=View.GONE
        }
        if(userId==list[pos].user._id){
            holder.chat_btn.visibility=View.GONE
        }
        holder.chat_btn.setOnClickListener{
            val getmessageService = BuilderRetrofit.builService(ApiInterface::class.java)
            val reqCall=getmessageService.accessChat(userId,filteredList[pos].user._id.toString())
            reqCall.enqueue(object: Callback<ChatResponseModel> {
                override fun onResponse(call: Call<ChatResponseModel>, response: Response<ChatResponseModel>) {
                    val intent= Intent(context, ChattingPageActivity::class.java)

                    intent.putExtra("postUserId",filteredList[pos].user._id.toString())
                    intent.putExtra("chatId",response.body()?._id.toString())
                    intent.putExtra("userName",filteredList[pos].user.userName.toString())
                    context.startActivity(intent)
                }

                override fun onFailure(call: Call<ChatResponseModel>, t: Throwable) {
                    Toast.makeText(context, "failed to access chat", Toast.LENGTH_SHORT).show()
                    Log.d("accessChat", "onFailure: "+t.message)
                }
            })
        }

        holder.write_answer_btn.setOnClickListener{
            val intent=Intent(context,CommentAactivity::class.java)
            intent.putExtra("postId",filteredList[pos]._id.toString())
            intent.putExtra("userId",userId)
            context.startActivity(intent)
        }


    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val name_tv:TextView=itemView.findViewById(cirqle.com.R.id.name_tv)
        val passout_tv:TextView=itemView.findViewById(cirqle.com.R.id.passout_tv)
        val short_question_tv:TextView=itemView.findViewById(cirqle.com.R.id.short_question_tv)
        val see_more_btn:TextView=itemView.findViewById(cirqle.com.R.id.see_more_btn)
        val full_question_tv:TextView=itemView.findViewById(cirqle.com.R.id.full_question_tv)
        val viewpager:ViewPager2=itemView.findViewById(cirqle.com.R.id.viewpager)
        val options_rv:RecyclerView=itemView.findViewById(cirqle.com.R.id.options_rv)
        val write_answer_btn:TextView=itemView.findViewById(cirqle.com.R.id.write_answer_btn)
        val chat_btn:TextView=itemView.findViewById(cirqle.com.R.id.chat_btn)
        val profilePic:ImageView=itemView.findViewById(cirqle.com.R.id.profilePic)
        val likes_tv:TextView=itemView.findViewById(cirqle.com.R.id.likes_tv)
        val unlike_tv:TextView=itemView.findViewById(cirqle.com.R.id.unlike_tv)
        val liked_tv:TextView=itemView.findViewById(cirqle.com.R.id.liked_tv)
        val like_unlike_layout:LinearLayout=itemView.findViewById(cirqle.com.R.id.like_unlike_layout)
        val more_btn:ImageView=itemView.findViewById(cirqle.com.R.id.more)


    }



    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = mutableListOf<AddDoubtResponseModel>()
                if (constraint.isNullOrBlank()) {
                    filteredResults.addAll(list)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    list.forEach { item ->
                        if (item.user.name.toLowerCase(Locale.ROOT).contains(filterPattern)  ) {
                            filteredResults.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<AddDoubtResponseModel>
                notifyDataSetChanged()
            }
        }
    }
    fun showPopupMenu(view: View, pos:Int) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(cirqle.com.R.menu.post_more_menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                cirqle.com.R.id.edit -> {
                    Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()
                    true
                }
                cirqle.com.R.id.delete -> {
                    val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).delete_post(filteredList[pos]._id)
                    reqCall.enqueue(object :Callback<DeleteResponseModel>{
                        override fun onResponse(
                            call: Call<DeleteResponseModel>,
                            response: Response<DeleteResponseModel>
                        ) {
                            list.removeAt(pos)
                            notifyDataSetChanged()
                            Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<DeleteResponseModel>, t: Throwable) {
                            Log.d("delete", "onFailure: "+t.message)
                        }

                    })

                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

}