package cirqle.com.Account

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cirqle.com.Account.Adapters.OrdersAdapter
import cirqle.com.Account.Models.GetOrdersModel
import cirqle.com.Account.Models.ProfileUpdateResponse
import cirqle.com.Authentication.LoginActivity
import cirqle.com.BuyAndSell.Adapters.HomeRVPostAdapter
import cirqle.com.BuyAndSell.Adapters.ImageShowViewPagerAdapter
import cirqle.com.BuyAndSell.Models.BuySellresponseModel
import cirqle.com.BuyAndSell.Models.GetPostResponseModel
import cirqle.com.LostAndFound.Adapters.PostAdapter
import cirqle.com.LostAndFound.Models.AddPostResponseModel
import cirqle.com.R
import cirqle.com.SeniorCirqle.Adapters.AskDoubtsPostAdapter
import cirqle.com.SeniorCirqle.Models.GetDoubtsResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import okhttp3.internal.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountFragment : Fragment() {
   private lateinit var logout:TextView
    private lateinit var name:TextView
    private lateinit var collegeName:TextView
    private lateinit var phone:TextView
    private lateinit var profilePic:ImageView
    private lateinit var updateProfilePic:RelativeLayout
     private lateinit var selectedImage:Uri
     private lateinit var myOrders:TextView
    private lateinit var myBuyAndSell:TextView
    private lateinit var myLostPost:TextView
    private lateinit var myFoundPost:TextView
    private lateinit var myFunFeed:TextView
    private lateinit var myDoubtPost:TextView
    private lateinit var complaint:TextView
    private lateinit var home_rv:RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var shimmerLayout: ShimmerFrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_account, container, false)
        logout=view.findViewById(R.id.logout_btn)
        complaint=view.findViewById(R.id.complaint)
        profilePic=view.findViewById(R.id.profilePic)
        name=view.findViewById(R.id.name)
        phone=view.findViewById(R.id.phone)
        collegeName=view.findViewById(R.id.collegeName)
        updateProfilePic=view.findViewById(R.id.updateProfilePic)
        myOrders=view.findViewById(R.id.myOrders)
        myBuyAndSell=view.findViewById(R.id.myBuyAndSell)
        myDoubtPost=view.findViewById(R.id.myDoubtPost)
        myFunFeed=view.findViewById(R.id.myFunFeed)
        myLostPost=view.findViewById(R.id.myLostPost)
        myFoundPost=view.findViewById(R.id.myFoundPost)
        home_rv=view.findViewById(R.id.home_rv)
        shimmerLayout = view.findViewById(R.id.shimmerLayout)

        val userId=Utility.getUserDetails(requireContext())?._id
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        home_rv.visibility= View.GONE
        val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).get_user_orders(userId!!)
        myOrders.setBackgroundResource(R.drawable.selected_tab_bg)
        reqCall.enqueue(object :Callback<ArrayList<GetOrdersModel>>{
            override fun onResponse(
                call: Call<ArrayList<GetOrdersModel>>,
                response: Response<ArrayList<GetOrdersModel>>
            ) {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                home_rv.visibility= View.VISIBLE
                val orderAdapter= OrdersAdapter(requireContext(),response.body()!!)
                home_rv.layoutManager= LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,false)
                home_rv.adapter=orderAdapter
            }

            override fun onFailure(call: Call<ArrayList<GetOrdersModel>>, t: Throwable) {
                Log.d("user", "onResponse: "+t.message)
            }

        })
        myOrders.setOnClickListener{
            myOrders.setBackgroundResource(R.drawable.selected_tab_bg)
            myBuyAndSell.setBackgroundResource(R.drawable.tab_unselected_bg)
            myFoundPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            myLostPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            myFunFeed.setBackgroundResource(R.drawable.tab_unselected_bg)
            myDoubtPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            shimmerLayout.startShimmer()
            shimmerLayout.visibility = View.VISIBLE
            home_rv.visibility= View.GONE
            val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).get_user_orders(userId!!)
            reqCall.enqueue(object :Callback<ArrayList<GetOrdersModel>>{
                override fun onResponse(
                    call: Call<ArrayList<GetOrdersModel>>,
                    response: Response<ArrayList<GetOrdersModel>>
                ) {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    home_rv.visibility= View.VISIBLE
                    val orderAdapter= OrdersAdapter(requireContext(),response.body()!!)
                    home_rv.layoutManager= LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL,false)
                    home_rv.adapter=orderAdapter
                }

                override fun onFailure(call: Call<ArrayList<GetOrdersModel>>, t: Throwable) {
                    Log.d("user", "onResponse: "+t.message)
                }

            })
        }
        myBuyAndSell.setOnClickListener{
            myOrders.setBackgroundResource(R.drawable.tab_unselected_bg)
            myBuyAndSell.setBackgroundResource(R.drawable.selected_tab_bg)
            myFoundPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            myLostPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            myFunFeed.setBackgroundResource(R.drawable.tab_unselected_bg)
            myDoubtPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            shimmerLayout.startShimmer()
            shimmerLayout.visibility = View.VISIBLE
            home_rv.visibility= View.GONE
            val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).get_user_buy_sell(userId!!)
            reqCall.enqueue(object :Callback<GetPostResponseModel>{
                override fun onResponse(
                    call: Call<GetPostResponseModel>,
                    response: Response<GetPostResponseModel>
                ) {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    home_rv.visibility= View.VISIBLE
                    val list=response.body()?.post!!
                    if(list.size> 0) {
                        val adapter = HomeRVPostAdapter(requireContext(), response.body()?.post!!)
                        home_rv.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        home_rv.adapter = adapter
                    }
                    Log.d("user", "onResponse: "+response.body())
                }
                override fun onFailure(call: Call<GetPostResponseModel>, t: Throwable) {
                    Log.d("user", "onResponse: "+t.message)
                }

            })
        }
        myLostPost.setOnClickListener{
            myOrders.setBackgroundResource(R.drawable.tab_unselected_bg)
            myBuyAndSell.setBackgroundResource(R.drawable.tab_unselected_bg)
            myFoundPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            myLostPost.setBackgroundResource(R.drawable.selected_tab_bg)
            myFunFeed.setBackgroundResource(R.drawable.tab_unselected_bg)
            myDoubtPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            shimmerLayout.startShimmer()
            shimmerLayout.visibility = View.VISIBLE
            home_rv.visibility= View.GONE
            val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).get_user_lost_found(userId!!,"Lost")
            reqCall.enqueue(object :Callback<ArrayList<AddPostResponseModel>>{
                override fun onResponse(
                    call: Call<ArrayList<AddPostResponseModel>>,
                    response: Response<ArrayList<AddPostResponseModel>>
                ) {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    home_rv.visibility= View.VISIBLE
                    val adapter = PostAdapter(requireContext(), response.body()!!)
                    home_rv.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    home_rv.adapter = adapter
                }

                override fun onFailure(call: Call<ArrayList<AddPostResponseModel>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
        myFoundPost.setOnClickListener{
            myOrders.setBackgroundResource(R.drawable.tab_unselected_bg)
            myBuyAndSell.setBackgroundResource(R.drawable.tab_unselected_bg)
            myFoundPost.setBackgroundResource(R.drawable.selected_tab_bg)
            myLostPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            myFunFeed.setBackgroundResource(R.drawable.tab_unselected_bg)
            myDoubtPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            shimmerLayout.startShimmer()
            shimmerLayout.visibility = View.VISIBLE
            home_rv.visibility= View.GONE
            val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).get_user_lost_found(userId!!,"Found")
            reqCall.enqueue(object :Callback<ArrayList<AddPostResponseModel>>{
                override fun onResponse(
                    call: Call<ArrayList<AddPostResponseModel>>,
                    response: Response<ArrayList<AddPostResponseModel>>
                ) {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    home_rv.visibility= View.VISIBLE
                    val adapter = PostAdapter(requireContext(), response.body()!!)
                    home_rv.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    home_rv.adapter = adapter
                }

                override fun onFailure(call: Call<ArrayList<AddPostResponseModel>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
        myFunFeed.setOnClickListener{
            myOrders.setBackgroundResource(R.drawable.tab_unselected_bg)
            myBuyAndSell.setBackgroundResource(R.drawable.tab_unselected_bg)
            myFoundPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            myLostPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            myFunFeed.setBackgroundResource(R.drawable.selected_tab_bg)
            myDoubtPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            shimmerLayout.startShimmer()
            shimmerLayout.visibility = View.VISIBLE
            home_rv.visibility= View.GONE
            val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).get_user_feed(userId!!,"funFeedPost")
            reqCall.enqueue(object :Callback<GetDoubtsResponseModel>{
                override fun onResponse(
                    call: Call<GetDoubtsResponseModel>,
                    response: Response<GetDoubtsResponseModel>
                ) {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    home_rv.visibility= View.VISIBLE
                    val postAdapter= AskDoubtsPostAdapter(requireContext(),response.body()?.post!!)
                    home_rv.layoutManager= LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL,false)
                    home_rv.adapter=postAdapter
                    Log.d("user", "onResponse: "+response.body())
                }

                override fun onFailure(call: Call<GetDoubtsResponseModel>, t: Throwable) {
                    Log.d("user", "onResponse: "+t.message)
                }

            })
        }
        myDoubtPost.setOnClickListener{
            myOrders.setBackgroundResource(R.drawable.tab_unselected_bg)
            myBuyAndSell.setBackgroundResource(R.drawable.tab_unselected_bg)
            myFoundPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            myLostPost.setBackgroundResource(R.drawable.tab_unselected_bg)
            myFunFeed.setBackgroundResource(R.drawable.tab_unselected_bg)
            myDoubtPost.setBackgroundResource(R.drawable.selected_tab_bg)
            shimmerLayout.startShimmer()
            shimmerLayout.visibility = View.VISIBLE
            home_rv.visibility= View.GONE
            val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).get_user_feed(userId!!,"doubtPost")
            reqCall.enqueue(object :Callback<GetDoubtsResponseModel>{
                override fun onResponse(
                    call: Call<GetDoubtsResponseModel>,
                    response: Response<GetDoubtsResponseModel>
                ) {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    home_rv.visibility= View.VISIBLE
                    val postAdapter= AskDoubtsPostAdapter(requireContext(),response.body()?.post!!)
                    home_rv.layoutManager= LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL,false)
                    home_rv.adapter=postAdapter
                    Log.d("user", "onResponse: "+response.body())
                }

                override fun onFailure(call: Call<GetDoubtsResponseModel>, t: Throwable) {
                    Log.d("user", "onResponse: "+t.message)
                }

            })
        }

        complaint.setOnClickListener{
            startActivity(Intent(requireContext(),ComplaintActivity::class.java))
        }
        logout.setOnClickListener{
           showLogoutConfirmationDialog()
        }


        val user=Utility.getUserDetails(requireContext())
if(!user?.profilePic.isNullOrBlank()) {
    Picasso.get().load(user?.profilePic).into(profilePic)
}
        name.text=user?.name.toString()
        collegeName.text=user?.collegeName.toString()
        phone.text=user?.phone.toString()
        updateProfilePic.setOnClickListener{
            openGallery()
        }


        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//            val selectedImageUris = mutableListOf<Uri>()
                val data = result.data
                selectedImage=data?.data!!
                profilePic.setImageURI(selectedImage)
                val userId=Utility.getUserDetails(requireContext())?._id
                val UserId=Utility.getUserDetails(requireContext())?._id
                val bitmap=Utility.getImageBitmap(selectedImage,requireContext())
                val compressedImage = Utility.compressImage(bitmap!!)
                val imageName = "image_${System.currentTimeMillis()}.jpg"
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("images/$imageName")
                val uploadtask=imageRef.putBytes(compressedImage).addOnCompleteListener(
                    OnCompleteListener { task->
                        if(task.isSuccessful){
                            imageRef.downloadUrl.addOnSuccessListener { url->
                                val reqCall =BuilderRetrofit.builService(ApiInterface::class.java).update_profilePic(userId!!, url.toString())
                                reqCall.enqueue(object : Callback<ProfileUpdateResponse>{
                                    override fun onResponse(call: Call<ProfileUpdateResponse>, response: Response<ProfileUpdateResponse>
                                    ) {
                                        val user=Utility.getUserDetails(requireContext())
                                        user?.let {
                                            it.profilePic = url.toString()
                                            Utility.saveObjectLocally(requireContext(),"userDetails",it!!)
                                        }
                                        Toast.makeText(context, "uploaded succesfully", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onFailure(call: Call<ProfileUpdateResponse>, t: Throwable) {
                                        Log.d("profile", "onFailure: "+t.message)
                                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                                    }

                                })
                             }
                        }
                     })
            }
        }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            // Perform the logout action
            performLogout()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun performLogout() {
        FirebaseAuth.getInstance().signOut();
        Utility.deleteDataLocally(requireContext(),"token")
        Utility.deleteDataLocally(requireContext(),"userDetails")
        startActivity(Intent(context,LoginActivity::class.java))
        activity?.finish()
    }

}