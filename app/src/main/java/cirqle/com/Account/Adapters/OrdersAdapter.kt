package cirqle.com.Account.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.Account.Models.GetOrdersModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.airbnb.lottie.LottieAnimationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersAdapter(var context:Context,var list:ArrayList<GetOrdersModel>): RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_orders,parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rupeeSymbol = "\u20B9"
        var status="outForDelivery";
        var pos=holder.adapterPosition
        holder.name.text=list[pos].user.name.toString()
        holder.hostel.text=list[pos].hostel.toString()
        holder.productCount.text=list[pos].carts.size.toString()
        holder.totalAmount.text=rupeeSymbol+list[pos].totalAmount.toString()
        holder.time_ago.text= Utility.getTimeAgoString(list[pos].updatedAt).toString()

        holder.orderId.text="OrderId : ${list[pos]._id.toString()}"
        var orderDetailsAdapter=OrderDetailsAdapter(context, list[pos].carts)
        holder.cartProductRv.layoutManager= LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        holder.cartProductRv.adapter=orderDetailsAdapter

        if(list[pos].status.toString()=="pending"){
            holder.pending.visibility=View.VISIBLE
            holder.out_for_delivery.visibility=View.GONE
            holder.delivered.visibility=View.GONE
            holder.statusTv.text="Order Placed (Ready To Ship)"
            holder.statusTv.setTextColor(ContextCompat.getColor(context, R.color.pending))
        }
        else if(list[pos].status.toString()=="outForDelivery"){
            holder.pending.visibility=View.GONE
            holder.out_for_delivery.visibility=View.VISIBLE
            holder.delivered.visibility=View.GONE
            holder.statusTv.text="Out For Delivery"
            holder.statusTv.setTextColor(ContextCompat.getColor(context, R.color.yellow))
        }
        else{
            holder.pending.visibility=View.GONE
            holder.out_for_delivery.visibility=View.GONE
            holder.delivered.visibility=View.VISIBLE
            holder.statusTv.text="Delivered"
            holder.statusTv.setTextColor(ContextCompat.getColor(context, R.color.succes_green))
        }

        holder.viewDetails.setOnClickListener{
            if( holder.details_layout.visibility==View.GONE) {
                holder.details_layout.visibility = View.VISIBLE
                holder.viewDetails.text="Hide Details"
            }
            else{
                holder.details_layout.visibility = View.GONE
                holder.viewDetails.text="View Details"
            }
        }
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val name:TextView=itemView.findViewById(R.id.name)
        val hostel:TextView=itemView.findViewById(R.id.hostel)
        val productCount:TextView=itemView.findViewById(R.id.productCount)
        val totalAmount:TextView=itemView.findViewById(R.id.totalAmount)
        val viewDetails:TextView=itemView.findViewById(R.id.view_details_btn)
        val cartProductRv:RecyclerView=itemView.findViewById(R.id.cart_product_rv)
        val details_layout: LinearLayout =itemView.findViewById(R.id.details_layout)
        val orderId:TextView=itemView.findViewById(R.id.OrderId)
        val pending:LottieAnimationView=itemView.findViewById(R.id.anim_pending)
        val out_for_delivery:LottieAnimationView=itemView.findViewById(R.id.anim_out_for_delivery)
        val delivered:LottieAnimationView=itemView.findViewById(R.id.anim_delivered)
        val statusTv:TextView=itemView.findViewById(R.id.status)
        val time_ago:TextView=itemView.findViewById(R.id.time_ago)
    }
}