package cirqle.com.BuyAndSell

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cirqle.com.Adapters.HomePageHorizontalRecyclerViewAdapter
import cirqle.com.BuyAndSell.Adapters.HomeRVPostAdapter
import cirqle.com.BuyAndSell.Models.BuySellresponseModel
import cirqle.com.BuyAndSell.Models.GetPostResponseModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class BuyAndSellHomeActivity : AppCompatActivity() {
    private lateinit var buy_sell_home_rv:RecyclerView
    private lateinit var rvadapter: HomeRVPostAdapter
    private lateinit var buy_sell_fab_btn:FloatingActionButton
    private lateinit var list:ArrayList<BuySellresponseModel>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private val REQUEST_STORAGE=1

    private val REQUEST_CODE = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_and_sell_home)
        buy_sell_home_rv=findViewById(R.id.buy_sell_home_rv)
        buy_sell_fab_btn=findViewById(R.id.buy_sell_fab_btn)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        shimmerLayout = findViewById(R.id.shimmerLayout)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loaddata();
            swipeRefreshLayout.isRefreshing = false
        })
        loaddata();
        buy_sell_fab_btn.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), REQUEST_STORAGE)
            }

        }




    }

private fun loaddata(){
    shimmerLayout.startShimmer()
    shimmerLayout.visibility = View.VISIBLE
    buy_sell_home_rv.visibility= View.GONE
    list= ArrayList()
    val getAdsService = BuilderRetrofit.builService(ApiInterface::class.java)
    val collegeName= Utility.getUserDetails(this@BuyAndSellHomeActivity)?.collegeName!!
    val reqCall=getAdsService.getAds(collegeName)
    reqCall.enqueue(object:Callback<GetPostResponseModel>{
        override fun onResponse(call: Call<GetPostResponseModel>, response: Response<GetPostResponseModel>) {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            buy_sell_home_rv.visibility= View.VISIBLE
            list=response.body()?.post!!
            rvadapter= HomeRVPostAdapter(this@BuyAndSellHomeActivity,list)
            buy_sell_home_rv.layoutManager= LinearLayoutManager(this@BuyAndSellHomeActivity, LinearLayoutManager.VERTICAL,false)
            buy_sell_home_rv.adapter=rvadapter
        }

        override fun onFailure(call: Call<GetPostResponseModel>, t: Throwable) {
            Log.d("failed", "onFailure: "+t.message)
        }
    })
}
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUris = mutableListOf<Uri>()
            val data = result.data
            data?.let { intent ->
                if (intent.clipData != null) {
                    val count = intent.clipData!!.itemCount
                    for (i in 0 until count) {
                        val uri = intent.clipData!!.getItemAt(i).uri
                        selectedImageUris.add(uri)
                    }
                } else if (intent.data != null) {
                    val uri = intent.data!!
                    selectedImageUris.add(uri)
                }
            }

            if (selectedImageUris.isNotEmpty()) {
                val intent = Intent(this, AddBuyAndSellActivity::class.java)
                intent.putExtra("imageUris", selectedImageUris.map { it.toString() }.toTypedArray())
                startActivity(intent)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==REQUEST_STORAGE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the call
               openGallery()
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}