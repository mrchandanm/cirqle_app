package cirqle.com.TravelBuddy

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.R
import cirqle.com.SeniorCirqle.Models.AddCommentResponseModel
import cirqle.com.TravelBuddy.Adapter.TripPostAdapter
import cirqle.com.TravelBuddy.Models.AddTripResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.PermissionCallback
import cirqle.com.Utils.Utility
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TravelBuddyHomeActivity : AppCompatActivity(),PermissionCallback {
    private lateinit var search_bar:EditText
    private lateinit var adapter:TripPostAdapter
    private lateinit var home_rv:RecyclerView
    private lateinit var add_post_fab_btn:FloatingActionButton
    private lateinit var filter:AppCompatButton
    private  var list=mutableListOf<AddTripResponseModel>()
    private var filteredDate:String=""
    private val REQUEST_CALL_PHONE=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_buddy_home)

        init()
        filter.setOnClickListener{
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.layout_calendar_dialog)
                val calendarView: CalendarView = dialog.findViewById(R.id.calendarView)
                // Set initial display to current year
//                calendarView.maxDate = System.currentTimeMillis()

                calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    // Use the selectedDate as needed
                    filteredDate=selectedDate
                    adapter.filter.filter(filteredDate)
                    dialog.dismiss()
                }
                dialog.show()


        }


        val user= Utility.getUserDetails(this)?._id
        val collegeName= Utility.getUserDetails(this)?.collegeName
        adapter= TripPostAdapter(this,list,this)
        add_post_fab_btn.setOnClickListener{
            startActivity(Intent(this@TravelBuddyHomeActivity,AddTripActivity::class.java))
            finish()
        }


        val getReqCall= BuilderRetrofit.builService(ApiInterface::class.java).get_trip(collegeName!!)
        getReqCall.enqueue(object : Callback<ArrayList<AddTripResponseModel>> {
            override fun onResponse(call: Call<ArrayList<AddTripResponseModel>>, response: Response<ArrayList<AddTripResponseModel>>) {
                if(response.isSuccessful){
                    list=response.body()!!
                    adapter= TripPostAdapter(this@TravelBuddyHomeActivity,list,this@TravelBuddyHomeActivity)
                    val layoutManager=LinearLayoutManager(this@TravelBuddyHomeActivity)
                    home_rv.layoutManager=layoutManager
                    home_rv.adapter=adapter
                    Log.d("travel", "onFailure: "+response.body())
                }
                else{
                }

            }

            override fun onFailure(call: Call<ArrayList<AddTripResponseModel>>, t: Throwable) {
                Log.d("travel", "onFailure: "+t.message)
            }
        })


        search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                adapter.filter.filter(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                adapter.filter.filter(searchText)
            }
        })






    }

    override fun requestPermission(permission: String) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CALL_PHONE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==REQUEST_CALL_PHONE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the call
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun init(){
        home_rv=findViewById(R.id.home_rv)
        add_post_fab_btn=findViewById(R.id.add_post_fab_btn)
        search_bar = findViewById(R.id.search_bar)
        filter=findViewById(R.id.filter)
    }
}