package cirqle.com.TravelBuddy

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import cirqle.com.R
import cirqle.com.TravelBuddy.Models.AddTripModel
import cirqle.com.TravelBuddy.Models.AddTripResponseModel
import cirqle.com.Utils.ApiInterface
import cirqle.com.Utils.BuilderRetrofit
import cirqle.com.Utils.Utility
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTripActivity : AppCompatActivity() {
    private lateinit var destination_et:TextInputEditText
    private lateinit var meeting_point:TextInputEditText
    private lateinit var description_et:TextInputEditText
    private lateinit var departure_date:TextInputEditText
    private lateinit var return_date:TextInputEditText
    private lateinit var cost_sharing:TextInputEditText
    private lateinit var seats_available:TextInputEditText
    private lateinit var submit_btn:AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)
        init()
        val user=Utility.getUserDetails(this)?._id
        val collegeName=Utility.getUserDetails(this)?.collegeName

        submit_btn.setOnClickListener {
            if(destination_et.text.isNullOrBlank()){
                description_et.setError("Destination is required")
                return@setOnClickListener
            }
            if(meeting_point.text.isNullOrBlank()){
                meeting_point.setError("Meeting Point is required")
                return@setOnClickListener
            }
            if(description_et.text.isNullOrBlank()){
                description_et.setError("Description is required")
                return@setOnClickListener
            }
            if(departure_date.text.isNullOrBlank()){
                departure_date.setError("departure date required")
                return@setOnClickListener
            }
            val reqCall = BuilderRetrofit.builService(ApiInterface::class.java).add_trip(
                AddTripModel(
                    destination_et.text.toString(),
                    meeting_point.text.toString(),
                    user!!,
                    description_et.text.toString(),
                    collegeName!!,
                    departure_date.text.toString(),
                    return_date.text.toString(),
                    cost_sharing.text.toString(),
                    seats_available.text.toString()
                )
            )
            reqCall.enqueue(object : Callback<AddTripResponseModel> {
                override fun onResponse(
                    call: Call<AddTripResponseModel>,
                    response: Response<AddTripResponseModel>
                ) {
                    Toast.makeText(this@AddTripActivity, "Uploaded", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AddTripActivity, TravelBuddyHomeActivity::class.java))
                    finish()
                    Log.d("travel", "onResponse: " + response.body())
                }

                override fun onFailure(call: Call<AddTripResponseModel>, t: Throwable) {
                    Toast.makeText(this@AddTripActivity, "failed", Toast.LENGTH_SHORT).show()
                    Log.d("travel", "onFailure: " + t.message)
                }
            })
        }




        departure_date.setOnClickListener{
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.layout_calendar_dialog)
            val calendarView: CalendarView = dialog.findViewById(R.id.calendarView)
            // Set initial display to current year
            calendarView.minDate = System.currentTimeMillis()

            calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                // Use the selectedDate as needed
                departure_date.setText(selectedDate)
                Toast.makeText(this, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            dialog.show()
        }
        return_date.setOnClickListener{
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.layout_calendar_dialog)
            val calendarView: CalendarView = dialog.findViewById(R.id.calendarView)
            // Set initial display to current year
            calendarView.minDate = System.currentTimeMillis()

            calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                // Use the selectedDate as needed
                return_date.setText(selectedDate)
                Toast.makeText(this, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            dialog.show()

        }

    }
    fun init(){
        destination_et=findViewById(R.id.destination_et)
        meeting_point=findViewById(R.id.meeting_point)
        departure_date=findViewById(R.id.departure_date)
        description_et=findViewById(R.id.description_et)
        return_date=findViewById(R.id.return_date)
        cost_sharing=findViewById(R.id.cost_sharing)
        seats_available=findViewById(R.id.seats_available)
        submit_btn=findViewById(R.id.submit_btn)
    }
}