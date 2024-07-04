package cirqle.com.BuyAndSell.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.R

class ImageShowViewPagerAdapter(private val context: Context, private val imageUris: List<Uri>):
    RecyclerView.Adapter<ImageShowViewPagerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewpager_image_show_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return imageUris.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageURI(imageUris[position])
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.viewpager_item)
    }
}
