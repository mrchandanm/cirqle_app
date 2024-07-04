package cirqle.com.LostAndFound

import android.app.Dialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.viewpager2.widget.ViewPager2
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import cirqle.com.BuyAndSell.Adapters.ImageShowViewPagerAdapter
import cirqle.com.BuyAndSell.Workers.PostImageUploadWorker
import cirqle.com.LostAndFound.Workers.PostUploadWorker
import cirqle.com.R
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.google.android.material.textfield.TextInputEditText

class AddLostAndFoundPost : AppCompatActivity() {
    private lateinit var date:TextInputEditText
    private lateinit var title_et:TextInputEditText
    private lateinit var description_et:TextInputEditText
    private lateinit var location_et:TextInputEditText
    private lateinit var submit_btn:AppCompatButton
    private lateinit var category_radio_group:RadioGroup
    private  var category:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lost_and_found_post)
        init()

        category_radio_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{ radiogroup, i->
            when(i){
                R.id.found_radio_btn->{category="Found"}
                R.id.lost_radio_btn->{category="Lost"}
            }
        })

        val imageUriStrings = intent.getStringArrayExtra("imageUris")
        val imageUris = imageUriStrings?.map { Uri.parse(it) } ?: emptyList()
        val viewPager: ViewPager2 = findViewById(R.id.imageshow_viewpager)
        val adapter = ImageShowViewPagerAdapter(this, imageUris)
        viewPager.adapter = adapter

        submit_btn.setOnClickListener{
            if(category.isEmpty()) {
                Toast.makeText(this@AddLostAndFoundPost, "Category Required", Toast.LENGTH_SHORT).show()
            }
            else if(title_et.text.toString().isEmpty()) {
                title_et.setError("Title Required")
                Toast.makeText(this@AddLostAndFoundPost, "title Required", Toast.LENGTH_SHORT).show()
            }
            else if(description_et.text.toString().isEmpty()) {
                description_et.setError("Description Required")
                Toast.makeText(this@AddLostAndFoundPost, "description Required", Toast.LENGTH_SHORT).show()
            }
            else if(location_et.text.toString().isEmpty()) {
                location_et.setError("Location Required")
                Toast.makeText(this@AddLostAndFoundPost, "location Required", Toast.LENGTH_SHORT).show()
            }
            else if(date.text.toString().isEmpty()) {
                date.setError("date Required")
                Toast.makeText(this@AddLostAndFoundPost, "Date Required", Toast.LENGTH_SHORT).show()
            }
            else {
                uploadImagesToFirebase(imageUris)
                Toast.makeText(this, "uploading...", Toast.LENGTH_SHORT).show()
                finish()
            }
        }


        date.setOnClickListener{
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.layout_calendar_dialog)
            val calendarView: CalendarView = dialog.findViewById(R.id.calendarView)
            // Set initial display to current year
            calendarView.maxDate = System.currentTimeMillis()

            calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                // Use the selectedDate as needed
                date.setText(selectedDate)
                Toast.makeText(this, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            dialog.show()

        }


    }

    private fun uploadImagesToFirebase(imageUris: List<Uri>) {
        val workManager = WorkManager.getInstance(this)
        val inputData = workDataOf(
            "imageUris" to imageUris.map { it.toString() }.toTypedArray(),
            "title" to title_et.text.toString(),
            "description" to description_et.text.toString(),
            "category" to category,
            "date" to date.text.toString(),
            "location" to location_et.text.toString()
        )

        val uploadWorkRequest = OneTimeWorkRequestBuilder<PostUploadWorker>()
            .setInputData(inputData)
            .build()
        workManager.enqueue(uploadWorkRequest)
    }
    fun init(){
        date=findViewById(R.id.date)
        title_et=findViewById(R.id.title_et)
        description_et=findViewById(R.id.description_et)
        location_et=findViewById(R.id.location_et)
        submit_btn=findViewById(R.id.submit_btn)
        category_radio_group=findViewById(R.id.category_radio_group)
    }
}