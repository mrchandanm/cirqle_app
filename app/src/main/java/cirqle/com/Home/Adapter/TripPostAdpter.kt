package cirqle.com.Home.Adapter


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.Chat.ChattingPageActivity
import cirqle.com.Chat.Models.ChatResponseModel
import cirqle.com.R
import cirqle.com.TravelBuddy.Models.AddTripResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.PermissionCallback
import cirqle.com.Utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TripPostAdapter(private var context: Context, private var list:kotlin.collections.List<AddTripResponseModel>):
    RecyclerView.Adapter<TripPostAdapter.ViewHolder>() {
    private var filteredList: kotlin.collections.List<AddTripResponseModel> = list
    var userId= Utility.getUserDetails(context)?._id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_trip_home_rv,parent,false))
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos=holder.adapterPosition
        holder.name_tv.text=filteredList[position]?.user?.name.toString()
        holder.from_tv.text=filteredList[position]?.from.toString()
        holder.to_tv.text=filteredList[position]?.to.toString()
        holder.dep_date_tv.text=filteredList[position].DepartureDate.toString()
        holder.ret_date_tv.text=filteredList[position].returnDate.toString()
        holder.description_tv.text=filteredList[position].description.toString()
        holder.cost_tv.text=filteredList[position].costSharing.toString()
        holder.seat_tv.text=filteredList[position].seatsAvailable.toString()

        holder.chat_btn.setOnClickListener{
            val getmessageService = BuilderRetrofit.builService(ApiInterface::class.java)
            val reqCall=getmessageService.accessChat(userId!!,list[pos].user._id.toString())
            reqCall.enqueue(object: Callback<ChatResponseModel> {
                override fun onResponse(call: Call<ChatResponseModel>, response: Response<ChatResponseModel>) {
                    val intent= Intent(context, ChattingPageActivity::class.java)

                    intent.putExtra("postUserId",list[pos].user._id.toString())
                    intent.putExtra("chatId",response.body()?._id.toString())
                    intent.putExtra("userName",list[pos].user.userName.toString())
                    context.startActivity(intent)
                }

                override fun onFailure(call: Call<ChatResponseModel>, t: Throwable) {
                    Toast.makeText(context, "failed to access chat", Toast.LENGTH_SHORT).show()
                    Log.d("accessChat", "onFailure: "+t.message)
                }
            })
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name_tv: TextView =itemView.findViewById(R.id.name_tv)
        val from_tv: TextView =itemView.findViewById(R.id.from_tv)
        val to_tv: TextView =itemView.findViewById(R.id.to_tv)
        val dep_date_tv: TextView =itemView.findViewById(R.id.dep_date_tv)
        val ret_date_tv: TextView =itemView.findViewById(R.id.ret_date_tv)
        val description_tv: TextView =itemView.findViewById(R.id.description_tv)
        val cost_tv: TextView =itemView.findViewById(R.id.cost_tv)
        val seat_tv: TextView =itemView.findViewById(R.id.seat_tv)
        val call_btn: AppCompatButton =itemView.findViewById(R.id.call_btn)
        val chat_btn: AppCompatButton =itemView.findViewById(R.id.chat_btn)
    }



}