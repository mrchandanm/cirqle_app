package cirqle.com.SeniorCirqle.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.BuyAndSell.Models.BuySellresponseModel
import cirqle.com.R
import cirqle.com.SeniorCirqle.Models.OptionModel
import cirqle.com.SeniorCirqle.Models.OptionResponseModel
import cirqle.com.SeniorCirqle.Models.VoteResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import okhttp3.internal.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AskDoubtsOptionsAdapter(private var context: Context, private var list:ArrayList<OptionResponseModel>, private var totalVote:Int,private var postId:String): RecyclerView.Adapter<AskDoubtsOptionsAdapter.ViewHolder>() {
    val userId= Utility.getUserDetails(context)?._id!!
    companion object {
        private var NO_SELECT = -1
    }
    private var selectedPosition= list.indexOfFirst { it.votes.contains(userId) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_option_rv,parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos=holder.adapterPosition

        var percentage=0.0.toFloat()
        var count=0
        if(list[position]!=null) {
            if(list[position].votes!=null) {
                count = list[position].votes.size
                percentage = (count / totalVote.toFloat()) * 100
            }
        }
        holder.option_text.text=list[position].text
        holder.percentage_text.text= "($count) $percentage % "


        holder.radio_btn.isChecked = selectedPosition == position
        holder.layout.setBackgroundResource(
            if(selectedPosition==pos) R.drawable.option_clicked_bg
            else R.drawable.option_default_bg
        )
        holder.progress_bar.progress=percentage.toInt()
        holder.layout.setOnClickListener{
            var prevPositionId=if(selectedPosition!=-1) list[selectedPosition]._id
            else "abc"
            if(selectedPosition==pos){
                selectedPosition=-1
                list[position].votes.remove(userId)
                totalVote-=1

            }
            else{
                if(selectedPosition!=-1){
                    list[selectedPosition].votes.remove(userId)
                }
                else{
                    totalVote+=1
                }
                list[position].votes.add(userId)
                selectedPosition=pos
            }
            notifyDataSetChanged()


            val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).vote(postId,userId,list[position]._id,
                prevPositionId).enqueue(object :Callback<VoteResponseModel>{
                override fun onResponse(call: Call<VoteResponseModel>, response: Response<VoteResponseModel>) {
                    Log.d("vote", "onResponse: "+response.body())
                }

                override fun onFailure(call: Call<VoteResponseModel>, t: Throwable) {
                    Log.d("vote", "onFailure: "+t.message)

                }
                })

        }



    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val progress_bar:ProgressBar=itemView.findViewById(R.id.progress_bar)
        val option_text:TextView=itemView.findViewById(R.id.option_text)
        val percentage_text:TextView=itemView.findViewById(R.id.percentage_text)
        val radio_btn:RadioButton=itemView.findViewById(R.id.radio_btn)
        val layout:LinearLayout=itemView.findViewById(R.id.layout)
    }

}