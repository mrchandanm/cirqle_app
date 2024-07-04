package cirqle.com.Authentication

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import cirqle.com.Authentication.Models.RegisterResponseModel
import cirqle.com.Home.HomeActivity
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var register_page: TextView
    private lateinit var phone_et: TextInputEditText
    private lateinit var password_et: TextInputEditText
    private lateinit var submit_btn: AppCompatButton
    private lateinit var auth: FirebaseAuth;
    private lateinit var phone:String
    private lateinit var password:String
    private lateinit var progressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        phone_et=findViewById(R.id.phone_et)
        password_et=findViewById(R.id.password_et)
        submit_btn=findViewById(R.id.submit_btn)
        register_page=findViewById(R.id.register_page)

        progressDialog = Dialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setContentView(R.layout.layout_loading_progress_dialog)
        val progressBar = progressDialog.findViewById<LottieAnimationView>(R.id.progress_bar)
        auth=FirebaseAuth.getInstance()
        submit_btn.setOnClickListener{
            progressDialog.show()
            if(phone_et.text.isNullOrBlank()){
                phone_et.setError("Please Enter Phone No.")
                progressDialog.dismiss()
                return@setOnClickListener
            }
            if(password_et.text.isNullOrBlank()){
                phone_et.setError("password is required")
                progressDialog.dismiss()
                return@setOnClickListener
            }
            phone=phone_et.text.toString()
            password=password_et.text.toString()
            val registerService = BuilderRetrofit.builService(ApiInterface::class.java)
            val reqCall=registerService.login(phone_et.text.toString(),password_et.text.toString())
            reqCall.enqueue(object :Callback<RegisterResponseModel>{
                override fun onResponse(call: Call<RegisterResponseModel>, response: Response<RegisterResponseModel>
                ) {
                    if(response.isSuccessful){
                        val userDetails=response.body()?.user;
                        Log.d("user", "onResponse: "+userDetails)
                        Toast.makeText(this@LoginActivity, "Login Succesfully", Toast.LENGTH_SHORT).show()
                        Utility.saveObjectLocally(this@LoginActivity,"userDetails",userDetails!!)
                        val token=response.body()?.token
                        Utility.saveStringLocally(this@LoginActivity,"token",token!!)
                        auth.signInWithCustomToken(token)
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                        progressDialog.dismiss()
                    }
                    else{
                        phone_et.setError("Invalid Credentials")
                        password_et.setError("Invalid Credentials")
                        Log.d("logout", "onResponse: "+phone+" "+password)
                        progressDialog.dismiss()
                    }
                }
                override fun onFailure(call: Call<RegisterResponseModel>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "login Failed", Toast.LENGTH_SHORT).show()
                    Log.d("login", "onFailure: "+t.message)
                    progressDialog.dismiss()
                }
            })
        }

        register_page.setOnClickListener{
            startActivity(Intent(this,PhoneVerifiationActivity::class.java))
            finish()
        }
    }
}