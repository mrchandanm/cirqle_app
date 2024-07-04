package cirqle.com.SeniorCirqle

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.viewpager2.widget.ViewPager2
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import cirqle.com.BuyAndSell.Adapters.ImageShowViewPagerAdapter
import cirqle.com.DateSpark.Adapters.ViewpagerCenterCropImageAdapter
import cirqle.com.DateSpark.Workers.UploadUserWorker
import cirqle.com.R
import cirqle.com.SeniorCirqle.Models.OptionModel
import cirqle.com.SeniorCirqle.Workers.AddDoubtWorker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import java.io.Serializable

class AddDoubtsActivity : AppCompatActivity() {
    private lateinit var add_photot_btn:AppCompatButton
    private lateinit var add_poll_btn:AppCompatButton
    private lateinit var add_option_btn:AppCompatButton
    private lateinit var option_3_layout:TextInputLayout
    private lateinit var option_4_layout:TextInputLayout
    private lateinit var option_5_layout:TextInputLayout
    private lateinit var option_6_layout:TextInputLayout
    private lateinit var option_1:TextInputEditText
    private lateinit var option_2:TextInputEditText
    private lateinit var option_3:TextInputEditText
    private lateinit var option_4:TextInputEditText
    private lateinit var option_5:TextInputEditText
    private lateinit var option_6:TextInputEditText
    private lateinit var delete_option_btn:ImageView
    private lateinit var poll_layout:LinearLayout
    private lateinit var viewpager:ViewPager2
    private lateinit var submit_btn:AppCompatButton
    private lateinit var question_et:AppCompatEditText
    private var dataReloadListener: DoubtCirqleInterface? = null

    var selectedImageUris = mutableListOf<Uri>()
    private lateinit var question:String
    private var isAnonymos:String = "No"
    var options= mutableListOf<OptionModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_doubts)
        init()
        dataReloadListener = intent.getSerializableExtra("dataReloadListener") as? DoubtCirqleInterface
        submit_btn.setOnClickListener{
            if (question_et.text.isNullOrBlank() && options.isEmpty() && selectedImageUris.isEmpty()){
                Toast.makeText(this, "Nothing to upload", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
            question=question_et.text.toString()
           addOptions()
            upload(selectedImageUris)
            finish()
        }

        add_photot_btn.setOnClickListener{
            openGallery()
        }
        add_poll_btn.setOnClickListener{
            if(poll_layout.visibility==View.GONE){
                poll_layout.visibility=View.VISIBLE
                add_poll_btn.text="Remove Poll"
            }
            else{
                poll_layout.visibility=View.GONE
                add_poll_btn.text="Add Poll"
            }
        }

        add_option_btn.setOnClickListener{
            if(option_3_layout.visibility== View.GONE){
                delete_option_btn.visibility=View.VISIBLE
                option_3_layout.visibility=View.VISIBLE
            }
            else if(option_4_layout.visibility== View.GONE){
                option_4_layout.visibility=View.VISIBLE
            }
            else if(option_5_layout.visibility== View.GONE){
                option_5_layout.visibility=View.VISIBLE
            }
            else{
                option_6_layout.visibility=View.VISIBLE
            }

        }

        delete_option_btn.setOnClickListener{
            if( option_6_layout.visibility==View.VISIBLE){
                option_6_layout.visibility=View.GONE
                option_6.text?.clear()

            }
            else if( option_5_layout.visibility==View.VISIBLE){
                option_5_layout.visibility=View.GONE
                option_5.text?.clear()
            }
            else if( option_4_layout.visibility==View.VISIBLE){
                option_4_layout.visibility=View.GONE
                option_4.text?.clear()
            }
            else if( option_3_layout.visibility==View.VISIBLE){
                option_3_layout.visibility=View.GONE
                option_3.text?.clear()
                delete_option_btn.visibility=View.GONE
            }

        }


    }

    private fun upload(imageUris: List<Uri>) {
        val option = Gson().toJson(options)
        val workManager = WorkManager.getInstance(this)
        val inputData = workDataOf(
            "imageUris" to imageUris.map { it.toString() }.toTypedArray(),
            "question" to question,
            "options" to option,
            "category" to "doubtPost",
            "isAnonymos" to isAnonymos
        )

        val addDoubtWorker = OneTimeWorkRequestBuilder<AddDoubtWorker>()
            .setInputData(inputData)
            .build()
        workManager.enqueue(addDoubtWorker)
    }

    private fun addOptions() {
        if(!option_1.text.toString().trim().isEmpty()){
            options.add(OptionModel(option_1.text.toString()))
        }
        if(!option_2.text.toString().trim().isEmpty()){
            options.add(OptionModel(option_2.text.toString()))
        }
        if(!option_3.text.toString().trim().isEmpty()){
            options.add(OptionModel(option_3.text.toString()))
        }
        if(!option_4.text.toString().trim().isEmpty()){
            options.add(OptionModel(option_4.text.toString()))
        }
        if(!option_5.text.toString().trim().isEmpty()){
            options.add(OptionModel(option_5.text.toString()))
        }
        if(!option_6.text.toString().trim().isEmpty()){
            options.add(OptionModel(option_6.text.toString()))
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryLauncher.launch(intent)
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
                    viewpager.visibility = View.VISIBLE
                    val adapter = ImageShowViewPagerAdapter(this, selectedImageUris)
                    viewpager.adapter = adapter
                }
            }
        }

    fun init(){
        add_option_btn=findViewById(R.id.add_option_btn)
        add_photot_btn=findViewById(R.id.add_photo_btn)
        add_poll_btn=findViewById(R.id.add_poll_btn)
        option_3_layout=findViewById(R.id.option_3_layout)
        option_4_layout=findViewById(R.id.option_4_layout)
        option_5_layout=findViewById(R.id.option_5_layout)
        option_6_layout=findViewById(R.id.option_6_layout)
        delete_option_btn=findViewById(R.id.delete_option_btn)
        option_1=findViewById(R.id.option_1)
        option_2=findViewById(R.id.option_2)
        option_3=findViewById(R.id.option_3)
        option_4=findViewById(R.id.option_4)
        option_5=findViewById(R.id.option_5)
        option_6=findViewById(R.id.option_6)
        poll_layout=findViewById(R.id.poll_layout)
        viewpager=findViewById(R.id.viewpager)
        submit_btn=findViewById(R.id.submit_btn)
        question_et=findViewById(R.id.question_et)
    }
}