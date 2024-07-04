package cirqle.com.BuyAndSell

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.viewpager2.widget.ViewPager2
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import cirqle.com.Adapters.SearchAndSelectAdapter
import cirqle.com.BuyAndSell.Adapters.ImageShowViewPagerAdapter
import cirqle.com.BuyAndSell.Workers.PostImageUploadWorker
import cirqle.com.R
import cirqle.com.Utils.Utility
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text


class AddBuyAndSellActivity : AppCompatActivity() {
    private lateinit var submit_btn:AppCompatButton
    private lateinit var title_et:TextInputEditText
    private lateinit var description_et:TextInputEditText
    private lateinit var category_select:AutoCompleteTextView
    private lateinit var negotiable_radio_group:RadioGroup
    private lateinit var price_et:TextInputEditText


     private lateinit var price:String
    private lateinit var title:String
    private lateinit var description:String
    private lateinit var category:String
    private lateinit var negotiable:String
    private lateinit var owner:String
    private lateinit var collegeName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_buy_and_sell)

        init()


        negotiable_radio_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{radiogroup,i->
           when(i){
               R.id.yes_radio_btn->{negotiable="Yes"}
               R.id.no_radio_btn->{negotiable="No"}
           }
        })


        val imageUriStrings = intent.getStringArrayExtra("imageUris")
        val imageUris = imageUriStrings?.map { Uri.parse(it) } ?: emptyList()

        val viewPager: ViewPager2 = findViewById(R.id.imageshow_viewpager)
        val adapter = ImageShowViewPagerAdapter(this, imageUris)
        viewPager.adapter = adapter

        submit_btn.setOnClickListener{
            if(title_et.text.isNullOrBlank()){
                title_et.setError("Title Required")
                return@setOnClickListener
            }
            if(description_et.text.isNullOrBlank()){
                description_et.setError("Title Required")
                return@setOnClickListener
            }
            if(price_et.text.isNullOrBlank()){
                price_et.setError("Title Required")
                return@setOnClickListener
            }
            finish()
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
            title=title_et.text.toString()
            description=description_et.text.toString()
            category=category_select.text.toString()
            price=price_et.text.toString()
            uploadImagesToFirebase(imageUris)
        }



        }

    private fun uploadImagesToFirebase(imageUris: List<Uri>) {
        val workManager = WorkManager.getInstance(this)
        val inputData = workDataOf(
            "imageUris" to imageUris.map { it.toString() }.toTypedArray(),
            "price" to price,
            "title" to title,
            "description" to description,
            "category" to category,
            "negotiable" to negotiable,
            "owner" to owner,
            "collegeName" to collegeName,
            )

        val uploadWorkRequest = OneTimeWorkRequestBuilder<PostImageUploadWorker>()
            .setInputData(inputData)
            .build()
        workManager.enqueue(uploadWorkRequest)
    }


    fun init(){
        title_et=findViewById(R.id.title_et)
        description_et=findViewById(R.id.description_et)
        category_select=findViewById(R.id.category_select)
        submit_btn=findViewById(R.id.submit_btn)
        negotiable_radio_group=findViewById(R.id.negotiable_radio_group)
        price_et=findViewById(R.id.price_et)

        owner=Utility.getUserDetails(this@AddBuyAndSellActivity)?._id!!
        collegeName=Utility.getUserDetails(this@AddBuyAndSellActivity)?.collegeName!!

        //**********************************Select College search ****************************************************
        val datalist: ArrayList<String> = ArrayList()
        datalist.add("Cycle")
        datalist.add("Stationary")

        val adapter= SearchAndSelectAdapter(this,android.R.layout.simple_list_item_2,datalist)
        category_select.setAdapter(adapter)
        //**********************************Select College search****************************************************
    }


    }