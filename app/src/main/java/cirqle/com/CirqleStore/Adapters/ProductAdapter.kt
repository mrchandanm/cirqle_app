package cirqle.com.CirqleStore.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.CirqleStore.Models.*
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductAdapter(private var context: Context, private var list:MutableList<AddProductResponseModel>): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_cirqle_store_product_items_rv,parent,false))
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user=Utility.getUserDetails(context)?._id
        if(list[position].images.size>0) {
            Picasso.get().load(list[position].images[0]).into(holder.productImage)
        }
        val rupeeSymbol = "\u20B9"
        holder.title.text=list[position].title.toString()
        holder.price.text=rupeeSymbol+list[position].price.toString()
        holder.mrp.text="MRP "+list[position].mrp.toString()
        holder.brandName.text=list[position].brand.toString()
        holder.expiry.text="Exp. "+list[position].expiry.toString()
        holder.quantity_tv.text=list[position].productDetails.toString()
        var quantity=0
        var cartId=""
        var pos=holder.adapterPosition
        val getcart=BuilderRetrofit.builService(ApiInterface::class.java).get_user_cart(user!!,list[position]._id)
        getcart.enqueue(object :Callback<MutableList<GetUserCartResponseModel>>{
            override fun onResponse(call: Call<MutableList<GetUserCartResponseModel>>, response: Response<MutableList<GetUserCartResponseModel>>) {
                if(response.isSuccessful){
                    if(!response.body().isNullOrEmpty()){
                       holder.add_btn.visibility=View.GONE
                       holder.addRemoveItem.visibility=View.VISIBLE
                        val carts=response.body()!!
                        holder.tvQuantity.text=carts[0].quantity.toString()
                        quantity=carts[0].quantity
                        cartId=carts[0]._id
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<GetUserCartResponseModel>>, t: Throwable) {
                Log.d("getqt", "onFailure: "+t.message)
            }

        })



        holder.add_btn.setOnClickListener{
//            var cart=ArrayList<LocalCartModel>()
//            var carts=Utility.getCarts(context)?.carts
//            carts?.add(LocalCartModel(list[position]._id,list[position],1))
//            Utility.saveObjectLocally(context,"cart",CartModelToSaveLocally(carts!!))
            val reqCall= BuilderRetrofit.builService(ApiInterface::class.java).add_to_cart(list[pos]._id,user!!,1)
            reqCall.enqueue(object:Callback<GetCartResponseModel>{
                override fun onResponse(call: Call<GetCartResponseModel>, response: Response<GetCartResponseModel>) {
                    Toast.makeText(context, "added to cart", Toast.LENGTH_SHORT).show()
                    Log.d("cart", "onResponse: "+response.body())
                    cartId=response.body()?._id!!
                   quantity=1
                }

                override fun onFailure(call: Call<GetCartResponseModel>, t: Throwable) {
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                    Log.d("cart", "onResponse: "+t.message)
                }

            })
            holder.add_btn.visibility=View.GONE
            holder.addRemoveItem.visibility=View.VISIBLE
        }
        holder.btnPlus.setOnClickListener{
            Log.d("qty", "onBindViewHolder: "+quantity)
//            var carts=Utility.getCarts(context)?.carts
//            val posi = carts?.indexOfFirst { it.productId == list[position]._id }!!
            quantity++
            Log.d("qty", "onBindViewHolder: "+quantity)
            val plus_item=BuilderRetrofit.builService(ApiInterface::class.java).update_cart_quantity(cartId,quantity)
            plus_item.enqueue(object :Callback<GetCartResponseModel>{
                override fun onResponse(call: Call<GetCartResponseModel>, response: Response<GetCartResponseModel>) {
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
                holder.add_btn.visibility=View.VISIBLE
                holder.addRemoveItem.visibility=View.GONE
                val plus_item=BuilderRetrofit.builService(ApiInterface::class.java).update_cart_quantity(cartId,0)
                plus_item.enqueue(object :Callback<GetCartResponseModel>{
                    override fun onResponse(call: Call<GetCartResponseModel>, response: Response<GetCartResponseModel>) {
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
                val plus_item=BuilderRetrofit.builService(ApiInterface::class.java).update_cart_quantity(cartId,quantity)
                plus_item.enqueue(object :Callback<GetCartResponseModel>{
                    override fun onResponse(call: Call<GetCartResponseModel>, response: Response<GetCartResponseModel>) {
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

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val productImage:ImageView=itemView.findViewById(R.id.productImage)
        val title:TextView=itemView.findViewById(R.id.title)
        val quantity_tv:TextView=itemView.findViewById(R.id.quantity)
        val price:TextView=itemView.findViewById(R.id.price)
        val mrp:TextView=itemView.findViewById(R.id.mrp)
        val add_btn:AppCompatButton=itemView.findViewById(R.id.add_items)
        val addRemoveItem:LinearLayout=itemView.findViewById(R.id.addRemoveItem)
        val btnMinus:ImageButton=itemView.findViewById(R.id.btnMinus)
        val btnPlus:ImageButton=itemView.findViewById(R.id.btnPlus)
        val tvQuantity:TextView=itemView.findViewById(R.id.tvQuantity)
        val brandName:TextView=itemView.findViewById(R.id.brandName)
        val expiry:TextView=itemView.findViewById(R.id.expiry)


    }
}