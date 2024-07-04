package cirqle.com.BuyAndSell.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.FullImageViewctivity
import cirqle.com.R
import com.squareup.picasso.Picasso

class ViewPagerPicassoImageAdapter(private val context: Context, private val images: ArrayList<String>):
    RecyclerView.Adapter<ViewPagerPicassoImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewpager_image_show_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(images[position]).into(holder.imageView)
        holder.imageView.setOnClickListener{
            val intent= Intent(context, FullImageViewctivity::class.java)
            intent.putExtra("images",images)
            context.startActivity(intent)
        }

    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.viewpager_item)
    }
}