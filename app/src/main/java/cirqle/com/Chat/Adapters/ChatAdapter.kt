package cirqle.com.Chat.Adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.Chat.ChattingPageActivity
import cirqle.com.Chat.Models.ChatResponseModel
import cirqle.com.R
import cirqle.com.SeniorCirqle.Models.AddDoubtResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChatAdapter(private var context:Context,private var list:List<ChatResponseModel>, private var userId:String ): RecyclerView.Adapter<ChatAdapter.ViewHolder>(),Filterable {
    private lateinit var progressDialog: Dialog
    private var filteredList: kotlin.collections.List<ChatResponseModel> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_chat_rv_item,parent, false))
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(userId==filteredList[position].users[0]._id){
            holder.name_tv.text=filteredList[position].users[1].name.toString()
            if(!filteredList[position].users[1].profilePic.isNullOrBlank()) {
                Picasso.get().load(filteredList[position].users[1].profilePic.toString())
                    .into(holder.profile_pic_iv)
            }
        }
        else if(userId==filteredList[position].users[1]._id){
            holder.name_tv.text=filteredList[position].users[0].name.toString()
            if(!filteredList[position].users[0].profilePic.isNullOrBlank()) {
                Picasso.get().load(filteredList[position].users[0].profilePic.toString())
                    .into(holder.profile_pic_iv)
            }
        }
        else{
            holder.name_tv.text=filteredList[position].users[0].name.toString()
        }

         val lastmessage = if (filteredList[position]?.latestMessage?.content.toString().length > 10) {
           filteredList[position]?.latestMessage?.content.toString().substring(0, 10) + "..."
        } else {
           filteredList[position]?.latestMessage?.content.toString()
        }
        holder.last_message.text=lastmessage

        progressDialog = Dialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setContentView(R.layout.layout_loading_progress_dialog)
        holder.chat_layout.setOnClickListener{
            progressDialog.show()
            val getmessageService = BuilderRetrofit.builService(ApiInterface::class.java)
            val sender=if(list[position].users[0]._id==userId){
                filteredList[position].users[1]
            }else{
                filteredList[position].users[0]
            }
            val reqCall=getmessageService.accessChat(userId,sender._id.toString())
            reqCall.enqueue(object: Callback<ChatResponseModel> {
                override fun onResponse(call: Call<ChatResponseModel>, response: Response<ChatResponseModel>) {
                    val intent= Intent(context, ChattingPageActivity::class.java)
                    progressDialog.dismiss()
                    intent.putExtra("postUserId",sender._id.toString())
                    intent.putExtra("chatId",response.body()?._id)
                    intent.putExtra("userName",sender.userName)
                    intent.putExtra("profilePic",sender.profilePic )
                    context.startActivity(intent)
                }

                override fun onFailure(call: Call<ChatResponseModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(context, "failed to access chat", Toast.LENGTH_SHORT).show()
                    Log.d("accessChat", "onFailure: "+t.message)
                }
            })
        }

    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val name_tv:TextView=itemView.findViewById(R.id.name_tv)
        val last_message:TextView=itemView.findViewById(R.id.last_message_tv)
        val chat_layout:LinearLayout=itemView.findViewById(R.id.chat_layout)
        val profile_pic_iv:ImageView=itemView.findViewById(R.id.profile_pic_iv)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = mutableListOf<ChatResponseModel>()
                if (constraint.isNullOrBlank()) {
                    filteredResults.addAll(list)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    list.forEach { item ->
                        if ((item.users[0].name.toLowerCase(Locale.ROOT).contains(filterPattern) && item.users[0]._id!=userId) || (item.users[1].name.toLowerCase(Locale.ROOT).contains(filterPattern)&& item.users[1]._id!=userId)  ) {
                            filteredResults.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<ChatResponseModel>
                notifyDataSetChanged()
            }
        }
    }
}