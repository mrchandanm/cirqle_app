package cirqle.com.Authentication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import cirqle.com.Adapters.SearchAndSelectAdapter
import cirqle.com.Authentication.Models.RegisterResponseModel
import cirqle.com.Authentication.Models.UserModel
import cirqle.com.Home.HomeActivity
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import cirqle.com.Utils.Utility.saveObjectLocally
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Response

class UserDetailsFormActivity : AppCompatActivity() {

    //***********************layout View***************************
    private lateinit var select_college:AutoCompleteTextView
    private lateinit var gender_radio_group:RadioGroup
    private lateinit var male_radio_btn:RadioButton
    private lateinit var female_radio_btn:RadioButton
    private lateinit var full_name_et:TextInputEditText
    private lateinit var user_name_et:TextInputEditText
    private lateinit var email_et:TextInputEditText
    private lateinit var hosetl_name_et:TextInputEditText
    private lateinit var passout_year_et:TextInputEditText
    private lateinit var degree_et:TextInputEditText
    private lateinit var department_et:TextInputEditText
    private lateinit var submit_btn:AppCompatButton
    private lateinit var progressDialog: Dialog

    //***********************variable***************************
    private lateinit var name:String
    private lateinit var userName:String
    private lateinit var phone:String
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var gender:String
    private lateinit var collegeName:String
    private lateinit var hostelName:String
    private lateinit var degree:String
    private lateinit var department:String
    private lateinit var passoutYear:String
    private lateinit var profilePic:String
    private lateinit var fcmToken:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details_form)
       init()
        FirebaseMessaging.getInstance().token.addOnCompleteListener{task->
            if(task.isSuccessful) {
                fcmToken = task.result
            }
            else{
                fcmToken="";
            }

        };


        //**********************************Radio Button****************************************************
        gender_radio_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.male_radio_btn->{
                    gender="Male"
                }
                R.id.female_radio_btn->{
                    gender="Female"
                }
        }
        })
        //**********************************Radio Button****************************************************
        submit_btn.setOnClickListener{
            progressDialog.show()
            getField()
            if(name.isNullOrBlank()){
                full_name_et.setError("Name Required")
                progressDialog.dismiss()
                return@setOnClickListener
            }
            if(userName.isNullOrBlank()){
                user_name_et.setError("userName Required")
                progressDialog.dismiss()
                return@setOnClickListener
            }
            if(email.isNullOrBlank()){
                email_et.setError("Email Required")
                progressDialog.dismiss()
                return@setOnClickListener
            }
            if(gender.isNullOrBlank()){
                Toast.makeText(this, "Gender Required", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                return@setOnClickListener
            }
            if(collegeName.isNullOrBlank()){
                select_college.setError("college name Required")
                progressDialog.dismiss()
                return@setOnClickListener
            }
            if(hostelName.isNullOrBlank()){
                hosetl_name_et.setError("hostel name Required")
                progressDialog.dismiss()
                return@setOnClickListener
            }
            if(degree.isNullOrBlank()){
                degree_et.setError("degree Required")
                progressDialog.dismiss()
                return@setOnClickListener
            }
            if(department.isNullOrBlank()){
                department_et.setError("department Required")
                progressDialog.dismiss()
                return@setOnClickListener
            }
            if(passoutYear.isNullOrBlank()){
                passout_year_et.setError("passout Required")
                progressDialog.dismiss()
                return@setOnClickListener
            }

            val registerService =BuilderRetrofit.builService(ApiInterface::class.java)
            val reqCall=registerService.register(UserModel("",name,userName,phone,password,email,gender,collegeName,hostelName,degree,department,passoutYear,"",fcmToken))
            reqCall.enqueue(object : retrofit2.Callback<RegisterResponseModel>{
                override fun onResponse(call: Call<RegisterResponseModel>, response: Response<RegisterResponseModel>) {

                    if(response.isSuccessful){
                        val userDetails=response.body()?.user;
                        Toast.makeText(this@UserDetailsFormActivity, "Register Succesfully", Toast.LENGTH_SHORT).show()
                        Utility.saveObjectLocally(this@UserDetailsFormActivity,"userDetails",userDetails!!)
                        val token=response.body()?.token
                        Utility.saveStringLocally(this@UserDetailsFormActivity,"token",token!!)
                        startActivity(Intent(this@UserDetailsFormActivity,HomeActivity::class.java))
                        progressDialog.dismiss()
//                        val users=Utility.getUserDetails(this@UserDetailsFormActivity
 //                        val userDetail=Utility.getObjectLocally(this@UserDetailsFormActivity,"userDetails",UserModel::class.java)
//                        Log.d("users", "onResponse: "+Utility.getStringLocally(this@UserDetailsFormActivity,"token"))
                        }
                }

                override fun onFailure(call: Call<RegisterResponseModel>, t: Throwable) {
                    Log.d("response", "onFailure: "+t.message)
                }
            })

        }




    }

    fun getField(){
        name=full_name_et.text.toString()
        userName=user_name_et.text.toString()
        phone=intent.getStringExtra("phone").toString()
        password=intent.getStringExtra("password").toString()
        email=email_et.text.toString()
        collegeName=select_college.text.toString()
        hostelName=hosetl_name_et.text.toString()
        degree=degree_et.text.toString()
        department=department_et.text.toString()
        passoutYear=passout_year_et.text.toString()

    }

    fun init(){
        select_college=findViewById(R.id.select_college_spinner)
        gender_radio_group=findViewById(R.id.gender_radio_group)
        full_name_et=findViewById(R.id.full_name_et)
        user_name_et=findViewById(R.id.user_name_et)
        email_et=findViewById(R.id.email_et)
        hosetl_name_et=findViewById(R.id.hosetl_name_et)
        passout_year_et=findViewById(R.id.passout_year_et)
        degree_et=findViewById(R.id.degree_et)
        department_et=findViewById(R.id.department_et)
        submit_btn=findViewById(R.id.submit_btn)

        progressDialog = Dialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setContentView(R.layout.layout_loading_progress_dialog)
        val progressBar = progressDialog.findViewById<LottieAnimationView>(R.id.progress_bar)


        //**********************************Select College search ****************************************************
        val datalist: ArrayList<String> = ArrayList()
        datalist.add("Indian Institute of Technology, Patna")

        val adapter=SearchAndSelectAdapter(this,android.R.layout.simple_list_item_2,datalist)
        select_college.setAdapter(adapter)
        //**********************************Select College search****************************************************
    }
}