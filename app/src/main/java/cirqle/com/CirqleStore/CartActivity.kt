package cirqle.com.CirqleStore

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cirqle.com.Adapters.SearchAndSelectAdapter
import cirqle.com.CirqleStore.Adapters.CartAdapter
import cirqle.com.CirqleStore.Adapters.ProductAdapter
import cirqle.com.CirqleStore.Models.GetCartResponseModel
import cirqle.com.CirqleStore.Models.GetUserCartResponseModel
import cirqle.com.CirqleStore.Models.PlaceOrderModel
import cirqle.com.CirqleStore.Models.PlaceOrderResponseModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.RefreshListener
import cirqle.com.Utils.Utility
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText
import okhttp3.internal.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CartActivity : AppCompatActivity(), RefreshListener {
    private lateinit var cart_rv:RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var noOfProduct:TextView
    private lateinit var discount:TextView
    private lateinit var delivery_fee:TextView
    private lateinit var total_amount:TextView
    private lateinit var mrp_tv:TextView
    private lateinit var total:TextView
    private lateinit var savings:TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var select_hostel:AutoCompleteTextView
    private lateinit var placeOrder:AppCompatButton
    private lateinit var progressDialog: Dialog

    var totalAmount=0
    var mrp=0
    private lateinit var hostel:String
    private lateinit var carts:kotlin.collections.ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        init()
        progressDialog = Dialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setContentView(R.layout.layout_loading_progress_dialog)
        val user=Utility.getUserDetails(this)?._id
        val collegeName=Utility.getUserDetails(this)?.collegeName
        val datalist: ArrayList<String> = ArrayList()
        datalist.add("Apj Abdul kalam")
        datalist.add("Arya Bhatta")
        datalist.add("CV Raman")
        datalist.add("Ashima")

        val adapter= SearchAndSelectAdapter(this,android.R.layout.simple_list_item_2,datalist)
        select_hostel.setAdapter(adapter)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loaddata();
            swipeRefreshLayout.isRefreshing = false
        })
        loaddata()

        placeOrder.setOnClickListener{
            if(select_hostel.text.isEmpty()){
                select_hostel.setError("Hostel is Required")
            }
            else{
                progressDialog.show()
                val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).place_order(
                    PlaceOrderModel(user!!,select_hostel.text.toString(),totalAmount,mrp,collegeName!!,carts))
                reqCall.enqueue(object :Callback<PlaceOrderResponseModel>{
                    override fun onResponse(call: Call<PlaceOrderResponseModel>, response: Response<PlaceOrderResponseModel>) {
                        Toast.makeText(this@CartActivity, "order Placed", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                       val intent=Intent(this@CartActivity,PlaceOrderActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(call: Call<PlaceOrderResponseModel>, t: Throwable) {
                        Log.d("order", "onFailure: "+t.message)
                    }

                })
            }
        }

    }

    fun loaddata(){
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        cart_rv.visibility= View.GONE
        val user=Utility.getUserDetails(this)?._id
        val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).get_user_cart(user!!)
        reqCall.enqueue(object :Callback<MutableList<GetUserCartResponseModel>>{
            override fun onResponse(
                call: Call<MutableList<GetUserCartResponseModel>>,
                response: Response<MutableList<GetUserCartResponseModel>>
            ) {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                cart_rv.visibility= View.VISIBLE
                Log.d("getDoubt", "onsuccessful: "+response.body())
                mrp=0
                totalAmount=0
                carts= ArrayList()
                for(prod in response.body()!!){
                    mrp += prod.product.mrp* prod.quantity
                    totalAmount+=prod.product.price*prod.quantity
                    carts.add(prod._id.toString())
                }
                val rupeeSymbol = "\u20B9"
                mrp_tv.setText(rupeeSymbol+mrp.toString())
                discount.setText(rupeeSymbol+(mrp-totalAmount).toString())
                noOfProduct.setText("MRP "+"("+response.body()?.size.toString()!!+" Products"+")")
                total_amount.setText(rupeeSymbol+totalAmount.toString())
                total.setText("Total "+rupeeSymbol+totalAmount.toString())
                savings.setText("You will save "+(mrp-totalAmount).toString()+" on this order")
                delivery_fee.setText(rupeeSymbol+"0")

                adapter= CartAdapter(this@CartActivity,response.body()!!,this@CartActivity)
                cart_rv.layoutManager= LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL,false)
                cart_rv.adapter=adapter
            }

            override fun onFailure(call: Call<MutableList<GetUserCartResponseModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


    // Example usage







    fun init(){
        cart_rv=findViewById(R.id.cart_rv)
        shimmerLayout = findViewById(R.id.shimmerLayout)
        noOfProduct=findViewById(R.id.noOfProduct)
        discount=findViewById(R.id.discount)
        delivery_fee=findViewById(R.id.deliveryFee)
        total_amount=findViewById(R.id.total_amount)
        mrp_tv=findViewById(R.id.mrp_tv)
        total=findViewById(R.id.total)
        savings=findViewById(R.id.savings)
        select_hostel=findViewById(R.id.select_hostel)
        placeOrder=findViewById(R.id.placeOrder)

    }

    override fun onRefresh() {
        loaddata()
    }


}