package cirqle.com.LostAndFound.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.BuyAndSell.UserPostPageActivity
import cirqle.com.LostAndFound.LostAndFoundUserPageActivity
import cirqle.com.LostAndFound.Models.AddPostResponseModel
import cirqle.com.R
import com.squareup.picasso.Picasso

class PostAdapter(private var context: Context, private var list:List<AddPostResponseModel>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_lost_and_found_rv, parent, false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(list[position].images.size>0) {
            Picasso.get().load(list[position].images[0]).into(holder.image)
        }
        val shortTitle=if(list[position].title.toString().length>20) list[position].title.toString().substring(0,20)+"..."
        else list[position].title.toString()
        holder.title.text=shortTitle
        val shortdiscription = if (list[position]?.description.toString().length > 90) {
            list[position]?.description.toString().substring(0, 90) + "..."
        } else {
            list[position]?.description.toString()
        }
        holder.description.text=shortdiscription
        holder.date.text=list[position].date.toString()
        holder.location.text=list[position].location.toString()

        holder.mainLayout.setOnClickListener{
            val intent= Intent(context, LostAndFoundUserPageActivity::class.java)
            intent.putExtra("postDetails", list[position])
            intent.putExtra("images",list[position].images)
            context.startActivity(intent)
        }
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val image:ImageView=itemView.findViewById(R.id.image)
        val title:TextView=itemView.findViewById(R.id.title)
        val description:TextView=itemView.findViewById(R.id.description)
        val date:TextView=itemView.findViewById(R.id.date)
        val location:TextView=itemView.findViewById(R.id.location)
        val mainLayout:LinearLayout=itemView.findViewById(R.id.main_layout)
    }
}