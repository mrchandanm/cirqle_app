package cirqle.com.Authentication

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import cirqle.com.Home.HomeActivity
import cirqle.com.R
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {

    //**********************Layout View*******************
    private lateinit var login_page: TextView
    private lateinit var phone_et: TextInputEditText
    private lateinit var otp_et: TextInputEditText
    private lateinit var get_otp_btn: TextView
    private lateinit var password_et: TextInputEditText
    private lateinit var cnf_password_et: TextInputEditText
    private lateinit var submit_btn: AppCompatButton
    private lateinit var progress_bar:ProgressBar
    private lateinit var main_layout:LinearLayout

    //**********************variable*******************
    private lateinit var phone:String
    private lateinit var cnf_password:String
    private lateinit var password:String
    private lateinit var otp:String

    //**********************Firebase*******************
    private lateinit var auth: FirebaseAuth;
    private lateinit var verificationotpId : String
    private lateinit var progressDialog:Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
           init()
//        progress_bar=findViewById(R.id.progress_bar)
        main_layout=findViewById(R.id.main_layout)

        progressDialog = Dialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setContentView(R.layout.layout_loading_progress_dialog)
        val progressBar = progressDialog.findViewById<LottieAnimationView>(R.id.progress_bar)

        submit_btn.setOnClickListener(View.OnClickListener {
            progressDialog.show()
            if(password_et.text.toString()==cnf_password_et.text.toString()) {
                val intent = Intent(this@RegisterActivity, UserDetailsFormActivity::class.java)
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

    private fun verifyOtp(otpNumber: String) {
        if(otpNumber!=null) {
            val credential = PhoneAuthProvider.getCredential(verificationotpId!!, otpNumber)
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun getOtp() {
        phone = phone_et.text?.trim().toString()
        if (!phone.isEmpty()) {
            if (phone.length == 10) {
                phone = "+91$phone"

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phone) // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this) // Activity (for callback binding)
                    .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            } else {
                progressDialog.dismiss()
                phone_et.setError("Enter correct number")
            }
        } else {
            progressDialog.dismiss()
            phone_et.setError("Enter Number")
        }
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            progressDialog.dismiss()
            if (e is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(this@RegisterActivity, "some error :${e.message}", Toast.LENGTH_SHORT).show()
            } else if (e is FirebaseTooManyRequestsException) {
                Toast.makeText(this@RegisterActivity, "some error :${e.message}", Toast.LENGTH_SHORT).show()

            }
//            else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
//                // reCAPTCHA verification attempted with null Activity
//            }

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            progressDialog.dismiss()
            get_otp_btn.setText("Verify Otp")
            otp_et.setEnabled(true);
            verificationotpId=verificationId;
//            progress_bar.visibility=View.GONE

            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
//           val intent= Intent(this@RegisterActivity,MainActivity::class.java)
//            intent.putExtra("otp",verificationId)
//            intent.putExtra("resendToken", token)
//            intent.putExtra("phoneNumber",number)
//            startActivity(intent)

            // Save verification ID and resending token so we can use them later
//            storedVerificationId = verificationId
//            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    submit_btn.setEnabled(true)
                    get_otp_btn.setText("Verified")
                    get_otp_btn.setTextColor(getColor(R.color.succes_green))
                    otp_et.setEnabled(false)
                    progressDialog.dismiss()
//                    progress_bar.visibility=View.GONE
                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        progressDialog.dismiss()
                        otp_et.setError("wrong OTP")
                    }
                    progressDialog.dismiss()
                    // Update UI
                }
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
        auth=FirebaseAuth.getInstance()
    }
}