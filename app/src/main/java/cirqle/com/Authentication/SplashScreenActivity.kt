package cirqle.com.Authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import cirqle.com.Home.HomeActivity
import cirqle.com.MainActivity
import cirqle.com.R
import cirqle.com.Utils.Utility
import com.google.firebase.auth.FirebaseAuth
import java.util.Timer
import kotlin.concurrent.timerTask

class SplashScreenActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


//        val iLogin = Intent(this, LoginActivity::class.java)
 startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
        finish()
//        Timer().schedule(timerTask {
//            val token=Utility.getStringLocally(this@SplashScreenActivity,"token")
//            if(token.isNullOrBlank()){
//                startActivity(Intent(this@SplashScreenActivity,LoginActivity::class.java))
//            }
//            else {
//                startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
//            }
//            finish()
//        },3000)
    }
}