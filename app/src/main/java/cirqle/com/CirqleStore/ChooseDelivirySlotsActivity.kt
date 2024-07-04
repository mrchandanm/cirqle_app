package cirqle.com.CirqleStore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.CirqleStore.Adapters.DateSlotAdapter
import cirqle.com.R
import java.text.SimpleDateFormat
import java.util.*

class ChooseDelivirySlotsActivity : AppCompatActivity() {
    private lateinit var date_rv:RecyclerView
    private lateinit var dateAdapter:DateSlotAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_deliviry_slots)
        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val dates = mutableListOf<String>()

        val calendar = Calendar.getInstance()
        dates.add(dateFormat.format(calendar.time))

        for (i in 1 until 10) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            dates.add(dateFormat.format(calendar.time))
        }
        dateAdapter=DateSlotAdapter(this,dates)
        date_rv=findViewById(R.id.date_rv)
        date_rv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        date_rv.adapter = dateAdapter



    }
}