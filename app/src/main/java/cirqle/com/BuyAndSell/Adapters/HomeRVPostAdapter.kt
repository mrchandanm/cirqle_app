package cirqle.com.BuyAndSell.Adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.Adapters.HomePageHorizontalRecyclerViewAdapter
import cirqle.com.BuyAndSell.Models.BuySellresponseModel
import cirqle.com.BuyAndSell.Models.GetPostResponseModel
import cirqle.com.BuyAndSell.UserPostPageActivity
import cirqle.com.R
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso

class HomeRVPostAdapter(private var context: Context, private var list:List<BuySellresponseModel>):RecyclerView.Adapter<HomeRVPostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return HomeRVPostAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_but_and_sell_post, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tlist = list;
        val rupeeSymbol = "\u20B9"
        holder.price.text=rupeeSymbol+tlist[position].price.toString()
        holder.description.text=tlist[position].description.toString();
        holder.location.text=tlist[position].owner?.hostelName.toString();
        if(tlist[position].images.size>0) {
            Picasso.get().load(tlist[position].images[0]).into(holder.image)
        }
        holder.main_layout.setOnClickListener{
            val intent=Intent(context,UserPostPageActivity::class.java)
            intent.putExtra("postDetails", list[position])
            intent.putExtra("images",list[position].images)
            context.startActivity(intent)
        }

    }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val price : TextView =itemView.findViewById(R.id.price)
        val description : TextView =itemView.findViewById(R.id.description)
        val location : TextView =itemView.findViewById(R.id.location)
        val image:ImageView=itemView.findViewById(R.id.image)
        val main_layout:LinearLayout=itemView.findViewById(R.id.main_layout)
    }
}