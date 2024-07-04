package cirqle.com.Account

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import cirqle.com.CirqleStore.CirqleStoreActivity
import cirqle.com.Home.HomeActivity
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplaintActivity : AppCompatActivity() {
    private lateinit var complaint_box:TextView
    private lateinit var submit_btn:AppCompatButton
    private lateinit var close_btn:ImageButton
    private lateinit var progressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint)
        init()
        progressDialog = Dialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setContentView(R.layout.layout_loading_progress_dialog)
        submit_btn.setOnClickListener{
            progressDialog.show()
            val complaint=complaint_box.text.toString()
            val userId=Utility.getUserDetails(this)?._id
            val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).register_complaint(userId!!,complaint)
            reqCall.enqueue(object:Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Toast.makeText(this@ComplaintActivity, "Complaint Registered", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this@ComplaintActivity, HomeActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                    startActivity(intent)
                    progressDialog.dismiss()
                    finish()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@ComplaintActivity, "Complaint Registered", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this@ComplaintActivity, HomeActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                    startActivity(intent)
                    progressDialog.dismiss()
                    finish()
                }

            })

        }
        close_btn.setOnClickListener{
//            val intent = Intent(this@ComplaintActivity, HomeActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
            finish()
        }
    }
    fun init(){
        complaint_box=findViewById(R.id.complaint)
        submit_btn=findViewById(R.id.submit_button)
        close_btn=findViewById(R.id.close_btn)
    }
    override fun onBackPressed() {
        // Restart the activity
//        val intent = Intent(this@ComplaintActivity, HomeActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
        finish()
    }
}