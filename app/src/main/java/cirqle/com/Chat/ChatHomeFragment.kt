package cirqle.com.Chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.Chat.Adapters.ChatAdapter
import cirqle.com.Chat.Models.FetchChatResponseModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatHomeFragment : Fragment() {
    private lateinit var chat_rv:RecyclerView
    private lateinit var search_bar:EditText
    private lateinit var adapter:ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View =inflater.inflate(R.layout.fragment_chat_home, container, false)

        chat_rv=view.findViewById(R.id.chat_rv)
        search_bar = view.findViewById(R.id.search_bar)

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


        val userId= Utility.getUserDetails(requireContext())?._id!!
        val getmessageService = BuilderRetrofit.builService(ApiInterface::class.java)
        val reqCall=getmessageService.fetchChats(userId)
        reqCall.enqueue(object:Callback<FetchChatResponseModel>{
            override fun onResponse(call: Call<FetchChatResponseModel>, response: Response<FetchChatResponseModel>) {
              if(response.isSuccessful){
                  Log.d("responsebody", "onResponse: "+response.body()?.result)
                  val layoutManager=LinearLayoutManager(context)
                  chat_rv.layoutManager=layoutManager
                  adapter=ChatAdapter(requireContext(),response.body()?.result!!, userId)
                  chat_rv.adapter=adapter
              }
                else{
                  Log.d("response", "onResponse: "+response.message())
              }
            }

            override fun onFailure(call: Call<FetchChatResponseModel>, t: Throwable) {
                Log.d("chat", "onFailure: "+t.message)
            }
        })

        return view
    }
}