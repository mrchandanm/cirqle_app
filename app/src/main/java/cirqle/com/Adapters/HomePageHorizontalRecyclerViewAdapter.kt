package cirqle.com.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.HomeHorizontalRVmodel
import cirqle.com.R

class HomePageHorizontalRecyclerViewAdapter(private val context: Context, private var list:ArrayList<HomeHorizontalRVmodel>) : RecyclerView.Adapter<HomePageHorizontalRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_page_horizontal_recuclerview_column,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tlist = list;
        holder.price.text=tlist[position].price.toString();
        holder.description.text=tlist[position].description.toString();
        holder.location.text=tlist[position].location.toString();
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val price : TextView=itemView.findViewById(R.id.price)
        val description : TextView=itemView.findViewById(R.id.description)
        val location : TextView=itemView.findViewById(R.id.location)


    }

}