package cirqle.com.LostAndFound

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import cirqle.com.BuyAndSell.AddBuyAndSellActivity
import cirqle.com.FunFeeds.AddFeedActivity
import cirqle.com.LostAndFound.Adapters.PostAdapter
import cirqle.com.LostAndFound.Adapters.TabViewPagerAdapter
import cirqle.com.LostAndFound.Models.AddPostResponseModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LostAndFoundHomeActivity : AppCompatActivity() {
    private lateinit var add_post_fab_btn:FloatingActionButton
    private lateinit var home_rv:RecyclerView
    private lateinit var adapter:PostAdapter
    private lateinit var found:TextView
    private lateinit var lost:TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private val REQUEST_STORAGE=1
    private var category:String="Found"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost_and_found_home)
        val collegeName=Utility.getUserDetails(this@LostAndFoundHomeActivity)?.collegeName

        init()
//        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
//        val viewPager2: ViewPager2 = findViewById(R.id.viewPager)
//        val adapter = TabViewPagerAdapter(this)

//        viewPager2.adapter = adapter
//
//        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
//            if(position==0) tab.text = "Found"
//            else tab.text = "Lost"
//        }.attach()


//        val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).get_lost_found_post(collegeName!!)
//        reqCall.enqueue(object :Callback<ArrayList<AddPostResponseModel>>{
//            override fun onResponse(call: Call<ArrayList<AddPostResponseModel>>, response: Response<ArrayList<AddPostResponseModel>>) {
//                adapter= PostAdapter(this@LostAndFoundHomeActivity,response.body()!!)
//                home_rv.layoutManager=LinearLayoutManager(this@LostAndFoundHomeActivity)
//                home_rv.adapter=adapter
//                Log.d("getlaf", "onFailure: "+response.body())
//            }
//
//            override fun onFailure(call: Call<ArrayList<AddPostResponseModel>>, t: Throwable) {
//                Toast.makeText(this@LostAndFoundHomeActivity, "failed", Toast.LENGTH_SHORT).show()
//                Log.d("getlaf", "onFailure: "+t.message)
//            }
//        })
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loaddata();
            swipeRefreshLayout.isRefreshing = false
        })
        loaddata()
        found.setBackgroundResource(R.drawable.selected_tab_bg)

        found.setOnClickListener{
            category="Found"
            found.setBackgroundResource(R.drawable.selected_tab_bg)
            lost.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        lost.setOnClickListener{
            category="Lost"
            found.setBackgroundResource(R.drawable.tab_unselected_bg)
            lost.setBackgroundResource(R.drawable.selected_tab_bg)
            loaddata()
        }
        add_post_fab_btn.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), REQUEST_STORAGE)
            }
        }



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
                val intent = Intent(this, AddLostAndFoundPost::class.java)
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

    private fun loaddata() {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        home_rv.visibility= View.GONE
        val collegeName= Utility.getUserDetails(this)?.collegeName
        val reqCall= BuilderRetrofit.builService(ApiInterface::class.java).get_lost_found_post(collegeName!!,category)
        reqCall.enqueue(object : Callback<ArrayList<AddPostResponseModel>> {
            override fun onResponse(call: Call<ArrayList<AddPostResponseModel>>, response: Response<ArrayList<AddPostResponseModel>>) {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                home_rv.visibility= View.VISIBLE
                adapter= PostAdapter(this@LostAndFoundHomeActivity,response.body()!!)
                home_rv.layoutManager= LinearLayoutManager(this@LostAndFoundHomeActivity)
                home_rv.adapter=adapter
                Log.d("getlaf", "onFailure: "+response.body())
            }

            override fun onFailure(call: Call<ArrayList<AddPostResponseModel>>, t: Throwable) {
                Toast.makeText(this@LostAndFoundHomeActivity, "failed", Toast.LENGTH_SHORT).show()
                Log.d("getlaf", "onFailure: "+t.message)
            }
        })
    }

    fun init(){
//        home_rv=findViewById(R.id.home_rv)
        add_post_fab_btn=findViewById(R.id.add_post_fab_btn)
        home_rv=findViewById(R.id.home_rv)
        found=findViewById(R.id.found)
        lost=findViewById(R.id.lost)
        shimmerLayout = findViewById(R.id.shimmerLayout)
    }
}