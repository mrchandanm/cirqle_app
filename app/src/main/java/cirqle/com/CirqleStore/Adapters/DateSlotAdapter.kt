package cirqle.com.CirqleStore.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cirqle.com.R

class DateSlotAdapter(private var context:Context, private var list:MutableList<String>):RecyclerView.Adapter<DateSlotAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_date_slot_rv,parent, false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.date.text=list[position].toString()
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val date:TextView=itemView.findViewById(R.id.dateSlot)
    }
}