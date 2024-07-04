package cirqle.com.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.Adapters.HomePageHorizontalRecyclerViewAdapter
import cirqle.com.BuyAndSell.Adapters.HomeRVPostAdapter
import cirqle.com.BuyAndSell.BuyAndSellHomeActivity
import cirqle.com.BuyAndSell.Models.BuySellresponseModel
import cirqle.com.BuyAndSell.Models.GetPostResponseModel
import cirqle.com.DateSpark.DateSparkHomeActivity
import cirqle.com.DateSpark.RegistrationForDateSparkActivity
import cirqle.com.FunFeeds.FunFeedActivity
import cirqle.com.HomeHorizontalRVmodel
import cirqle.com.LostAndFound.Adapters.PostAdapter
import cirqle.com.LostAndFound.LostAndFoundHomeActivity
import cirqle.com.LostAndFound.Models.AddPostResponseModel
import cirqle.com.R
import cirqle.com.SeniorCirqle.SeniorCirqleHomeActivity
import cirqle.com.SkillSwap.SkillsSwapHomeActivity
import cirqle.com.SomethingWentWrongActivity
import cirqle.com.TravelBuddy.Adapter.TripPostAdapter
import cirqle.com.TravelBuddy.Models.AddTripResponseModel
import cirqle.com.TravelBuddy.TravelBuddyHomeActivity
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.PermissionCallback
import cirqle.com.Utils.Utility
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var buy_and_sell_icon:LinearLayout
    private lateinit var lost_and_found_icon:LinearLayout
    private lateinit var travel_buddy_icon:LinearLayout
    private lateinit var fun_feed_icon:LinearLayout
    private lateinit var date_spark_icon:LinearLayout
    private lateinit var rent_items_icon:LinearLayout
    private lateinit var senior_cirqle_icon:LinearLayout

    private lateinit var buy_sell_rv:RecyclerView
    private lateinit var lost_rv:RecyclerView
    private lateinit var travel_buddy_rv:RecyclerView
    private lateinit var Found_rv:RecyclerView
    private lateinit var find_date_rv:RecyclerView
    private lateinit var rvadapter:HomePageHorizontalRecyclerViewAdapter
    private lateinit var list:ArrayList<HomeHorizontalRVmodel>
    private lateinit var BuyAndSelladapter:HomeRVPostAdapter

    private lateinit var buyAndSelllist:List<BuySellresponseModel>
    private lateinit var travelBuddyList:List<AddTripResponseModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View=inflater.inflate(R.layout.fragment_home, container, false)

        buy_and_sell_icon=view.findViewById(R.id.buy_and_sell_icon)
        lost_and_found_icon=view.findViewById(R.id.lost_and_found_icon)
        travel_buddy_icon=view.findViewById(R.id.travel_buddy_icon)
        fun_feed_icon=view.findViewById(R.id.fun_feed_icon)
        date_spark_icon=view.findViewById(R.id.date_spark_icon)
        senior_cirqle_icon=view.findViewById(R.id.senior_cirqle_icon)
        buy_sell_rv=view.findViewById(R.id.buy_sell_rv)
        val college_name:TextView=view.findViewById(R.id.college_name)
        college_name.text=Utility.getUserDetails(requireContext())?.collegeName.toString()
        val name_tv=view.findViewById<TextView>(R.id.name_tv)
        name_tv.text=Utility.getUserDetails(requireContext())?.name.toString()

        buy_and_sell_icon.setOnClickListener{
            startActivity(Intent(context,BuyAndSellHomeActivity::class.java))
        }
        lost_and_found_icon.setOnClickListener{
            startActivity(Intent(context,LostAndFoundHomeActivity::class.java))
        }
        travel_buddy_icon.setOnClickListener{
            startActivity(Intent(context,TravelBuddyHomeActivity::class.java))
        }
        fun_feed_icon.setOnClickListener{
            startActivity(Intent(context,FunFeedActivity::class.java))
        }
        date_spark_icon.setOnClickListener{
            startActivity(Intent(context,RegistrationForDateSparkActivity::class.java))
        }
        senior_cirqle_icon.setOnClickListener{
            startActivity(Intent(context,SeniorCirqleHomeActivity::class.java))
        }

        view.findViewById<TextView>(R.id.buynsell_tab).setOnClickListener{
            startActivity(Intent(context,BuyAndSellHomeActivity::class.java))
        }
        view.findViewById<TextView>(R.id.lostnfound_tab).setOnClickListener{
            startActivity(Intent(context,LostAndFoundHomeActivity::class.java))
        }
        view.findViewById<TextView>(R.id.travelbuddy_tab).setOnClickListener{
            startActivity(Intent(context,TravelBuddyHomeActivity::class.java))
        }
        view.findViewById<TextView>(R.id.fun_feed).setOnClickListener{
            startActivity(Intent(context,FunFeedActivity::class.java))
        }
        view.findViewById<TextView>(R.id.datespark_tab).setOnClickListener{
            startActivity(Intent(context,RegistrationForDateSparkActivity::class.java))
        }
        view.findViewById<TextView>(R.id.doubtcirqle_tab).setOnClickListener{
            startActivity(Intent(context,SeniorCirqleHomeActivity::class.java))
        }

        val buyandsell_shimmerLayout: ShimmerFrameLayout = view.findViewById(R.id.buyandsell_shimmerLayout)
        buyandsell_shimmerLayout.startShimmer()
        buyandsell_shimmerLayout.visibility = View.VISIBLE
        buyAndSelllist= ArrayList()
        buy_sell_rv.visibility=View.GONE
        val getAdsService = BuilderRetrofit.builService(ApiInterface::class.java)
        val collegeName= Utility.getUserDetails(requireContext())?.collegeName!!
        val BuyAndSellreqCall=getAdsService.getAds(collegeName)
        BuyAndSellreqCall.enqueue(object: Callback<GetPostResponseModel> {
            override fun onResponse(call: Call<GetPostResponseModel>, response: Response<GetPostResponseModel>) {
                if(response.isSuccessful) {
                    buyandsell_shimmerLayout.stopShimmer()
                    buyandsell_shimmerLayout.visibility = View.GONE
                    buy_sell_rv.visibility=View.VISIBLE
                    val tlist=response.body()?.post!!
                    if (tlist.size> 10) {
                        buyAndSelllist = response.body()?.post?.take(10)!!
                    } else {
                        buyAndSelllist = response.body()?.post!!
                    }
                    BuyAndSelladapter = HomeRVPostAdapter(requireContext(), buyAndSelllist)
                    buy_sell_rv.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    buy_sell_rv.adapter = BuyAndSelladapter
                }
            }

            override fun onFailure(call: Call<GetPostResponseModel>, t: Throwable) {
                Log.d("failed", "onFailure: "+t.message)
            }
        })
        val  buy_and_sell_viewAll:TextView=view.findViewById(R.id.buy_and_sell_viewAll)
        buy_and_sell_viewAll.setOnClickListener{
            startActivity(Intent(requireContext(),BuyAndSellHomeActivity::class.java))
        }




        lost_rv=view.findViewById(R.id.lost_rv)
        val lost_shimmerLayout: ShimmerFrameLayout = view.findViewById(R.id.lost_shimmerLayout)
        lost_shimmerLayout.startShimmer()
        lost_shimmerLayout.visibility = View.VISIBLE
        lost_rv.visibility=View.GONE
        val LostreqCall= BuilderRetrofit.builService(ApiInterface::class.java).get_lost_found_post(collegeName!!,"Lost")
        LostreqCall.enqueue(object : Callback<ArrayList<AddPostResponseModel>> {
            override fun onResponse(call: Call<ArrayList<AddPostResponseModel>>, response: Response<ArrayList<AddPostResponseModel>>) {
                if(response.isSuccessful) {
                    lost_shimmerLayout.stopShimmer()
                    lost_shimmerLayout.visibility = View.GONE
                    lost_rv.visibility=View.VISIBLE
                    val tlist=response.body()!!
                    val shortList=if(tlist.size>5) tlist.take(5)
                    else tlist
                    val adapter = PostAdapter(requireContext(), shortList)
                    lost_rv.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    lost_rv.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<AddPostResponseModel>>, t: Throwable) {
                Log.d("getlaf", "onFailure: "+t.message)
            }
        })
        val lost_viewAll:TextView=view.findViewById(R.id.lost_viewAll)
        lost_viewAll.setOnClickListener{
            startActivity(Intent(requireContext(),LostAndFoundHomeActivity::class.java))
        }

        travel_buddy_rv=view.findViewById(R.id.travel_buddy_rv)
        val travelBuddy_shimmerLayout: ShimmerFrameLayout = view.findViewById(R.id.travelBuddy_shimmerLayout)
        travelBuddy_shimmerLayout.startShimmer()
        travelBuddy_shimmerLayout.visibility = View.VISIBLE
        travel_buddy_rv.visibility=View.GONE
        val getReqCall= BuilderRetrofit.builService(ApiInterface::class.java).get_trip(collegeName!!)
        getReqCall.enqueue(object : Callback<ArrayList<AddTripResponseModel>> {
            override fun onResponse(call: Call<ArrayList<AddTripResponseModel>>, response: Response<ArrayList<AddTripResponseModel>>) {
                if(response.isSuccessful){
                    travelBuddy_shimmerLayout.stopShimmer()
                    travelBuddy_shimmerLayout.visibility = View.GONE
                    travel_buddy_rv.visibility=View.VISIBLE
                    val tlist=response.body()!!
                    if(tlist.size>5) travelBuddyList=tlist.take(5)
                    else travelBuddyList=tlist

                    val adapter= cirqle.com.Home.Adapter.TripPostAdapter(requireContext(),travelBuddyList)
                    val layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                    travel_buddy_rv.layoutManager=layoutManager
                    travel_buddy_rv.adapter=adapter
                    Log.d("travel", "onFailure: "+response.body())
                }
                else{
                }

            }

            override fun onFailure(call: Call<ArrayList<AddTripResponseModel>>, t: Throwable) {

                Log.d("travel", "onFailure: "+t.message)
            }
        })
        val travel_buddy_viewAll:TextView=view.findViewById(R.id.travel_buddy_viewAll)
        travel_buddy_viewAll.setOnClickListener{
            startActivity(Intent(requireContext(),TravelBuddyHomeActivity::class.java))
        }





        Found_rv=view.findViewById(R.id.Found_rv)
        val found_shimmerLayout: ShimmerFrameLayout = view.findViewById(R.id.found_shimmerLayout)
        found_shimmerLayout.startShimmer()
        found_shimmerLayout.visibility = View.VISIBLE
        Found_rv.visibility=View.GONE
        val FoundreqCall= BuilderRetrofit.builService(ApiInterface::class.java).get_lost_found_post(collegeName!!,"Found")
        FoundreqCall.enqueue(object : Callback<ArrayList<AddPostResponseModel>> {
            override fun onResponse(call: Call<ArrayList<AddPostResponseModel>>, response: Response<ArrayList<AddPostResponseModel>>) {
                if(response.isSuccessful) {
                    found_shimmerLayout.stopShimmer()
                    found_shimmerLayout.visibility = View.GONE
                    Found_rv.visibility=View.VISIBLE
                    val tlist=response.body()!!
                     val shortList=if(tlist.size>5) tlist.take(5)
                    else tlist
                    val adapter = PostAdapter(requireContext(), shortList)
                    Found_rv.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    Found_rv.adapter = adapter
                    Log.d("getlaf", "onFailure: " + response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<AddPostResponseModel>>, t: Throwable) {
                Log.d("getlaf", "onFailure: "+t.message)
            }
        })
        val found_viewAll:TextView=view.findViewById(R.id.found_viewAll)
        found_viewAll.setOnClickListener{
            startActivity(Intent(requireContext(),LostAndFoundHomeActivity::class.java))
        }


        return view
    }



}