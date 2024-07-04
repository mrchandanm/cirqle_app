package cirqle.com.DateSpark.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.DateSpark.Models.DateSparkResponseModel
import cirqle.com.R
import com.squareup.picasso.Picasso

class HomeSliderViewPagerAdapter(private val context: Context, private val list: List<DateSparkResponseModel>) : RecyclerView.Adapter<HomeSliderViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_dating_pager_slider,parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(list[position].images[0]).into(holder.image)
        holder.name.text=list[position].user.name

    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val image:ImageView=itemView.findViewById(R.id.image)
        val name:TextView=itemView.findViewById(R.id.name)
        val love:ImageView=itemView.findViewById(R.id.love)
        val chat:ImageView=itemView.findViewById(R.id.chat)
    }
}