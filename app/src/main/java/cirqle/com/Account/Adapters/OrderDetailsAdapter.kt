package cirqle.com.Account.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.Account.Models.CartModelForOrderResponse
import cirqle.com.R
import com.squareup.picasso.Picasso

class OrderDetailsAdapter(var context: Context, var list:ArrayList<CartModelForOrderResponse>):
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_order_details,parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rupeeSymbol = "\u20B9"
        holder.title.text=list[position].product.title
        holder.quantity.text=list[position].quantity.toString()
        holder.price.text=rupeeSymbol+" "+list[position].product.price.toString()
        holder.total.text=rupeeSymbol+" "+(list[position].quantity*list[position].product.price).toString()
        holder.product_details.text=list[position].product.productDetails.toString()
        if(list[position].product.images.size>0) {
            Picasso.get().load(list[position].product.images[0]).into(holder.image)
        }
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val title: TextView =itemView.findViewById(R.id.title)
        val quantity: TextView =itemView.findViewById(R.id.quantity)
        val price: TextView =itemView.findViewById(R.id.price)
        val total: TextView =itemView.findViewById(R.id.total)
        val image: ImageView =itemView.findViewById(R.id.image)
        val product_details:TextView=itemView.findViewById(R.id.product_details)
    }

}