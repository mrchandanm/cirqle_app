package cirqle.com.CirqleStore

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import cirqle.com.R

import com.airbnb.lottie.LottieAnimationView


class PlaceOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(cirqle.com.R.layout.activity_place_order)
        val animationView = findViewById<LottieAnimationView>(cirqle.com.R.id.lottieAnimationView)
        val goBack=findViewById<AppCompatButton>(R.id.go_back)
        animationView.setAnimation("done_anim.json")
        animationView.playAnimation()
        goBack.setOnClickListener{
            val intent = Intent(this@PlaceOrderActivity, CirqleStoreActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }
    override fun onBackPressed() {
        // Restart the activity
        val intent = Intent(this@PlaceOrderActivity, CirqleStoreActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}




