package cirqle.com.Authentication

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import cirqle.com.Authentication.Models.SendOtpModel
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.common.api.Api
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Objects
import java.util.concurrent.TimeUnit

class PhoneVerifiationActivity : AppCompatActivity() {
    //**********************Layout View*******************
    private lateinit var login_page: TextView
    private lateinit var phone_et: TextInputEditText
    private lateinit var otp_et: TextInputEditText
    private lateinit var get_otp_btn: TextView
    private lateinit var password_et: TextInputEditText
    private lateinit var cnf_password_et: TextInputEditText
    private lateinit var submit_btn: AppCompatButton
    private lateinit var progress_bar: ProgressBar
    private lateinit var main_layout: LinearLayout

    //**********************variable*******************
    private lateinit var phone:String
    private lateinit var cnf_password:String
    private lateinit var password:String
    private lateinit var otp:String

    private lateinit var progressDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verifiation)
init()
        main_layout=findViewById(R.id.main_layout)
        progressDialog = Dialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setContentView(R.layout.layout_loading_progress_dialog)
        val progressBar = progressDialog.findViewById<LottieAnimationView>(R.id.progress_bar)
        submit_btn.setOnClickListener(View.OnClickListener {
            progressDialog.show()
            if(password_et.text.toString()==cnf_password_et.text.toString()) {
                val intent = Intent(this@PhoneVerifiationActivity, UserDetailsFormActivity::class.java)
                intent.putExtra("phone", phone_et.text.toString())
                intent.putExtra("password", password_et.text.toString())
                startActivity(intent)
                finish()
                progressDialog.dismiss()
            }
            else{
                progressDialog.dismiss()
                cnf_password_et.setError("passward mismatch")
            }
        })

        get_otp_btn.setOnClickListener(View.OnClickListener {
            if (get_otp_btn.text=="Get OTP") {
                progressDialog.show()
//                progress_bar.visibility=View.VISIBLE
                getOtp()
            }
            else{

                if (  otp_et.getText().toString().length == 6) {
                    progressDialog.show()
//                    progress_bar.visibility=View.VISIBLE
                    verifyOtp(otp_et.getText().toString())
                }
            }
        })
        login_page.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }


    }
    private fun getOtp() {
        phone = phone_et.text?.trim().toString()
        if (!phone.isEmpty()) {
            if (phone.length == 10) {
//                phone = "+91$phone"
                val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).sendOtp(phone)
                reqCall.enqueue(object :Callback<SendOtpModel>{
                    override fun onResponse(
                        call: Call<SendOtpModel>,
                        response: Response<SendOtpModel>
                    ) {
                        progressDialog.dismiss()
                        get_otp_btn.setText("Verify Otp")
                        otp_et.setEnabled(true);
                        Toast.makeText(this@PhoneVerifiationActivity, "Otp Send Successfully", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<SendOtpModel>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(this@PhoneVerifiationActivity, "something went wrong", Toast.LENGTH_SHORT).show()
                    }

                })
            } else {
                progressDialog.dismiss()
                phone_et.setError("Enter correct number")
            }
        } else {
            progressDialog.dismiss()
            phone_et.setError("Enter Number")
        }
    }
    private fun verifyOtp(otpNumber: String) {
        if(otpNumber!=null) {
            val reqCall=BuilderRetrofit.builService(ApiInterface::class.java).verifyOtp(otpNumber)
            reqCall.enqueue(object :Callback<SendOtpModel>{
                override fun onResponse(
                    call: Call<SendOtpModel>,
                    response: Response<SendOtpModel>
                ) {
                    if(response.body()?.message=="verified"){
                        submit_btn.setEnabled(true)
                        get_otp_btn.setText("Verified")
                        get_otp_btn.setTextColor(getColor(R.color.succes_green))
                        otp_et.setEnabled(false)
                        progressDialog.dismiss()
                    }
                    else{
                        progressDialog.dismiss()
                        get_otp_btn.setError("Wrong OTP")
                    }
                }

                override fun onFailure(call: Call<SendOtpModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(this@PhoneVerifiationActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
    fun init(){
        login_page=findViewById(R.id.login_page)
        phone_et=findViewById(R.id.phone_et)
        otp_et=findViewById(R.id.otp_et)
        get_otp_btn=findViewById(R.id.get_otp_btn)
        password_et=findViewById(R.id.password_et)
        cnf_password_et=findViewById(R.id.cnf_password_et)
        submit_btn=findViewById(R.id.submit_btn)
    }
}