package cirqle.com.DateSpark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import cirqle.com.DateSpark.Adapters.HomeSliderViewPagerAdapter
import cirqle.com.DateSpark.Models.DateSparkResponseModel
import cirqle.com.DateSpark.Models.GetUserResponseModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DateSparkHomeActivity : AppCompatActivity() {
    private lateinit var viewpager:ViewPager2
    private lateinit var pg:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_spark_home)

        viewpager=findViewById(R.id.viewpager)
        pg=findViewById(R.id.pg)

        val getAdsService = BuilderRetrofit.builService(ApiInterface::class.java)
        val collegeName= Utility.getUserDetails(this@DateSparkHomeActivity)?.collegeName!!
        val gender= Utility.getUserDetails(this@DateSparkHomeActivity)?.gender!!
        val reqCall=getAdsService.datespark_get_user(collegeName,gender)
       reqCall.enqueue(object :Callback<GetUserResponseModel>{
           override fun onResponse(call: Call<GetUserResponseModel>, response: Response<GetUserResponseModel>) {
               if(response.isSuccessful){
                   pg.visibility= View.GONE
                   viewpager.visibility=View.VISIBLE
                   val adapter=HomeSliderViewPagerAdapter(this@DateSparkHomeActivity,response.body()?.post!!)
                   viewpager.adapter=adapter
               }
               else{
                   pg.visibility= View.GONE
                   viewpager.visibility=View.VISIBLE
                   Toast.makeText(this@DateSparkHomeActivity, "response unsuccesful", Toast.LENGTH_SHORT).show()
               }
           }

           override fun onFailure(call: Call<GetUserResponseModel>, t: Throwable) {
               pg.visibility= View.GONE
               viewpager.visibility=View.VISIBLE
               Log.d("dateresponse", "onFailure: "+t.message)
           }
       })
    }
}