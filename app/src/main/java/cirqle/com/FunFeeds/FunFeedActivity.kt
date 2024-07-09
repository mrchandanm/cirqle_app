package cirqle.com.FunFeeds

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cirqle.com.R
import cirqle.com.SeniorCirqle.Adapters.AskDoubtsPostAdapter
import cirqle.com.SeniorCirqle.AddDoubtsActivity
import cirqle.com.SeniorCirqle.Models.GetDoubtsResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FunFeedActivity : AppCompatActivity() {

    private lateinit var add_doubt_fab_btn:FloatingActionButton
    private lateinit var home_rv: RecyclerView
    private lateinit var postAdapter: AskDoubtsPostAdapter
    private lateinit var search_bar: EditText
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var today: TextView
    private lateinit var thisMonth: TextView
    private lateinit var thisWeek: TextView
    private lateinit var allTime: TextView
    private var filter=""
    private val REQUEST_STORAGE=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fun_feed)
        init()
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            loaddata();
            swipeRefreshLayout.isRefreshing = false
        })

        loaddata()
        allTime.setBackgroundResource(R.drawable.selected_tab_bg)
        allTime.setOnClickListener{
            filter=""
            allTime.setBackgroundResource(R.drawable.selected_tab_bg)
            thisWeek.setBackgroundResource(R.drawable.tab_unselected_bg)
            thisMonth.setBackgroundResource(R.drawable.tab_unselected_bg)
            today.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }

        today.setOnClickListener{
            filter="Today"
            today.setBackgroundResource(R.drawable.selected_tab_bg)
            thisWeek.setBackgroundResource(R.drawable.tab_unselected_bg)
            thisMonth.setBackgroundResource(R.drawable.tab_unselected_bg)
            allTime.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        thisWeek.setOnClickListener{
            filter="Week"
            thisWeek.setBackgroundResource(R.drawable.selected_tab_bg)
            today.setBackgroundResource(R.drawable.tab_unselected_bg)
            thisMonth.setBackgroundResource(R.drawable.tab_unselected_bg)
            allTime.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }
        thisMonth.setOnClickListener{
            filter="Month"
            thisMonth.setBackgroundResource(R.drawable.selected_tab_bg)
            thisWeek.setBackgroundResource(R.drawable.tab_unselected_bg)
            today.setBackgroundResource(R.drawable.tab_unselected_bg)
            allTime.setBackgroundResource(R.drawable.tab_unselected_bg)
            loaddata()
        }

        add_doubt_fab_btn.setOnClickListener{
            if (Build.VERSION.SDK_INT >= 33 && Build.VERSION.SDK_INT < 35) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    val intent=Intent(this@FunFeedActivity,AddFeedActivity::class.java)
                    startActivity(intent)
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), REQUEST_STORAGE)
                }
            } else {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    val intent=Intent(this@FunFeedActivity,AddFeedActivity::class.java)
                    startActivity(intent)
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE)
                }
            }



        }
        search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                postAdapter.filter.filter(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                postAdapter.filter.filter(searchText)
            }
        })
    }

    fun loaddata(){
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        home_rv.visibility= View.GONE
        val collegeName= Utility.getUserDetails(this@FunFeedActivity)?.collegeName!!
        val reqCall= BuilderRetrofit.builService(ApiInterface::class.java).get_doubts(collegeName,filter,"funFeedPost")
        reqCall.enqueue(object : Callback<GetDoubtsResponseModel> {
            override fun onResponse(call: Call<GetDoubtsResponseModel>, response: Response<GetDoubtsResponseModel>) {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                home_rv.visibility= View.VISIBLE
                Log.d("getDoubt", "onsuccessful: "+response.body()?.post)
                postAdapter= AskDoubtsPostAdapter(this@FunFeedActivity,response.body()?.post!!)
                home_rv.layoutManager= LinearLayoutManager(this@FunFeedActivity,
                    LinearLayoutManager.VERTICAL,false)
                home_rv.adapter=postAdapter
            }

            override fun onFailure(call: Call<GetDoubtsResponseModel>, t: Throwable) {
                Log.d("getDoubt", "onFailure: "+t.message)
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==REQUEST_STORAGE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the call
                val intent=Intent(this@FunFeedActivity,AddFeedActivity::class.java)
                startActivity(intent)
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Go to Setting to allow storage permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun init(){
        shimmerLayout = findViewById(R.id.shimmerLayout)
        home_rv=findViewById(R.id.home_rv)
        add_doubt_fab_btn=findViewById(R.id.add_doubt_fab_btn)
        search_bar = findViewById(R.id.search_bar)
        today=findViewById(R.id.today)
        thisWeek=findViewById(R.id.thisWeek)
        thisMonth=findViewById(R.id.thisMonth)
        allTime=findViewById(R.id.allTime)
    }

}