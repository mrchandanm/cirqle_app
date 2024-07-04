package cirqle.com.CirqleStore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cirqle.com.CirqleStore.Adapters.ProductAdapter
import cirqle.com.CirqleStore.Models.GetProductModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CirqleStoreActivity : AppCompatActivity() {
    private lateinit var home_rv: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var search_bar: EditText
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var AllItems: TextView
    private lateinit var snacks: TextView
    private lateinit var bevrages: TextView
    private lateinit var chocolates: TextView
    private lateinit var hairCare: TextView
    private lateinit var bodyCare: TextView
    private lateinit var skinCare: TextView
    private lateinit var oralCare: TextView
    private lateinit var laundry: TextView
    private lateinit var cart:ImageView
    private lateinit var cart_qty:TextView
    private var category=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cirqle_store)

        init()
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loaddata();
            swipeRefreshLayout.isRefreshing = false
        })
        cart.setOnClickListener{
            startActivity(Intent(this@CirqleStoreActivity,CartActivity::class.java))
        }

        loaddata()
        AllItems.setBackgroundResource(R.drawable.store_tab_selected_bg)
        AllItems.setOnClickListener{
            category=""
            AllItems.setBackgroundResource(R.drawable.store_tab_selected_bg)
            snacks.setBackgroundResource(R.drawable.store_tab_unselected_bg)
            bevrages.setBackgroundResource(R.drawable.store_tab_unselected_bg)
            chocolates.setBackgroundResource(R.drawable.tab_unselected_bg)
            hairCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            bodyCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            skinCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            oralCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            laundry.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        snacks.setOnClickListener{
            category="snacks"
            AllItems.setBackgroundResource(R.drawable.tab_unselected_bg)
            snacks.setBackgroundResource(R.drawable.store_tab_selected_bg)
            bevrages.setBackgroundResource(R.drawable.tab_unselected_bg)
            chocolates.setBackgroundResource(R.drawable.tab_unselected_bg)
            hairCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            bodyCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            skinCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            oralCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            laundry.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        bevrages.setOnClickListener{
            category="bevrages"
            AllItems.setBackgroundResource(R.drawable.tab_unselected_bg)
            snacks.setBackgroundResource(R.drawable.tab_unselected_bg)
            bevrages.setBackgroundResource(R.drawable.store_tab_selected_bg)
            chocolates.setBackgroundResource(R.drawable.tab_unselected_bg)
            hairCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            bodyCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            skinCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            oralCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            laundry.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        chocolates.setOnClickListener{
            category="chocolates"
            AllItems.setBackgroundResource(R.drawable.tab_unselected_bg)
            snacks.setBackgroundResource(R.drawable.tab_unselected_bg)
            bevrages.setBackgroundResource(R.drawable.tab_unselected_bg)
            chocolates.setBackgroundResource(R.drawable.store_tab_selected_bg)
            hairCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            bodyCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            skinCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            oralCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            laundry.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        hairCare.setOnClickListener{
            category="hairCare"
            AllItems.setBackgroundResource(R.drawable.tab_unselected_bg)
            snacks.setBackgroundResource(R.drawable.tab_unselected_bg)
            bevrages.setBackgroundResource(R.drawable.tab_unselected_bg)
            chocolates.setBackgroundResource(R.drawable.tab_unselected_bg)
            hairCare.setBackgroundResource(R.drawable.store_tab_selected_bg)
            bodyCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            skinCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            oralCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            laundry.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        bodyCare.setOnClickListener{
            category="bodyCare"
            AllItems.setBackgroundResource(R.drawable.tab_unselected_bg)
            snacks.setBackgroundResource(R.drawable.tab_unselected_bg)
            bevrages.setBackgroundResource(R.drawable.tab_unselected_bg)
            chocolates.setBackgroundResource(R.drawable.tab_unselected_bg)
            hairCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            bodyCare.setBackgroundResource(R.drawable.store_tab_selected_bg)
            skinCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            oralCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            laundry.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        skinCare.setOnClickListener{
            category="skinCare"
            AllItems.setBackgroundResource(R.drawable.tab_unselected_bg)
            snacks.setBackgroundResource(R.drawable.tab_unselected_bg)
            bevrages.setBackgroundResource(R.drawable.tab_unselected_bg)
            chocolates.setBackgroundResource(R.drawable.tab_unselected_bg)
            hairCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            bodyCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            skinCare.setBackgroundResource(R.drawable.store_tab_selected_bg)
            oralCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            laundry.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        oralCare.setOnClickListener{
            category="oralcare"
            AllItems.setBackgroundResource(R.drawable.tab_unselected_bg)
            snacks.setBackgroundResource(R.drawable.tab_unselected_bg)
            bevrages.setBackgroundResource(R.drawable.tab_unselected_bg)
            chocolates.setBackgroundResource(R.drawable.tab_unselected_bg)
            hairCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            bodyCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            skinCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            oralCare.setBackgroundResource(R.drawable.store_tab_selected_bg)
            laundry.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        laundry.setOnClickListener{
            category="laundry"
            AllItems.setBackgroundResource(R.drawable.tab_unselected_bg)
            snacks.setBackgroundResource(R.drawable.tab_unselected_bg)
            bevrages.setBackgroundResource(R.drawable.tab_unselected_bg)
            chocolates.setBackgroundResource(R.drawable.tab_unselected_bg)
            hairCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            bodyCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            skinCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            oralCare.setBackgroundResource(R.drawable.tab_unselected_bg)
            laundry.setBackgroundResource(R.drawable.store_tab_selected_bg)
            loaddata()
        }

    }

    fun loaddata(){
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        home_rv.visibility= View.GONE
        val reqCall= BuilderRetrofit.builService(ApiInterface::class.java).get_product("Indian Institute of Technology, Patna",category)
        reqCall.enqueue(object : Callback<GetProductModel> {
            override fun onResponse(call: Call<GetProductModel>, response: Response<GetProductModel>) {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                home_rv.visibility= View.VISIBLE
                Log.d("getDoubt", "onsuccessful: "+response.body())
                productAdapter= ProductAdapter(this@CirqleStoreActivity,response.body()?.products!!)
                home_rv.layoutManager= LinearLayoutManager(this@CirqleStoreActivity,
                    LinearLayoutManager.VERTICAL,false)
                home_rv.adapter=productAdapter
            }

            override fun onFailure(call: Call<GetProductModel>, t: Throwable) {
                Toast.makeText(this@CirqleStoreActivity, "failed", Toast.LENGTH_SHORT).show()
                Log.d("getDoubt", "onFailure: "+t.message)
            }
        })
    }

    fun init(){
        shimmerLayout = findViewById(R.id.shimmerLayout)
        home_rv=findViewById(R.id.home_rv)
//        search_bar = findViewById(R.id.search_bar)
        AllItems=findViewById(R.id.allItems)
        snacks=findViewById(R.id.snacks)
        bevrages=findViewById(R.id.bevrages)
        chocolates=findViewById(R.id.chocolates)
        hairCare=findViewById(R.id.hairCare)
        bodyCare=findViewById(R.id.bodyCare)
        skinCare=findViewById(R.id.skinCare)
        oralCare=findViewById(R.id.oralCare)
        laundry=findViewById(R.id.laundry)
        cart=findViewById(R.id.cart)
    }
}