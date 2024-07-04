package cirqle.com.DateSpark

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import cirqle.com.BuyAndSell.Adapters.ImageShowViewPagerAdapter
import cirqle.com.BuyAndSell.AddBuyAndSellActivity
import cirqle.com.BuyAndSell.Models.GetPostResponseModel
import cirqle.com.BuyAndSell.Workers.PostImageUploadWorker
import cirqle.com.DateSpark.Adapters.ViewpagerCenterCropImageAdapter
import cirqle.com.DateSpark.Models.CheckUserModel
import cirqle.com.DateSpark.Models.DateSparkResponseModel
import cirqle.com.DateSpark.Models.DateSparkUserModel
import cirqle.com.DateSpark.Workers.UploadUserWorker
import cirqle.com.R
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationForDateSparkActivity : AppCompatActivity() {
    private lateinit var submit_btn: AppCompatButton
    private lateinit var next_btn: AppCompatButton
    private lateinit var relationship_type_radio_group: RadioGroup
    private lateinit var relationship_type_layout: CardView
    private lateinit var images_layout: CardView
    private lateinit var viewpager_image: ViewPager2
    private lateinit var add_photos: TextView
    private lateinit var add_photo_layout: LinearLayout
    private lateinit var pg:ProgressBar
    var selectedImageUris = mutableListOf<Uri>()

    private var relationshipType: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_for_date_spark)
        submit_btn = findViewById(R.id.submit_btn)
        next_btn = findViewById(R.id.next_btn)
        relationship_type_layout = findViewById(R.id.relationship_type_layout)
        images_layout = findViewById(R.id.images_layout)
        relationship_type_radio_group = findViewById(R.id.relationship_type_radio_group)
        add_photos = findViewById(R.id.add_photo)
        add_photo_layout = findViewById(R.id.add_photo_layout)
        viewpager_image = findViewById(R.id.viewpage_image)
        pg=findViewById(R.id.pg)
        val user= Utility.getUserDetails(applicationContext)?._id!!
        val getAdsService = BuilderRetrofit.builService(ApiInterface::class.java).check_user(user).enqueue(object:Callback<CheckUserModel>{
            override fun onResponse(call: Call<CheckUserModel>, response: Response<CheckUserModel>) {
              if(response.isSuccessful){
                  val check=response.body()?.isuser!!
                  if(check){
                      startActivity(Intent(this@RegistrationForDateSparkActivity,DateSparkHomeActivity::class.java))
                      finish()
                  }
                  else{
                      pg.visibility=View.GONE
                      relationship_type_layout.visibility=View.VISIBLE
                  }
              }

            }

            override fun onFailure(call: Call<CheckUserModel>, t: Throwable) {
                Toast.makeText(this@RegistrationForDateSparkActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                Log.d("check", "onFailure: "+t.message)
                pg.visibility=View.GONE
            }
        })

        relationship_type_radio_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.relationship -> {
                    relationshipType = "Serious, looking for something long-term"
                }
                R.id.casual -> {
                    relationshipType = "Casual, just exploring options"
                }
                R.id.not_sure -> {
                    relationshipType = "Not sure yet, open to see where things go"
                }
            }
        })
        next_btn.setOnClickListener {
            if (relationshipType == "") {
                Toast.makeText(this, "please Select Relationship type", Toast.LENGTH_SHORT).show()
            } else {
                relationship_type_layout.visibility = View.GONE
                images_layout.visibility = View.VISIBLE

            }

        }

        add_photos.setOnClickListener {
            openGallery()
        }
        submit_btn.setOnClickListener {
            uploadImagesToFirebase(selectedImageUris)
            startActivity(Intent(this@RegistrationForDateSparkActivity,DateSparkHomeActivity::class.java))
            finish()
        }


    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//            val selectedImageUris = mutableListOf<Uri>()
                val data = result.data
                data?.let { intent ->
                    if (intent.clipData != null) {
                        val count = intent.clipData!!.itemCount
                        for (i in 0 until count) {
                            val uri = intent.clipData!!.getItemAt(i).uri
                            selectedImageUris.add(uri)
                        }
                    } else if (intent.data != null) {
                        val uri = intent.data!!
                        selectedImageUris.add(uri)
                    }
                }

                if (selectedImageUris.isNotEmpty()) {
                    add_photo_layout.visibility = View.GONE
                    viewpager_image.visibility = View.VISIBLE
                    val adapter = ViewpagerCenterCropImageAdapter(this, selectedImageUris)
                    viewpager_image.adapter = adapter
                }
            }
        }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryLauncher.launch(intent)
    }


    private fun uploadImagesToFirebase(imageUris: List<Uri>) {
        val workManager = WorkManager.getInstance(this)
        val inputData = workDataOf(
            "imageUris" to imageUris.map { it.toString() }.toTypedArray(),
            "relationshipType" to relationshipType,
        )

        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadUserWorker>()
            .setInputData(inputData)
            .build()
        workManager.enqueue(uploadWorkRequest)
    }
}