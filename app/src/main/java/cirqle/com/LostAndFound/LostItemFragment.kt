package cirqle.com.LostAndFound

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cirqle.com.LostAndFound.Adapters.PostAdapter
import cirqle.com.LostAndFound.Models.AddPostResponseModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LostItemFragment : Fragment() {
    private lateinit var home_rv: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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
        val view=inflater.inflate(R.layout.fragment_lost_item, container, false)


        home_rv=view.findViewById(R.id.home_rv)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Toast.makeText(requireContext(), "refresh", Toast.LENGTH_SHORT).show()
         loaddata()
            swipeRefreshLayout.isRefreshing = false
        })
        loaddata()



        return view
    }
    private fun loaddata(){
        val collegeName= Utility.getUserDetails(requireContext())?.collegeName
        val reqCall= BuilderRetrofit.builService(ApiInterface::class.java).get_lost_found_post(collegeName!!,"Lost")
        reqCall.enqueue(object : Callback<ArrayList<AddPostResponseModel>> {
            override fun onResponse(call: Call<ArrayList<AddPostResponseModel>>, response: Response<ArrayList<AddPostResponseModel>>) {
                adapter= PostAdapter(requireContext(),response.body()!!)

                home_rv.layoutManager= LinearLayoutManager(requireContext())
                home_rv.adapter=adapter
                Log.d("getlaf", "onFailure: "+response.body())
            }

            override fun onFailure(call: Call<ArrayList<AddPostResponseModel>>, t: Throwable) {
                Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                Log.d("getlaf", "onFailure: "+t.message)
            }
        })
    }

}