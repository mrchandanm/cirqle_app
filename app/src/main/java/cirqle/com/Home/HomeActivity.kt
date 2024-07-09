package cirqle.com.Home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cirqle.com.Account.AccountFragment
import cirqle.com.Chat.ChatHomeFragment
import cirqle.com.CirqleStore.CirqleStoreActivity
import cirqle.com.CirqleStore.CirqleStoreFragment
import cirqle.com.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

class HomeActivity : AppCompatActivity() {
    private lateinit var bottom_nav:BottomNavigationView
    private lateinit var home_container:FrameLayout
    private lateinit var home_layout:LinearLayout
    private lateinit var chat_layout:LinearLayout
    private lateinit var account_layout:LinearLayout
    private lateinit var store_layout:LinearLayout
    private lateinit var store_icon:TextView
    private lateinit var store_text:TextView
    private lateinit var home_icon:TextView
    private lateinit var home_tv:TextView
    private lateinit var chat_icon:TextView
    private lateinit var chat_tv:TextView
    private lateinit var profile_icon:TextView
    private lateinit var profile_tv:TextView
    private val NOTIFICATION_PERMISSION_REQUEST_CODE=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
        loadfrag(HomeFragment(),0)
        home_icon.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_secondary)
        home_tv.setTextColor(ContextCompat.getColor(this, R.color.primary_secondary))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE)
            }
        }

        home_layout.setOnClickListener {
            loadfrag(HomeFragment(),1)
            home_icon.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_secondary)
            home_tv.setTextColor(ContextCompat.getColor(this, R.color.primary_secondary))
            chat_icon.backgroundTintList = ContextCompat.getColorStateList(this, R.color.txt_gray)
            chat_tv.setTextColor(ContextCompat.getColor(this, R.color.txt_gray))
            profile_icon.backgroundTintList = ContextCompat.getColorStateList(this, R.color.txt_gray)
            profile_tv.setTextColor(ContextCompat.getColor(this, R.color.txt_gray))
        }
        chat_layout.setOnClickListener {
            loadfrag(ChatHomeFragment(),1)
            home_icon.backgroundTintList = ContextCompat.getColorStateList(this, R.color.txt_gray)
            home_tv.setTextColor(ContextCompat.getColor(this, R.color.txt_gray))
            profile_icon.backgroundTintList = ContextCompat.getColorStateList(this, R.color.txt_gray)
            profile_tv.setTextColor(ContextCompat.getColor(this, R.color.txt_gray))
            chat_icon.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_secondary)
            chat_tv.setTextColor(ContextCompat.getColor(this, R.color.primary_secondary))
        }
        account_layout.setOnClickListener {
            loadfrag(AccountFragment(),1)
            home_icon.backgroundTintList = ContextCompat.getColorStateList(this, R.color.txt_gray)
            home_tv.setTextColor(ContextCompat.getColor(this, R.color.txt_gray))
            chat_icon.backgroundTintList = ContextCompat.getColorStateList(this, R.color.txt_gray)
            chat_tv.setTextColor(ContextCompat.getColor(this, R.color.txt_gray))
            profile_icon.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_secondary)
            profile_tv.setTextColor(ContextCompat.getColor(this, R.color.primary_secondary))
        }
        store_layout.setOnClickListener {
            startActivity(Intent(this@HomeActivity,CirqleStoreActivity::class.java))
//            loadfrag(CirqleStoreFragment(),1)
//            store_icon.setTextColor(getColor(R.color.primary_secondary))
//            store_text.setTextColor(getColor(R.color.primary_secondary))
        }

        bottom_nav.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.navHome->{
                    loadfrag(HomeFragment(),1)

                    true
                }
                R.id.nav_chat->{
                    loadfrag(ChatHomeFragment(),1)
                    true
                }
                R.id.nav_accounnt->{
                    loadfrag(AccountFragment(),1)
                    true
                }
                R.id.nav_cirqle_Store->{
                    loadfrag(CirqleStoreFragment(),1)
                    true
                }
                else->false
            }
        }





    }




    fun loadfrag(fragment: Fragment?, flag: Int) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        if (flag == 0) {
            ft.add(R.id.home_container, fragment!!)
        } else {
            ft.replace(R.id.home_container, fragment!!)
        }
        ft.commit()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun init(){
        home_container=findViewById(R.id.home_container)
        bottom_nav=findViewById(R.id.bottom_nav)
        home_layout=findViewById(R.id.home_layout)
        chat_layout=findViewById(R.id.chat_layout)
        account_layout=findViewById(R.id.account_layout)
        store_layout=findViewById(R.id.store_layout)
        store_icon=findViewById(R.id.store_icon)
        store_text=findViewById(R.id.store_text)
        home_icon=findViewById(R.id.home_icon)
        home_tv=findViewById(R.id.home_tv)
        chat_icon=findViewById(R.id.chat_icon)
        chat_tv=findViewById(R.id.chat_tv)
        profile_icon=findViewById(R.id.profile_icon)
        profile_tv=findViewById(R.id.profile_tv)


    }
}