package cirqle.com.SkillSwap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import cirqle.com.R
import com.facebook.shimmer.ShimmerFrameLayout

class SkillsSwapHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skills_swap_home)

        val shimmerLayout: ShimmerFrameLayout = findViewById(R.id.shimmerLayout)
        shimmerLayout.startShimmer()

        // Simulate loading delay
        Handler(Looper.getMainLooper()).postDelayed({
            // Stop shimmer animation and show actual content
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            // Show your actual content
            // e.g., recyclerView.visibility = View.VISIBLE
        }, 3000) // Delay in milliseconds
    }
}