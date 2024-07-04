package cirqle.com

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import cirqle.com.Adapters.ViewPagerPiccasoFullViewAdapter
import cirqle.com.BuyAndSell.Adapters.ViewPagerPicassoImageAdapter

class FullImageViewctivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image_viewctivity)
        val viewpager:ViewPager2=findViewById(R.id.viewpager_full_view)
        val images=intent.getStringArrayListExtra("images")
        val adapter = ViewPagerPiccasoFullViewAdapter(this, images!!)
        viewpager.adapter=adapter
        Toast.makeText(this, images.size.toString(), Toast.LENGTH_SHORT).show()
    }
}