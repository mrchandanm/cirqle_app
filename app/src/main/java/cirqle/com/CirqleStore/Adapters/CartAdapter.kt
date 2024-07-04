package cirqle.com.CirqleStore.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.CirqleStore.Models.AddProductResponseModel
import cirqle.com.CirqleStore.Models.GetCartResponseModel
import cirqle.com.CirqleStore.Models.GetUserCartResponseModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.RefreshListener
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartAdapter(private var context: Context, private var list:MutableList<GetUserCartResponseModel>,private val refreshListener: RefreshListener): RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.layout_cart_product_rv, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].product.images.size > 0) {
            Picasso.get().load(list[position].product.images[0]).into(holder.productImage)
        }
        val rupeeSymbol = "\u20B9"
        holder.title.text = list[position].product.title.toString()
        holder.price.text = rupeeSymbol + list[position].product.price.toString()
        holder.mrp.text = "MRP " + list[position].product.mrp.toString()
        holder.brandName.text = list[position].product.brand.toString()
        holder.expiry.text = "Exp. " + list[position].product.expiry.toString()
        var quantity = list[position].quantity
        holder.tvQuantity.setText(quantity.toString())
        holder.btnPlus.setOnClickListener{
            quantity++
            val plus_item= BuilderRetrofit.builService(ApiInterface::class.java).update_cart_quantity(list[position]._id,quantity)
            plus_item.enqueue(object : Callback<GetCartResponseModel> {
                override fun onResponse(call: Call<GetCartResponseModel>, response: Response<GetCartResponseModel>) {
                    refreshListener.onRefresh()
                    Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show()
                    Log.d("updatecart", "onResponse: "+response.body())
                }

                override fun onFailure(call: Call<GetCartResponseModel>, t: Throwable) {
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                    Log.d("updatecart", "onResponse: "+t.message)
                }
            })

            holder.tvQuantity.setText(quantity.toString())
        }
        holder.btnMinus.setOnClickListener{
            if(quantity==1 || quantity==0){
                val plus_item=BuilderRetrofit.builService(ApiInterface::class.java).update_cart_quantity(list[position]._id,0)
                plus_item.enqueue(object :Callback<GetCartResponseModel>{
                    override fun onResponse(call: Call<GetCartResponseModel>, response: Response<GetCartResponseModel>) {
                        refreshListener.onRefresh()
                        Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show()
                        Log.d("updatecart", "onResponse: "+response.body())
                    }

                    override fun onFailure(call: Call<GetCartResponseModel>, t: Throwable) {
                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                        Log.d("updatecart", "onResponse: "+t.message)
                    }

                })
                quantity=1
                holder.tvQuantity.setText(quantity.toString())
            }
            else{
                quantity--
                val plus_item=BuilderRetrofit.builService(ApiInterface::class.java).update_cart_quantity(list[position]._id,quantity)
                plus_item.enqueue(object :Callback<GetCartResponseModel>{
                    override fun onResponse(call: Call<GetCartResponseModel>, response: Response<GetCartResponseModel>) {
                        refreshListener.onRefresh()
                        Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show()
                        Log.d("updatecart", "onResponse: "+response.body())
                    }

                    override fun onFailure(call: Call<GetCartResponseModel>, t: Throwable) {
                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                        Log.d("updatecart", "onResponse: "+t.message)
                    }

                })
                holder.tvQuantity.setText(quantity.toString())
            }
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val title: TextView = itemView.findViewById(R.id.title)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val price: TextView = itemView.findViewById(R.id.price)
        val mrp: TextView = itemView.findViewById(R.id.mrp)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val brandName: TextView = itemView.findViewById(R.id.brandName)
        val expiry: TextView = itemView.findViewById(R.id.expiry)

    }
}

